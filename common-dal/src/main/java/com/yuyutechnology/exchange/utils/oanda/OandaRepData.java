package com.yuyutechnology.exchange.utils.oanda;

import java.util.Map;

public class OandaRepData {

	private String base_currency;
	private Meta meta;
	private Map<String,CurrencyInfo> quotes;
	
	public String getBase_currency() {
		return base_currency;
	}
	public void setBase_currency(String base_currency) {
		this.base_currency = base_currency;
	}
	public Meta getMeta() {
		return meta;
	}
	public void setMeta(Meta meta) {
		this.meta = meta;
	}
	public Map<String, CurrencyInfo> getQuotes() {
		return quotes;
	}
	public void setQuotes(Map<String, CurrencyInfo> quotes) {
		this.quotes = quotes;
	}


}
