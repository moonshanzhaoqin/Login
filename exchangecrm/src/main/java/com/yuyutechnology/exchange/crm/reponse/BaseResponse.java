package com.yuyutechnology.exchange.crm.reponse;

public class BaseResponse {
	private int retCode;
	private String message;

	public int getRetCode() {
		return retCode;
	}

	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
