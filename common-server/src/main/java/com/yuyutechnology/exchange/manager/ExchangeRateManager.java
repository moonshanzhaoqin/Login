package com.yuyutechnology.exchange.manager;

public interface ExchangeRateManager {
	
	public void updateExchangeRateNoGoldq();

	public double getExchangeRate(String base,String outCurrency);

	public void updateGoldpayExchangeRate();
	
}
