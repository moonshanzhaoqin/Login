/**
 * 
 */
package com.yuyutechnology.exchange.server.controller.response;

import com.yuyutechnology.exchange.dto.CampaignInfo;

/**
 * @author suzan.wu
 *
 */
public class GetCampaignInfoResponse extends BaseResponse {
	private CampaignInfo campaignInfo;

	public CampaignInfo getCampaignInfo() {
		return campaignInfo;
	}

	public void setCampaignInfo(CampaignInfo campaignInfo) {
		this.campaignInfo = campaignInfo;
	}
}
