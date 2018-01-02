package com.yuyutechnology.exchange.server.controller.request;

public class TransactionPreviewRequest {
	
	private String areaCode;
	private String userPhone;
	private String currency;
	private String transAmount;
//	private String transferComment;
	
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
	public String getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}

//	public String getTransferComment() {
//		return transferComment;
//	}
//	public void setTransferComment(String transferComment) {
//		this.transferComment = transferComment;
//	}

}
