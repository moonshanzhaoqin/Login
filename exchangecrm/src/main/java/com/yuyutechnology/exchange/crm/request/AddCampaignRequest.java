/**
 * 
 */
package com.yuyutechnology.exchange.crm.request;

import org.apache.commons.lang.StringUtils;

/**
 * @author suzan.wu
 *
 */
public class AddCampaignRequest {
	private String startTime;
	private String endTime;
	private String campaignBudget;
	private String inviterBonus;
	private String inviteeBonus;

	@Override
	public String toString() {
		return "AddCampaignRequest [startTime=" + startTime + ", endTime=" + endTime + ", campaignBudget="
				+ campaignBudget + ", inviterBonus=" + inviterBonus + ", inviteeBonus=" + inviteeBonus + "]";
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCampaignBudget() {
		return campaignBudget;
	}

	public void setCampaignBudget(String campaignBudget) {
		this.campaignBudget = campaignBudget;
	}

	public String getInviterBonus() {
		return inviterBonus;
	}

	public void setInviterBonus(String inviterBonus) {
		this.inviterBonus = inviterBonus;
	}

	public String getInviteeBonus() {
		return inviteeBonus;
	}

	public void setInviteeBonus(String inviteeBonus) {
		this.inviteeBonus = inviteeBonus;
	}

	/**
	 * 判断参数是否为空
	 * 
	 * @return
	 */
	public boolean Empty() {
		if (StringUtils.isBlank(this.startTime)) {
			return true;
		}
		if (StringUtils.isBlank(this.endTime)) {
			return true;
		}
		if (StringUtils.isBlank(this.campaignBudget)) {
			return true;
		}
		if (StringUtils.isBlank(this.inviterBonus)) {
			return true;
		}if (StringUtils.isBlank(this.inviterBonus)) {
			return true;
		}
		return false;
	}

}
