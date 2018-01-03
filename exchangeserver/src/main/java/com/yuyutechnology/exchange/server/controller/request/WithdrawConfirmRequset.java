package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class WithdrawConfirmRequset extends BaseRequest{
	private int goldBullion;
	private String userEmail;
	private String checkToken;

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
	
	public String getCheckToken() {
		return checkToken;
	}

	public void setCheckToken(String checkToken) {
		this.checkToken = checkToken;
	}

	/**
	 * 判断参数是否为空
	 * 
	 * @return
	 */
	public boolean empty() {
		if (this.goldBullion <= 0) {
			return true;
		}
		if (StringUtils.isBlank(this.userEmail)) {
			return true;
		}
		if (StringUtils.isBlank(this.checkToken)) {
			return true;
		}
		return false;
	}
}
