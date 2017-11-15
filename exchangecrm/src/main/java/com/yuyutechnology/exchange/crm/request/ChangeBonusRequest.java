/**
 * 
 */
package com.yuyutechnology.exchange.crm.request;

import java.math.BigDecimal;

/**
 * @author suzan.wu
 *
 */
public class ChangeBonusRequest {
	private Integer campaignId;
	private BigDecimal inviterBonus;
	private BigDecimal inviteeBonus;

	@Override
	public String toString() {
		return "ChangeBonusRequest [campaignId=" + campaignId + ", inviterBonus=" + inviterBonus + ", inviteeBonus="
				+ inviteeBonus + "]";
	}

	public Integer getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public BigDecimal getInviterBonus() {
		return inviterBonus;
	}

	public void setInviterBonus(BigDecimal inviterBonus) {
		this.inviterBonus = inviterBonus;
	}

	public BigDecimal getInviteeBonus() {
		return inviteeBonus;
	}

	public void setInviteeBonus(BigDecimal inviteeBonus) {
		this.inviteeBonus = inviteeBonus;
	}
	
}
