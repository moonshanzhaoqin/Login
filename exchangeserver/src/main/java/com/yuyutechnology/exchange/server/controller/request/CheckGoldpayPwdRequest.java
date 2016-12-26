package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class CheckGoldpayPwdRequest {

	private String goldpayPwd;

	public String getGoldpayPwd() {
		return goldpayPwd;
	}

	public void setGoldpayPwd(String goldpayPwd) {
		this.goldpayPwd = goldpayPwd;
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
		return false;
	}
}
