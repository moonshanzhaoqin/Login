package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dto.CampaignInfo;
import com.yuyutechnology.exchange.dto.InviterInfo;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.manager.CampaignManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.pojo.Campaign;
import com.yuyutechnology.exchange.pojo.Collect;
import com.yuyutechnology.exchange.pojo.Inviter;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.push.PushManager;
import com.yuyutechnology.exchange.util.ShareCodeUtil;

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
	InviterDAO inviterDAO;
	@Autowired
	CollectDAO collectDAO;
	@Autowired
	ConfigManager configManager;
	@Autowired
	PushManager pushManager;

	@Override
	public InviterInfo getInviterInfo(Integer userId) {
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
	public List<Campaign> getCampaignList() {
		return campaignDAO.getCampaigns();
	}

	@Override
	public Collect activeCollect(String areaCode, String userPhone) {
		Calendar earliestTime = Calendar.getInstance();
		earliestTime.add(Calendar.HOUR_OF_DAY,
				configManager.getConfigLongValue(ConfigKeyEnum.COLLECT_ACTIVE_TIME, 1L).intValue()); // 现在时间的1小时前
		/* 在有效时间内的领取的 */
		String hql = "from Collect  where areaCode=? and userPhone=? and collectTime > ?";
		Collect collect = collectDAO.findHQL(hql, new Object[] { areaCode, userPhone, earliestTime.getTime() });
		return collect;
	}

	@Override
	public String collect(String areaCode, String userPhone, String inviterCode, int sharePath) {

		/* 邀请人 */
		Integer inviterId = Integer.valueOf(String.valueOf(ShareCodeUtil.codeToId(inviterCode)));

		/* 活动信息 */
		Campaign campaign = activeCampaign();
		if (campaign == null) {
			/* 现在没有活动 */
			return RetCodeConsts.NO_CAMPAIGN;
		}

		if (campaign.getBudgetSurplus().compareTo(campaign.getInviterBonus().add(campaign.getInviteeBonus())) == -1) {
			/* 钱不够 */
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
		String str = redisDAO.getValueByKey(ServerConsts.REDIS_KEY_ACTIVE_CAMPAIGN);
		if (str == null) {
			/* 现在没有进行中的活动 */
			logger.info("There is no active campaign now");
			return null;
		}
		Integer campaignId = Integer.valueOf(str);
		Campaign campaign = campaignDAO.getCampaign(campaignId);
		if (campaign.getCampaignStatus() == ServerConsts.CAMPAIGN_STATUS_OFF) {
			/* 活动已关闭 */
			redisDAO.deleteData(ServerConsts.REDIS_KEY_ACTIVE_CAMPAIGN);
			return null;
		}
		Calendar now = Calendar.getInstance();
		if (now.after(campaign.getEndTime())) {
			/* 活动已结束 */
			redisDAO.deleteData(ServerConsts.REDIS_KEY_ACTIVE_CAMPAIGN);
			return null;
		}
		if (now.before(campaign.getStartTime())) {
			/* 活动还未开始 */
			return null;
		}
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
	public void grantBouns(Integer userId, String areaCode, String userPhone) {
		/* 领取是否过了有效期 */
		Collect collect = activeCollect(areaCode, userPhone);
		if (collect == null) {
			return;
		}

		/* 判断是否有钱可以支付 */
		Campaign campaign = campaignDAO.getCampaign(collect.getCampaignId());
		if (campaign.getBudgetSurplus().compareTo(collect.getInviteeBonus().add(collect.getInviterBonus())) == -1) {
			return;
		}

		/* 判断邀请人的人数限制 */
		Inviter inviter = inviterDAO.getInviter(collect.getInviterId());
		if (inviter.getInviteQuantity() >= configManager.getConfigLongValue(ConfigKeyEnum.INVITE_QUANTITY_RESTRICTION,
				1000L)) {
			return;
		}

		// TODO 给邀请人发钱 collect.getInviterId() collect.getInviterBonus()
		// TODO 给注册用户发钱 userId collect.getInviteeBonus()

		/* 更新预算 */
		campaign.setBudgetSurplus(
				campaign.getBudgetSurplus().subtract(collect.getInviteeBonus().add(collect.getInviterBonus())));
		campaignDAO.updateCampaign(campaign);

		/* 更新邀请人信息 */
		inviter.setInviteBonus(inviter.getInviteBonus().add(collect.getInviterBonus()));
		inviter.setInviteQuantity(inviter.getInviteQuantity() + 1);
		inviterDAO.updateInviter(inviter);

		/* 推送邀请人 */
		User inviterUser = userDAO.getUser(collect.getInviterId());
		pushManager.push4Invite(inviterUser.getPushId(), inviterUser.getPushTag(), collect.getInviterBonus());
		/* 推送注册用户 */
		User inviteeUser = userDAO.getUser(userId);
		pushManager.push4Invite(inviteeUser.getPushId(), inviteeUser.getPushTag(), collect.getInviteeBonus());
	}

	@Override
	public void changeBouns(Integer campaignId, BigDecimal inviterBonus, BigDecimal inviteeBonus) {
		Campaign campaign = campaignDAO.getCampaign(campaignId);
		campaign.setInviterBonus(inviterBonus);
		campaign.setInviteeBonus(inviteeBonus);
		campaignDAO.updateCampaign(campaign);

	}

	@Override
	public void additionalBudget(Integer campaignId, BigDecimal additionalBudget) {
		Campaign campaign = campaignDAO.getCampaign(campaignId);
		campaign.setBudgetSurplus(campaign.getBudgetSurplus().add(additionalBudget));
		campaign.setCampaignBudget(campaign.getCampaignBudget().add(additionalBudget));
		campaignDAO.updateCampaign(campaign);
	}

}
