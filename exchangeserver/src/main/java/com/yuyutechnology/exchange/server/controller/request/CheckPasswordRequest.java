package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class CheckPasswordRequest {
	private String userPassword;

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	/**
	 * 判断参数是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (StringUtils.isEmpty(this.userPassword)) {
			return true;
		}
		return false;
	}
}
