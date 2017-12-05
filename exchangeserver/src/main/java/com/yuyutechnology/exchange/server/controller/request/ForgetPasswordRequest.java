package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class ForgetPasswordRequest {
	private String areaCode;
	private String userPhone;
	private String verificationCode;
	private String newPassword;

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

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * 判断参数是否为空
	 * 
	 * @return
	 */
	public boolean empty() {
		if (StringUtils.isBlank(this.areaCode)) {
			return true;
		}
		if (StringUtils.isBlank(this.userPhone)) {
			return true;
		}
		if (StringUtils.isBlank(this.newPassword)) {
			return true;
		}
		if (StringUtils.isBlank(this.verificationCode)) {
			return true;
		}
		return false;
	}
}
