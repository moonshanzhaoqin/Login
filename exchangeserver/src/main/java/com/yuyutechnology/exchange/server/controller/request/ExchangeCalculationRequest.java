package com.yuyutechnology.exchange.server.controller.request;

public class ExchangeCalculationRequest {
	
	private String currencyOut;
	private String currencyIn;
	private Long amountOut;
	
	public String getCurrencyOut() {
		return currencyOut;
	}
	public void setCurrencyOut(String currencyOut) {
		this.currencyOut = currencyOut;
	}
	public String getCurrencyIn() {
		return currencyIn;
	}
	public void setCurrencyIn(String currencyIn) {
		this.currencyIn = currencyIn;
	}
	public Long getAmountOut() {
		return amountOut;
	}
	public void setAmountOut(Long amountOut) {
		this.amountOut = amountOut;
	}

}
