package com.yuyutechnology.exchange.crm.request;

public class ChangeCurrencyStatusRequest {
private String currency;
	private int status;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
