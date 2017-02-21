package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;

public interface OandaExchangeRateManager {
	
	public void updateExchangeRate(boolean refresh);

	public double getExchangeRate(String base,String outCurrency);
	
	public BigDecimal getExchangeResult(String transCurrency,BigDecimal transAmount);

}
