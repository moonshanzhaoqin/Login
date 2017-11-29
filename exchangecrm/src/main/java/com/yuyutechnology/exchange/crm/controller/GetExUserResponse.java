package com.yuyutechnology.exchange.crm.controller;

import com.yuyutechnology.exchange.crm.reponse.BaseResponse;

public class GetExUserResponse extends BaseResponse {

	private String areaCode;
	private String userPhone;
	private String userName;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
