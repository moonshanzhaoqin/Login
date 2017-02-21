package com.yuyutechnology.exchange.utils.oanda;

import java.util.HashMap;

public class OandaRedisData {
	
	private String base;
	private String updateTime;
	private HashMap<String, CurrencyInfo> exchangeRate;
	
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public HashMap<String, CurrencyInfo> getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(HashMap<String, CurrencyInfo> exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

}
