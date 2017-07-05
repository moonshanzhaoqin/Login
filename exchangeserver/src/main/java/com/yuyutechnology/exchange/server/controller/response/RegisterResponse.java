package com.yuyutechnology.exchange.server.controller.response;

import com.yuyutechnology.exchange.dto.UserInfo;

public class RegisterResponse extends BaseResponse {
	private String sessionToken;
	private String loginToken;
//	private boolean paypalRecharge;
	private UserInfo user;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

//	public boolean isPaypalRecharge() {
//		return paypalRecharge;
//	}
//
//	public void setPaypalRecharge(boolean paypalRecharge) {
//		this.paypalRecharge = paypalRecharge;
//	}

}
