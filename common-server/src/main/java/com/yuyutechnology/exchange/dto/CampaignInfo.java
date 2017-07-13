/**
 * 
 */
package com.yuyutechnology.exchange.dto;

import java.math.BigDecimal;

/**
 * @author suzan.wu
 *
 */
public class CampaignInfo {
	private BigDecimal inviterBonus;
	private BigDecimal inviteeBonus;
	private long quantityRestriction;
	private long activeTime;

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

	public long getQuantityRestriction() {
		return quantityRestriction;
	}

	public void setQuantityRestriction(long quantityRestriction) {
		this.quantityRestriction = quantityRestriction;
	}

	public long getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(long activeTime) {
		this.activeTime = activeTime;
	}

}
