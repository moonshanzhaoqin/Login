package com.yuyutechnology.exchange.server.controller.request;

public class GetExchangeDetailsRequest extends BaseRequest{

	private String exchangeId;

	public String getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}

}
