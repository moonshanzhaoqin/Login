package com.yuyutechnology.exchange.crm.request;

import org.apache.commons.lang.StringUtils;

public class ChangeCurrencyStatusRequest {
	private String currency;
	private int status;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 判断参数是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (StringUtils.isBlank(this.currency)) {
			return true;
		}
		return false;
	}
}
