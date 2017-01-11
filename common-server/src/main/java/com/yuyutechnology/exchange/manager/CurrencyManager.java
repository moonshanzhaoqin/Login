package com.yuyutechnology.exchange.manager;

import java.util.List;

import com.yuyutechnology.exchange.pojo.Currency;

public interface CurrencyManager {

	List<Currency> getCurrencyList();

	void updateCurrency(Currency currency);

	String addCurrency(String currency);

	String changeCurrencyStatus(String currencyId, int status);

}
