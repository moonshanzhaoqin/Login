package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class ChangeGoldpayRequest {
	private String checkToken;
	private String goldpayToken;

	public String getCheckToken() {
		return checkToken;
	}

	public void setCheckToken(String checkToken) {
		this.checkToken = checkToken;
	}

	public String getGoldpayToken() {
		return goldpayToken;
	}

	public void setGoldpayToken(String goldpayToken) {
		this.goldpayToken = goldpayToken;
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
		if (StringUtils.isEmpty(this.goldpayToken)) {
			return true;
		}
		return false;
	}
}
