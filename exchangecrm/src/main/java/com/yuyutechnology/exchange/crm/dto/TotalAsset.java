package com.yuyutechnology.exchange.crm.dto;

import java.math.BigDecimal;

import com.yuyutechnology.exchange.pojo.Currency;

public class TotalAsset {
	private Currency currency;
	private BigDecimal systemTotalAsset;
	private BigDecimal userTotalAsset;

	public TotalAsset() {
	}

	public TotalAsset(Currency currency, BigDecimal systemTotalAsset, BigDecimal userTotalAsset) {
		super();
		this.currency = currency;
		this.systemTotalAsset = systemTotalAsset;
		this.userTotalAsset = userTotalAsset;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public BigDecimal getSystemTotalAsset() {
		return systemTotalAsset;
	}

	public void setSystemTotalAsset(BigDecimal systemTotalAsset) {
		this.systemTotalAsset = systemTotalAsset;
	}

	public BigDecimal getUserTotalAsset() {
		return userTotalAsset;
	}

	public void setUserTotalAsset(BigDecimal userTotalAsset) {
		this.userTotalAsset = userTotalAsset;
	}
}
