package com.yuyutechnology.exchange.server.controller.dto;

import java.math.BigDecimal;
import java.util.Date;

public class TransferDTO {
	
	private String currency;
	private String currencyUnit;
	private BigDecimal amount;
	private String phoneNum;
	private String comments;
	private Date finishAt;
	private int transferType;

	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Date getFinishAt() {
		return finishAt;
	}
	public void setFinishAt(Date finishAt) {
		this.finishAt = finishAt;
	}
	public int getTransferType() {
		return transferType;
	}
	public void setTransferType(int transferType) {
		this.transferType = transferType;
	}
	public String getCurrencyUnit() {
		return currencyUnit;
	}
	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}
}
