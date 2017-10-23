/**
 * 
 */
package com.yuyutechnology.exchange.dto;

import java.math.BigDecimal;

/**
 * @author suzan.wu
 *
 */
public class InviterInfo {
	private String userName;
	private int inviteQuantity;
	private BigDecimal inviteBonus;
	private String inviterCode;

	public String getInviterCode() {
		return inviterCode;
	}

	public void setInviterCode(String inviterCode) {
		this.inviterCode = inviterCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getInviteQuantity() {
		return inviteQuantity;
	}

	public void setInviteQuantity(int inviteQuantity) {
		this.inviteQuantity = inviteQuantity;
	}

	public BigDecimal getInviteBonus() {
		return inviteBonus;
	}

	public void setInviteBonus(BigDecimal inviteBonus) {
		this.inviteBonus = inviteBonus;
	}
}
