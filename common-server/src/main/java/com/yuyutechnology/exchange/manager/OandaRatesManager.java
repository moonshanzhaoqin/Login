package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

public interface OandaRatesManager {

	public void updateExchangeRates();

	public BigDecimal getExchangedAmount(String currencyLeft, 
			BigDecimal amount, String currencyRight, String type);

	public BigDecimal getDefaultCurrencyAmount(String transCurrency, 
			BigDecimal transAmount, String type);

	public Date getExchangeRateUpdateDate();

	public BigDecimal getTotalBalance(int userId);

	public HashMap<String, Double> getExchangeRate(String base,String type);
	
	public BigDecimal getSingleExchangeRate(String currencyLeft, String currencyRight);
	
	

}
