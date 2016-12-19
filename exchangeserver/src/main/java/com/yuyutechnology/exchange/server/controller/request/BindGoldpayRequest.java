package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class BindGoldpayRequest {
	private String goldpayToken;

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
		if (StringUtils.isEmpty(this.goldpayToken)) {
			return true;
		}
		return false;
	}

}
