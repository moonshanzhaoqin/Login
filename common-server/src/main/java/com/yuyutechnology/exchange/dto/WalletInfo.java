package com.yuyutechnology.exchange.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yuyutechnology.exchange.pojo.Currency;

public class WalletInfo {
	private String currency;
	private String nameEn;
	private String nameCn;
	private String nameHk;
	private int currencyStatus;
	private BigDecimal balance;

	public WalletInfo() {

	}

	public WalletInfo(String currency, String nameEn, String nameCn, String nameHk, int currencyStatus,
			BigDecimal balance) {
		super();
		this.currency = currency;
		this.nameEn = nameEn;
		this.nameCn = nameCn;
		this.nameHk = nameHk;
		this.currencyStatus = currencyStatus;
		this.balance = balance;
	}

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

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}

	public String getNameHk() {
		return nameHk;
	}

	public void setNameHk(String nameHk) {
		this.nameHk = nameHk;
	}

	public int getCurrencyStatus() {
		return currencyStatus;
	}

	public void setCurrencyStatus(int currencyStatus) {
		this.currencyStatus = currencyStatus;
	}
}
