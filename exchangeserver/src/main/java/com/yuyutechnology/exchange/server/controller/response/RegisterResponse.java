package com.yuyutechnology.exchange.server.controller.response;

import java.util.List;

import com.yuyutechnology.exchange.dto.UserInfo;
import com.yuyutechnology.exchange.dto.WalletInfo;

public class RegisterResponse extends BaseResponse {
	private UserInfo user;
	private List<WalletInfo> wallets;
	private String sessionToken;
	private String loginToken;
	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}
	public List<WalletInfo> getWallets() {
		return wallets;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public void setWallets(List<WalletInfo> wallets) {
		this.wallets = wallets;
	}
	public String getLoginToken() {
		return loginToken;
	}
	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}
	
	public static void main(String[] args) {
		LoginResponse aa = new LoginResponse();
		System.out.println(aa.getApiName());
	}
}
