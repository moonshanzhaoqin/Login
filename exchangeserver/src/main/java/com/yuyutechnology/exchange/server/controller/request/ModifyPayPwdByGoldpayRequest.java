package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class ModifyPayPwdByGoldpayRequest {
	private String goldpayPwd;
	private String newUserPayPwd;

	public String getGoldpayPwd() {
		return goldpayPwd;
	}

	public void setGoldpayPwd(String goldpayPwd) {
		this.goldpayPwd = goldpayPwd;
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
		if (StringUtils.isEmpty(this.goldpayPwd)) {
			return true;
		}
		if (StringUtils.isEmpty(this.newUserPayPwd)) {
			return true;
		}
		return false;
	}
}
