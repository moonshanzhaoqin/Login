package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class LoginRequest {
	private String areaCode;
	private String userPhone;
	private String userPassword;
	private String loginToken;
	private String language;
	private String pushId;

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

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
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
	 * 以及走那个流程
	 * 
	 * @return
	 */
	public int isEmpty() {
		if (StringUtils.isEmpty(this.language)) {
			this.language = "zh_CN";
		}
		if (StringUtils.isNotBlank(this.loginToken)) {
			return 1;
		}
		if (StringUtils.isBlank(this.userPassword)) {
			return 0;
		}
		if (StringUtils.isBlank(this.areaCode)) {
			return 0;
		}
		if (StringUtils.isBlank(this.userPhone)) {
			return 0;
		}
		return 2;
	}
}
