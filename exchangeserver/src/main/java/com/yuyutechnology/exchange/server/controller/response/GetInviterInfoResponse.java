/**
 * 
 */
package com.yuyutechnology.exchange.server.controller.response;

import java.math.BigDecimal;

/**
 * @author suzan.wu
 *
 */
public class GetInviterInfoResponse extends BaseResponse{
	private String userName;
	private int inviteQuantity;
	private BigDecimal inviteBonus;

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
