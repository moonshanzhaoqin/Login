package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CampaignDAO;
import com.yuyutechnology.exchange.dao.CollectDAO;
import com.yuyutechnology.exchange.dao.InviterDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dto.InviterInfo;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.manager.CampaignManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.pojo.Campaign;
import com.yuyutechnology.exchange.pojo.Collect;
import com.yuyutechnology.exchange.pojo.Inviter;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.util.ShareCodeUtil;

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
	public boolean isCollected(String areaCode, String userPhone) {
		Calendar earliestTime = Calendar.getInstance();
		earliestTime.add(Calendar.HOUR_OF_DAY,
				configManager.getConfigLongValue(ConfigKeyEnum.COLLECT_ACTIVE_TIME, 1L).intValue()); // 现在时间的1小时前
		/* 在有效时间内的领取的 */
		String hql = "from Collect  where areaCode=? and userPhone=? and collectTime > ?";
		Collect collect = collectDAO.findHQL(hql, new Object[] { areaCode, userPhone, earliestTime.getTime() });
		if (collect != null) {
			return true;
		}
		return false;
	}

	@Override
	public void collect(String areaCode, String userPhone, String inviterCode, int sharePath) {

		/* 邀请人 */
		Integer inviterId = Integer.valueOf(String.valueOf(ShareCodeUtil.codeToId(inviterCode)));

		/* 活动信息 */
		Campaign campaign = activeCampaign();
		if (campaign == null) {
			/* 现在没有活动 */
			return;
		}

		if (campaign.getBudgetSurplus().equals(campaign.getInviterBonus().add(campaign.getInviteeBonus()))) {

			
			
			
			
		}

		new Collect(areaCode, userPhone, inviterId, campaign.getCampaignId(), campaign.getInviterBonus(),
				campaign.getInviteeBonus(), new Date(), ServerConsts.COLLECT_STATUS_UNREGISTER, sharePath);

	}

	Campaign activeCampaign() {
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

}
