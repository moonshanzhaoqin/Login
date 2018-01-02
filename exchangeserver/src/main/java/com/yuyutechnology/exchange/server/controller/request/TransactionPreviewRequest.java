package com.yuyutechnology.exchange.server.controller.request;

public class TransactionPreviewRequest {
	
	private String areaCode;
	private String userPhone;
	private String currency;
	private Double transAmount;
	
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(Double transAmount) {
		this.transAmount = transAmount;
	}


}
