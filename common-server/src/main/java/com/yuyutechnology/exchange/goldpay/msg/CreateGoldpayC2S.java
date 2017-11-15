package com.yuyutechnology.exchange.goldpay.msg;

public class CreateGoldpayC2S {
	private String areaCode;
	private String mobile;
	private String userName;
	private boolean newUser;

	public CreateGoldpayC2S() {
		super();
	}

	public CreateGoldpayC2S(String areaCode, String mobile, String userName, boolean newUser) {
		super();
		this.areaCode = areaCode;
		this.mobile = mobile;
		this.userName = userName;
		this.newUser = newUser;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public boolean isNewUser() {
		return newUser;
	}

	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
