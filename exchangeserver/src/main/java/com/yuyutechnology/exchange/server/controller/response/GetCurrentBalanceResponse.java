package com.yuyutechnology.exchange.server.controller.response;

import java.util.List;

import com.yuyutechnology.exchange.pojo.Wallet;

public class GetCurrentBalanceResponse extends BaseResponse{
	private List<Wallet> wallets;

	public List<Wallet> getWallets() {
		return wallets;
	}

	public void setWallets(List<Wallet> wallets) {
		this.wallets = wallets;
	}
}
