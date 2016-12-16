package com.yuyutechnology.exchange.server.controller.response;

import java.util.HashMap;

public class GetExchangeRateResponse extends BaseResponse {
	
	private String base;
	private HashMap<String, Double> exchangeRates;
	
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public HashMap<String, Double> getExchangeRates() {
		return exchangeRates;
	}
	public void setExchangeRates(HashMap<String, Double> exchangeRates) {
		this.exchangeRates = exchangeRates;
	}

}
