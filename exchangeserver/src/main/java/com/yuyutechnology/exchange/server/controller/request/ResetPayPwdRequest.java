package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class ResetPayPwdRequest {
	private String checkToken;
	private String newUserPayPwd;

	public String getCheckToken() {
		return checkToken;
	}

	public void setCheckToken(String checkToken) {
		this.checkToken = checkToken;
	}

	public String getNewUserPayPwd() {
		return newUserPayPwd;
	}

	public void setNewUserPayPwd(String newUserPayPwd) {
		this.newUserPayPwd = newUserPayPwd;
	}

	/**
	 * 判断参数是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (StringUtils.isEmpty(this.checkToken)) {
			return true;
		}
		if (StringUtils.isEmpty(this.newUserPayPwd)) {
			return true;
		}
		return false;
	}

}
