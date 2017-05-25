package com.yuyutechnology.exchange.server.controller.response;

public class PaypalTransInitResponse extends BaseResponse {
	
	private String transId;
	private String accessToken;
	private Double amount;
	
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
}
