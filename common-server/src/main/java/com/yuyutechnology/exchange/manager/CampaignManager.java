/**
 * 
 */
package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yuyutechnology.exchange.dto.InviterInfo;
import com.yuyutechnology.exchange.pojo.Campaign;

/**
 * @author suzan.wu
 *
 */
public interface CampaignManager {
	/**
	 * 新增活动
	 * 
	 * @param startTime
	 * @param endTime
	 * @param campaignBudget
	 * @param inviterBonus
	 * @param inviteeBonus
	 */
	void addCampaign(Date startTime, Date endTime, BigDecimal campaignBudget, BigDecimal inviterBonus,
			BigDecimal inviteeBonus);

	/**
	 * @return
	 * 
	 */
	List<Campaign> getCampaignList();

	/**
	 * @param userId
	 * @return
	 */
	InviterInfo getInviterInfo(Integer userId);

	// TODO 开启关闭活动
	// TODO 修改奖励金
	// TODO 追加预算
}
