package com.yuyutechnology.exchange.server.controller.response;

import java.util.List;

public class LoginResponse extends BaseResponse {
	private UserInfo user;
	private List<WalletInfo> wallets;
	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}
	public List<WalletInfo> getWallets() {
		return wallets;
	}
	public void setWallets(List<WalletInfo> wallets) {
		this.wallets = wallets;
	}
	
}
