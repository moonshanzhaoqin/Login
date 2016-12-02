package com.yuyutechnology.exchange.dao;

import com.yuyutechnology.exchange.pojo.Currency;

import java.util.List;

public interface CurrencyDAO {
	
	public Currency getCurrency(String currency);
	
	public List<Currency> getCurrencys();

}
