package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CampaignDAO;
import com.yuyutechnology.exchange.dao.CollectDAO;
import com.yuyutechnology.exchange.dao.InviterDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.dto.CampaignInfo;
import com.yuyutechnology.exchange.dto.InviterInfo;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.manager.CampaignManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.TransDetailsManager;
import com.yuyutechnology.exchange.pojo.Campaign;
import com.yuyutechnology.exchange.pojo.Collect;
import com.yuyutechnology.exchange.pojo.Inviter;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.push.PushManager;
import com.yuyutechnology.exchange.util.ShareCodeUtil;
import com.yuyutechnology.exchange.util.page.PageBean;

/**
 * @author suzan.wu
 *
 */
@Service
public class CampaignManagerImpl implements CampaignManager {

	private static Logger logger = LogManager.getLogger(CampaignManagerImpl.class);

	@Autowired
	RedisDAO redisDAO;
	@Autowired
	CampaignDAO campaignDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	InviterDAO inviterDAO;
	@Autowired
	CollectDAO collectDAO;
	@Autowired
	TransferDAO transferDAO;
	@Autowired
	ConfigManager configManager;
	@Autowired
	PushManager pushManager;
	@Autowired
	TransDetailsManager transDetailsManager;

	@Override
	public InviterInfo getInviterInfo(Integer userId) {
		logger.info("get {} InviterInfo ==>", userId);

		InviterInfo inviterInfo = new InviterInfo();

		User user = userDAO.getUser(userId);
		if (user == null) {
			logger.warn("Can not find the user!!!");
			return null;
		}
		inviterInfo.setUserName(user.getUserName());

		Inviter inviter = inviterDAO.getInviter(userId);
		if (inviter == null) {
			inviter = new Inviter(userId, 0, BigDecimal.ZERO);
			inviterDAO.updateInviter(inviter);
		}
		inviterInfo.setInviteQuantity(inviter.getInviteQuantity());
		inviterInfo.setInviteBonus(inviter.getInviteBonus());
		inviterInfo.setInviterCode(ShareCodeUtil.toSerialCode(userId));
		return inviterInfo;
	}

	@Override
	public void addCampaign(Date startTime, Date endTime, BigDecimal campaignBudget, BigDecimal inviterBonus,
			BigDecimal inviteeBonus) {
		campaignDAO.updateCampaign(new Campaign(startTime, endTime, ServerConsts.CAMPAIGN_STATUS_OFF, campaignBudget,
				campaignBudget, inviterBonus, inviteeBonus, new Date(), new Date()));
		return;
	}

	@Override
	public PageBean getCampaignList(int currentPage) {

		String hql = "from Campaign order by campaignStatus desc,campaignId";

		return campaignDAO.getCampaignsByPage(hql, new ArrayList<Object>(), currentPage, 10);
	}

	@Override
	public Collect activeCollect(String areaCode, String userPhone) {
		logger.info("get activeCollect for {} ==>", areaCode + userPhone);

		Calendar earliestTime = Calendar.getInstance();
		earliestTime.add(Calendar.HOUR_OF_DAY,
				-configManager.getConfigLongValue(ConfigKeyEnum.COLLECT_ACTIVE_TIME, 24L).intValue()); // 现在时间的1小时前
		/* 在有效时间内的领取的 */
		String hql = "from Collect  where areaCode=? and userPhone=? and collectTime > ?";
		Collect collect = collectDAO.findHQL(hql, new Object[] { areaCode, userPhone, earliestTime.getTime() });
		logger.info(collect == null ? null : collect.toString());
		return collect;
	}

	@Override
	public String collect(String areaCode, String userPhone, String inviterCode, int sharePath) {
		logger.info("collect {} ==>", areaCode + userPhone);
		/* 邀请人 */
		Integer inviterId = Integer.valueOf(String.valueOf(ShareCodeUtil.codeToId(inviterCode)));
		logger.info("--inviterId : {}", inviterId);

		Inviter inviter = inviterDAO.getInviter(inviterId);
		if (inviter == null) {
			return RetCodeConsts.INVITERCODE_INCORRECT;
		}

		/* 活动信息 */
		Campaign campaign = activeCampaign();
		if (campaign == null) {
			/* 现在没有活动 */
			return RetCodeConsts.NO_CAMPAIGN;
		}

		if (campaign.getBudgetSurplus().compareTo(campaign.getInviterBonus().add(campaign.getInviteeBonus())) == -1) {
			/* 钱不够 */
			logger.info("The budget is not enough");
			return RetCodeConsts.EXCESS_BUDGET;
		}
		collectDAO.updateCollect(
				new Collect(areaCode, userPhone, inviterId, campaign.getCampaignId(), campaign.getInviterBonus(),
						campaign.getInviteeBonus(), new Date(), ServerConsts.COLLECT_STATUS_UNREGISTER, sharePath));
		return RetCodeConsts.RET_CODE_SUCCESS;
	}

	/**
	 * 有效的活动
	 * 
	 * @return
	 */
	private Campaign activeCampaign() {
		logger.info("get activeCampaign ==>");

		String str = redisDAO.getValueByKey(ServerConsts.REDIS_KEY_ACTIVE_CAMPAIGN);
		if (str == null) {
			/* 现在没有进行中的活动 */
			logger.info("There is no active campaign now");
			return null;
		}
		Integer campaignId = Integer.valueOf(str);
		Campaign campaign = campaignDAO.getCampaign(campaignId);
		if (campaign == null || campaign.getCampaignStatus() == ServerConsts.CAMPAIGN_STATUS_OFF) {
			/* 活动已关闭 */
			redisDAO.deleteData(ServerConsts.REDIS_KEY_ACTIVE_CAMPAIGN);
			logger.info("The campaign is closed");
			return null;
		}
		Date now = new Date();
		logger.info("now: {}  StartTime : {}  EndTime : {}", now, campaign.getStartTime(), campaign.getEndTime());

		if (now.after(campaign.getEndTime())) {
			/* 活动已结束 */
			// redisDAO.deleteData(ServerConsts.REDIS_KEY_ACTIVE_CAMPAIGN);
			logger.info("The campaign is over");
			return null;
		}
		if (now.before(campaign.getStartTime())) {
			/* 活动还未开始 */
			logger.info("The campaign has not yet started");
			return null;
		}
		logger.info("campaignId : {}", campaign.getCampaignId());
		return campaign;
	}

	@Override
	public CampaignInfo getCampaignInfo() {
		Campaign campaign = activeCampaign();
		if (campaign == null) {
			/* 现在没有活动 */
			return null;
		}
		if (campaign.getBudgetSurplus().compareTo(campaign.getInviterBonus().add(campaign.getInviteeBonus())) == -1) {
			/* 钱不够 */
			logger.info("The budget is not enough");
			return null;
		}
		CampaignInfo campaignInfo = new CampaignInfo();
		campaignInfo.setInviteeBonus(campaign.getInviteeBonus());
		campaignInfo.setInviterBonus(campaign.getInviterBonus());
		campaignInfo.setActiveTime(configManager.getConfigLongValue(ConfigKeyEnum.COLLECT_ACTIVE_TIME, 24L));
		campaignInfo.setQuantityRestriction(
				configManager.getConfigLongValue(ConfigKeyEnum.INVITE_QUANTITY_RESTRICTION, 1000L));
		return campaignInfo;
	}

	@Override
	public void grantBonus(Integer userId, String areaCode, String userPhone) {
		logger.info("grantBonus : {} ", userId);

		/* 领取是否过了有效期 */
		Collect collect = activeCollect(areaCode, userPhone);
		if (collect == null) {
			logger.info("collect is not active");
			return;
		}
		collect.setRegisterStatus(ServerConsts.COLLECT_STATUS_REGISTER);
		collectDAO.updateCollect(collect);

		/* 判断是否有钱可以支付 */
		Campaign campaign = campaignDAO.getCampaign(collect.getCampaignId());
		if (campaign.getBudgetSurplus().compareTo(collect.getInviteeBonus().add(collect.getInviterBonus())) == -1) {
			logger.info("The budget is not enough");
			return;
		}

		/* 判断邀请人的人数限制 */
		Inviter inviter = inviterDAO.getInviter(collect.getInviterId());
		if (inviter.getInviteQuantity() >= configManager.getConfigLongValue(ConfigKeyEnum.INVITE_QUANTITY_RESTRICTION,
				1000L)) {
			logger.info("Exceed quanlity restriction");
			return;
		}

		/* 给邀请人发钱 */
		settlement(collect.getInviterId(), collect.getInviterBonus());
		/* 给注册用户发钱 */
		settlement(userId, collect.getInviteeBonus());

		/* 更新预算 */
		campaign.setBudgetSurplus(
				campaign.getBudgetSurplus().subtract(collect.getInviteeBonus().add(collect.getInviterBonus())));
		campaign.setUpdateTime(new Date());
		campaignDAO.updateCampaign(campaign);

		/* 更新邀请人信息 */
		inviter.setInviteBonus(inviter.getInviteBonus().add(collect.getInviterBonus()));
		inviter.setInviteQuantity(inviter.getInviteQuantity() + 1);
		inviterDAO.updateInviter(inviter);

	}

	private void settlement(Integer userId, BigDecimal bonus) {
		
		User user = userDAO.getUser(userId);

		User system = userDAO.getSystemUser();

		/* 生成订单 */
		String transferId = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_TRANSACTION);
		Transfer transfer = new Transfer();
		transfer.setTransferId(transferId);
		transfer.setUserFrom(system.getUserId());
		transfer.setUserTo(userId);
		transfer.setCreateTime(new Date());
		transfer.setFinishTime(new Date());
		transfer.setCurrency(ServerConsts.CURRENCY_OF_GOLDPAY);
		transfer.setTransferAmount(bonus);
		transfer.setTransferComment("inviteBonus");
		transfer.setTransferType(ServerConsts.TRANSFER_TYPE_IN_INVITE_CAMPAIGN);
		transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
		transfer.setAreaCode(user.getAreaCode());
		transfer.setPhone(user.getUserPhone());
		transferDAO.addTransfer(transfer);

		/* 生成详情 */
		transDetailsManager.addTransDetails(transferId, userId, system.getUserId(), "", "", "",
				ServerConsts.CURRENCY_OF_GOLDPAY, bonus, null, ServerConsts.TRANSFER_TYPE_IN_INVITE_CAMPAIGN);

		/* 账户加款 */
		walletDAO.updateWalletByUserIdAndCurrency(userId, ServerConsts.CURRENCY_OF_GOLDPAY, bonus, "+",
				ServerConsts.TRANSFER_TYPE_IN_INVITE_CAMPAIGN, transferId);

		/* 系统扣款 */
		walletDAO.updateWalletByUserIdAndCurrency(system.getUserId(), ServerConsts.CURRENCY_OF_GOLDPAY, bonus, "-",
				ServerConsts.TRANSFER_TYPE_IN_INVITE_CAMPAIGN, transferId);


		pushManager.push4Invite(user, transferId, bonus);

	}

	@Override
	public void changeBonus(Integer campaignId, BigDecimal inviterBonus, BigDecimal inviteeBonus) {
		Campaign campaign = campaignDAO.getCampaign(campaignId);
		campaign.setInviterBonus(inviterBonus);
		campaign.setInviteeBonus(inviteeBonus);
		campaign.setUpdateTime(new Date());
		campaignDAO.updateCampaign(campaign);

	}

	@Override
	public void additionalBudget(Integer campaignId, BigDecimal additionalBudget) {
		Campaign campaign = campaignDAO.getCampaign(campaignId);
		campaign.setBudgetSurplus(campaign.getBudgetSurplus().add(additionalBudget));
		campaign.setCampaignBudget(campaign.getCampaignBudget().add(additionalBudget));
		campaign.setUpdateTime(new Date());
		campaignDAO.updateCampaign(campaign);
	}

	@Override
	public Integer openCampaign(Integer campaignId) {
		String str = redisDAO.getValueByKey(ServerConsts.REDIS_KEY_ACTIVE_CAMPAIGN);
		if (str != null) {
			Campaign activeCampaign = campaignDAO.getCampaign(Integer.valueOf(str));
			if (activeCampaign != null && activeCampaign.getCampaignStatus() == ServerConsts.CAMPAIGN_STATUS_ON) {
				return activeCampaign.getCampaignId();
			}
		}

		redisDAO.saveData(ServerConsts.REDIS_KEY_ACTIVE_CAMPAIGN, campaignId);
		Campaign campaign = campaignDAO.getCampaign(campaignId);
		campaign.setCampaignStatus(ServerConsts.CAMPAIGN_STATUS_ON);
		campaign.setUpdateTime(new Date());
		campaignDAO.updateCampaign(campaign);
		return campaignId;
	}

	@Override
	public void closeCampaign(Integer campaignId) {
		redisDAO.deleteData(ServerConsts.REDIS_KEY_ACTIVE_CAMPAIGN);
		Campaign campaign = campaignDAO.getCampaign(campaignId);
		campaign.setCampaignStatus(ServerConsts.CAMPAIGN_STATUS_OFF);
		campaign.setUpdateTime(new Date());
		campaignDAO.updateCampaign(campaign);
	}

}
