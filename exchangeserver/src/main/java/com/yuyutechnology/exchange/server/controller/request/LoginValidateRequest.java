package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class LoginValidateRequest extends BaseRequest{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5082970225899609990L;
	private String areaCode;
	private String userPhone;
	private String userPassword;
	private String deviceName;
	private String deviceId;
	private String language;
	private String pushId;
	private String verificationCode;

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

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getPushId() {
		return pushId;
	}

	public void setPushId(String pushId) {
		this.pushId = pushId;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public boolean empty() {
		if (StringUtils.isBlank(this.verificationCode)) {
			return true;
		}
		if (StringUtils.isBlank(this.userPassword)) {
			return true;
		}
		if (StringUtils.isBlank(this.areaCode)) {
			return true;
		}
		if (StringUtils.isBlank(this.userPhone)) {
			return true;
		}
		if (StringUtils.isBlank(this.deviceId)) {
			return true;
		}
		if (StringUtils.isBlank(this.deviceName)) {
			return true;
		}
		return false;
	}

}
