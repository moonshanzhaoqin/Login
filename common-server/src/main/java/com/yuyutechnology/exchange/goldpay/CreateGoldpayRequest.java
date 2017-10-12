package com.yuyutechnology.exchange.goldpay;

public class CreateGoldpayRequest {
	private String areaCode;
	private String userPhone;
	private boolean newUser;

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

	public CreateGoldpayRequest(String areaCode, String userPhone, boolean newUser) {
		super();
		this.areaCode = areaCode;
		this.userPhone = userPhone;
		this.newUser = newUser;
	}

}
