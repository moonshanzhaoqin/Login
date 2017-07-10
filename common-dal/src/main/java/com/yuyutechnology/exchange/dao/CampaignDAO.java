/**
 * 
 */
package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.Campaign;

/**
 * @author suzan.wu
 *
 */
public interface CampaignDAO {

	/**
	 * @param campaignId
	 * @return
	 */
	Campaign getCampaign(Integer campaignId);

	/**
	 * @param campaign
	 */
	void updateCampaign(Campaign campaign);

	/**
	 * @return
	 */
	List<Campaign> getCampaigns();

}
