package com.yuyutechnology.exchange.server.controller.request;

import java.math.BigDecimal;

public class TransInitRequest {
	
	private Integer payerId;
	private Integer payeeId;
	private String currency;
	private double amount;
	private String transferComment;
	
	private Boolean feeDeduction;
	private BigDecimal fee;
	private int feepayerId;
	
	private Boolean restricted;
	
	public Integer getPayerId() {
		return payerId;
	}
	public void setPayerId(Integer payerId) {
		this.payerId = payerId;
	}
	public Integer getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getTransferComment() {
		return transferComment;
	}
	public void setTransferComment(String transferComment) {
		this.transferComment = transferComment;
	}

	public Boolean getFeeDeduction() {
		return feeDeduction;
	}
	public void setFeeDeduction(Boolean feeDeduction) {
		this.feeDeduction = feeDeduction;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public int getFeepayerId() {
		return feepayerId;
	}
	public void setFeepayerId(int feepayerId) {
		this.feepayerId = feepayerId;
	}
	public Boolean getRestricted() {
		return restricted;
	}
	public void setRestricted(Boolean restricted) {
		this.restricted = restricted;
	}

	
}
