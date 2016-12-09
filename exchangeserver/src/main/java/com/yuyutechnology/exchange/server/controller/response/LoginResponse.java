package com.yuyutechnology.exchange.server.controller.response;

import java.util.List;

import com.yuyutechnology.exchange.form.UserInfo;
import com.yuyutechnology.exchange.pojo.Wallet;

public class LoginResponse extends BaseResponse {
	private UserInfo user;
	private List<Wallet> wallets;
	private String sessionToken;
	private String loginToken;
	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}
	public List<Wallet> getWallets() {
		return wallets;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public void setWallets(List<Wallet> wallets) {
		this.wallets = wallets;
	}
	public String getLoginToken() {
		return loginToken;
	}
	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}
}
