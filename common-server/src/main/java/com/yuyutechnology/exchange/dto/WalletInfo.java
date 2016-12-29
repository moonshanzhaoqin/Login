package com.yuyutechnology.exchange.dto;

import java.math.BigDecimal;

public class WalletInfo {
	private String currency;
	private String nameEn;
	private String nameCn;
	private String nameHk;
	private int currencyStatus;
	private String currencyUnit;
	private BigDecimal balance;

	public WalletInfo() {

	}

	public WalletInfo(String currency, String nameEn, String nameCn, String nameHk, int currencyStatus,
			String currencyUnit, BigDecimal balance) {
		super();
		this.currency = currency;
		this.nameEn = nameEn;
		this.nameCn = nameCn;
		this.nameHk = nameHk;
		this.currencyStatus = currencyStatus;
		this.currencyUnit = currencyUnit;
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

	public String getCurrencyUnit() {
		return currencyUnit;
	}

	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}
}
