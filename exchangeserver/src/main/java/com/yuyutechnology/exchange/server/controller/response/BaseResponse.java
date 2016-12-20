package com.yuyutechnology.exchange.server.controller.response;

import com.yuyutechnology.exchange.ServerConsts;

public class BaseResponse {
	//success, failure, session
	private String apiName;
	private String retStatus = "failure";
	private String retCode;
	private String message;

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
		if (ServerConsts.successCodeList.contains(retCode)) {
			this.retStatus = "success";
		}else if (ServerConsts.sessionCodeList.contains(retCode)) {
			this.retStatus = "session";
		}
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

	public String getRetStatus() {
		return retStatus;
	}

	public void setRetStatus(String retStatus) {
		this.retStatus = retStatus;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
}
