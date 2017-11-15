/**
 * 
 */
package com.yuyutechnology.exchange.crm.request;

import java.math.BigDecimal;

/**
 * @author suzan.wu
 *
 */
public class AddBudgetRequest {
	private Integer campaignId;
	private BigDecimal additionalBudget;

	@Override
	public String toString() {
		return "AddBudgetRequest [campaignId=" + campaignId + ", additionalBudget=" + additionalBudget + "]";
	}

	public Integer getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public BigDecimal getAdditionalBudget() {
		return additionalBudget;
	}

	public void setAdditionalBudget(BigDecimal additionalBudget) {
		this.additionalBudget = additionalBudget;
	}
	
}
