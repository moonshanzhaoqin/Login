package com.yuyutechnology.exchange.server.controller.dto;

import java.math.BigDecimal;
import java.util.Date;

public class WithdrawDTO {
	private BigDecimal amount;
	private String currencyUnit;
	private Date createTime;
	private int WithdrawStatus;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getWithdrawStatus() {
		return WithdrawStatus;
	}

	public void setWithdrawStatus(int withdrawStatus) {
		WithdrawStatus = withdrawStatus;
	}

	public String getCurrencyUnit() {
		return currencyUnit;
	}

	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}
}
