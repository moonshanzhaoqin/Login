/**
 * 
 */
package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yuyutechnology.exchange.dto.CampaignInfo;
import com.yuyutechnology.exchange.dto.InviterInfo;
import com.yuyutechnology.exchange.pojo.Collect;
import com.yuyutechnology.exchange.util.page.PageBean;

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
	 * 
	 * @param i 
	 * @return
	 * 
	 */
	PageBean getCampaignList(int currentPage);

	/**
	 * 获取邀请人信息
	 * 
	 * @param userId
	 * @return
	 */
	InviterInfo getInviterInfo(Integer userId);

	/**
	 * 领取
	 * 
	 * @param areaCode
	 * @param userPhone
	 * @param inviterCode
	 * @param sharePath
	 * @return
	 */
	String collect(String areaCode, String userPhone, String inviterCode, int sharePath);

	/**
	 * 获取活动信息
	 * 
	 * @return
	 */
	CampaignInfo getCampaignInfo();

	/**
	 * 发放奖励金
	 * 
	 * @param userId
	 * @param areaCode
	 * @param userPhone
	 * @return 
	 */
	List<String> grantBonus(Integer userId, String areaCode, String userPhone);

	/**
	 * 有效的领取记录
	 * 
	 * @param areaCode
	 * @param userPhone
	 * @return
	 */
	Collect activeCollect(String areaCode, String userPhone);

	/**
	 * 修改奖励金
	 * 
	 * @param campaignId
	 * @param inviterBonus
	 * @param inviteeBonus
	 */
	void changeBonus(Integer campaignId, BigDecimal inviterBonus, BigDecimal inviteeBonus);

	/**
	 * 追加预算
	 * 
	 * @param campaignId
	 * @param additionalBudget
	 */
	void additionalBudget(Integer campaignId, BigDecimal additionalBudget);

	/**
	 * 开启活动
	 * 
	 * @param campaignId
	 * @return
	 */
	Integer openCampaign(Integer campaignId);

	/**
	 * 关闭活动
	 * 
	 * @param campaignId
	 */
	void closeCampaign(Integer campaignId);

}
