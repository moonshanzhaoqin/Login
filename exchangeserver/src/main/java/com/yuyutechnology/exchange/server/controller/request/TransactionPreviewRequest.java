package com.yuyutechnology.exchange.server.controller.request;

public class TransactionPreviewRequest extends BaseRequest{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4792317219259723672L;
	private String areaCode;
	private String userPhone;
	private String currency;
	private double transAmount;
	
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
	public double getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(double transAmount) {
		this.transAmount = transAmount;
	}


}
