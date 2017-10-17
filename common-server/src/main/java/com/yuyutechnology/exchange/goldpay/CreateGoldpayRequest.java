package com.yuyutechnology.exchange.goldpay;

public class CreateGoldpayRequest {
	private String areaCode;
	private String mobile;
	private boolean newUser;

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

	public CreateGoldpayRequest(String areaCode, String mobile, boolean newUser) {
		super();
		this.areaCode = areaCode;
		this.mobile = mobile;
		this.newUser = newUser;
	}

}
