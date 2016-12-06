package com.yuyutechnology.exchange.manager;

public interface ExchangeRateManager {
	
	public void updateExchangeRate();

	public double getExchangeRate(String base,String outCurrency);
	
}
