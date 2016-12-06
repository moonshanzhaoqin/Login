package com.yuyutechnology.exchange.server.controller.request;

public class ExchangeConfirmRequest {
	
	private String currencyOut;
	private String currencyIn;
	private double amountOut;
	private double amountIn;
	
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
	public double getAmountOut() {
		return amountOut;
	}
	public void setAmountOut(double amountOut) {
		this.amountOut = amountOut;
	}
	public double getAmountIn() {
		return amountIn;
	}
	public void setAmountIn(double amountIn) {
		this.amountIn = amountIn;
	}

}
