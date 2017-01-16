package com.yuyutechnology.exchange.crm.request;

import org.apache.commons.lang.StringUtils;

public class AddCurrencyRequest {
	private String currency;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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
