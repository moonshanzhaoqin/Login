package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class RegisterRequest {
	private String areaCode;
	private String userPhone;
	private String registrationCode;
	private String userName;
	private String userPassword;
	private String language;
	private String pushId;
	
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}


	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getRegistrationCode() {
		return registrationCode;
	}

	public void setRegistrationCode(String registrationCode) {
		this.registrationCode = registrationCode;
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

	/**
	 * 判断参数是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (StringUtils.isEmpty(this.areaCode)) {
			return true;
		}
		if (StringUtils.isEmpty(this.userPhone)) {
			return true;
		}
		if (StringUtils.isEmpty(this.userName)) {
			return true;
		}
		if (StringUtils.isEmpty(this.registrationCode)) {
			return true;
		}
		if (StringUtils.isEmpty(this.userPassword)) {
			return true;
		}
		if (StringUtils.isEmpty(this.language)) {
			this.language = "zh_CN";
		}
		return false;
	}

}
