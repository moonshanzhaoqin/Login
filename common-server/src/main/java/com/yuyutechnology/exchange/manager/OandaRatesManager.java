package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.yuyutechnology.exchange.util.oanda.OandaRespData;
import com.yuyutechnology.exchange.util.oanda.PriceInfo;

public interface OandaRatesManager {

	public void updateExchangeRates();

	public BigDecimal getExchangedAmount(String currencyLeft, 
			BigDecimal amount, String currencyRight);

	public BigDecimal getDefaultCurrencyAmount(String transCurrency, 
			BigDecimal transAmount);

	public Date getExchangeRateUpdateDate();

	public BigDecimal getTotalBalance(int userId);

	public HashMap<String, Double> getExchangeRate(String base);
	
	public HashMap<String, Double> getExchangeRateDiffLeft4OneRight(String currencyRight);
	
	public BigDecimal getSingleExchangeRate(String currencyLeft, String currencyRight);

	public List<PriceInfo> getAllPrices();

	public OandaRespData getCurrentPrices(String instruments);

}
