package com.yuyutechnology.exchange.server.controller.response;

public class BaseResponse {
	private String retCode;
	private String message;
	private String apiName;

	public BaseResponse() {
		super();
		String name = this.getClass().getSimpleName();
		name = name.substring(0, name.indexOf("Response"));
		name = name.substring(0, 1).toLowerCase() + name.substring(1);
		apiName = name;
	}

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

	public String getApiName() {
		return apiName;
	}
}
