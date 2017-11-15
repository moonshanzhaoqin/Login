/**
 * 
 */
package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.Campaign;
import com.yuyutechnology.exchange.util.page.PageBean;

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
	 * @param hql
	 * @param values
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	PageBean getCampaignsByPage(String hql, List<Object> values, int currentPage, int pageSize);

}
