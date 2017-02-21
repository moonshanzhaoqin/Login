package com.yuyutechnology.exchange.utils.oanda;

public class EffectiveParams {
	
	private String data_set;
	private String date;
	private String decimal_places;
	private String[] fields;
	private String[] quote_currencies;
	
	public String getData_set() {
		return data_set;
	}
	public void setData_set(String data_set) {
		this.data_set = data_set;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDecimal_places() {
		return decimal_places;
	}
	public void setDecimal_places(String decimal_places) {
		this.decimal_places = decimal_places;
	}
	public String[] getFields() {
		return fields;
	}
	public void setFields(String[] fields) {
		this.fields = fields;
	}
	public String[] getQuote_currencies() {
		return quote_currencies;
	}
	public void setQuote_currencies(String[] quote_currencies) {
		this.quote_currencies = quote_currencies;
	}
	
}
