package com.yuyutechnology.exchange.server.controller.response;

import java.util.List;

import com.yuyutechnology.exchange.dto.WalletInfo;

public class GetCurrentBalanceResponse extends BaseResponse {
	private List<WalletInfo> wallets;

	public List<WalletInfo> getWallets() {
		return wallets;
	}

	public void setWallets(List<WalletInfo> wallets) {
		this.wallets = wallets;
	}
}
