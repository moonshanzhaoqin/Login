package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;

public interface OandaRatesManager {

	public void updateExchangeRates();

	public BigDecimal getExchangedAmount(String currencyIn, 
			BigDecimal amountIn, String currencyOut, String type);

	public BigDecimal getDefaultCurrencyAmount(String transCurrency, 
			BigDecimal transAmount, String type);

}
