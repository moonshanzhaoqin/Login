package com.yuyutechnology.exchange.manager;

public interface ExchangeRateManager {
	
	public void updateExchangeRateNoGoldq();
	
	public void updateGoldpayExchangeRate();

	public double getExchangeRate(String base,String outCurrency);

}
