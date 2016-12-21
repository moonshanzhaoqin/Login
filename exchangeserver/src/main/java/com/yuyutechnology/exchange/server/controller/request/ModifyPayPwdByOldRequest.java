package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class ModifyPayPwdByOldRequest {
	private String oldUserPayPwd;
	private String newUserPayPwd;

	public String getOldUserPayPwd() {
		return oldUserPayPwd;
	}

	public void setOldUserPayPwd(String oldUserPayPwd) {
		this.oldUserPayPwd = oldUserPayPwd;
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
		if (StringUtils.isEmpty(this.oldUserPayPwd)) {
			return true;
		}
		if (StringUtils.isEmpty(this.newUserPayPwd)) {
			return true;
		}
		return false;
	}
}
