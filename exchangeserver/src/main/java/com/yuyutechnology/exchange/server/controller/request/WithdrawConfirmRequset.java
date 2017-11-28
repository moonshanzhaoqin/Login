package com.yuyutechnology.exchange.server.controller.request;

public class WithdrawConfirmRequset {
	private int goldBullion;
	private String userEmail;

	public int getGoldBullion() {
		return goldBullion;
	}

	public void setGoldBullion(int goldBullion) {
		this.goldBullion = goldBullion;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
}
