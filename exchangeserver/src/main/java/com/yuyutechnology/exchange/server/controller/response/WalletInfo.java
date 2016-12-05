package com.yuyutechnology.exchange.server.controller.response;

import java.math.BigDecimal;

public class WalletInfo {
	private String currency;
	private BigDecimal balance;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}
