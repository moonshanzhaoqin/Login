package com.yuyutechnology.exchange.server.controller.response;

import java.util.List;

import com.yuyutechnology.exchange.form.UserInfo;
import com.yuyutechnology.exchange.pojo.Wallet;

public class LoginResponse extends BaseResponse {
	private UserInfo user;
	private List<Wallet> wallets;
	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}
	public List<Wallet> getWallets() {
		return wallets;
	}
	public void setWallets(List<Wallet> wallets) {
		this.wallets = wallets;
	}
}
