package com.yuyutechnology.exchange.server.controller.request;

public class GetExchangeRateRequest extends BaseRequest{

	private String base;

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

}
