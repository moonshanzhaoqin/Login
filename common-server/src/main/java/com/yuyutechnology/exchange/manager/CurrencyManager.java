package com.yuyutechnology.exchange.manager;

import java.util.List;

import com.yuyutechnology.exchange.pojo.Currency;

public interface CurrencyManager {

	int enableCurrency(String currency);

	int disableCurrency(String currencyId);

	List<Currency> getCurrencyList();

	void updateCurrency(String currency, String nameCn, String nameEn, String nameHk, String currencyUnit,
			String Status, String currencyOrder);

}
