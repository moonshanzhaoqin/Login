package com.yuyutechnology.exchange.server.controller.request;

public class TestCodeRequest {
	private String userPhone;
	private String VerificationCode;

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getVerificationCode() {
		return VerificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		VerificationCode = verificationCode;
	}

}
