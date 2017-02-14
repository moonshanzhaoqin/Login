package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class ModifyPayPwdByGoldpayRequest {
	private String goldpayToken;
	private String newUserPayPwd;

	public String getGoldpayToken() {
		return goldpayToken;
	}

	public void setGoldpayToken(String goldpayToken) {
		this.goldpayToken = goldpayToken;
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
		if (StringUtils.isEmpty(this.goldpayToken)) {
			return true;
		}
		if (StringUtils.isEmpty(this.newUserPayPwd)) {
			return true;
		}
		return false;
	}

}
