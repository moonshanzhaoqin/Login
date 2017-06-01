package com.yuyutechnology.exchange.dto;

import java.util.Date;

public class TransferDTO {

	private String transferId;
	private String currency;
	private Double amount;
	private String currencyUnit;
	private String comments;
	private int transferType;
	private Date finishAt;
	
	private String phoneNum;
	private String trader;
	
	public String getTransferId() {
		return transferId;
	}
	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getCurrencyUnit() {
		return currencyUnit;
	}
	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public int getTransferType() {
		return transferType;
	}
	public void setTransferType(int transferType) {
		this.transferType = transferType;
	}
	public Date getFinishAt() {
		return finishAt;
	}
	public void setFinishAt(Date finishAt) {
		this.finishAt = finishAt;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getTrader() {
		return trader;
	}
	public void setTrader(String trader) {
		this.trader = trader;
	}
	
	
}
