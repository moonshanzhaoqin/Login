package com.yuyutechnology.exchange.crm.reponse;

public class BaseResponse {
	private String retCode;
	private String message;

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
