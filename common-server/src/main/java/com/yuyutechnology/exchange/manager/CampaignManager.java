/**
 * 
 */
package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yuyutechnology.exchange.dto.CampaignInfo;
import com.yuyutechnology.exchange.dto.InviterInfo;
import com.yuyutechnology.exchange.pojo.Campaign;
import com.yuyutechnology.exchange.pojo.Collect;

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


	/**
	 * @param areaCode
	 * @param userPhone
	 * @param inviterCode
	 * @param sharePath
	 * @return
	 */
	String collect(String areaCode, String userPhone, String inviterCode, int sharePath);

	/**
	 * @return
	 */
	CampaignInfo getCampaignInfo();

	/**
	 * 发放奖励金
	 * 
	 * @param userId
	 * @param areaCode
	 * @param userPhone
	 */
	void grantBouns(Integer userId, String areaCode, String userPhone);

	/**
	 * 有效的领取
	 * @param areaCode
	 * @param userPhone
	 * @return
	 */
	Collect activeCollect(String areaCode, String userPhone);

	// TODO 开启关闭活动
	// TODO 修改奖励金
	// TODO 追加预算
}
