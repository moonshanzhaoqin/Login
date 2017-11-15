package com.yuyutechnology.exchange.util.exchangerate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class GoldpayExchangeRate {
	
	private Date date;
	private Map<String,BigDecimal> gdp4Others; //gdp4Others 表示 1gdp兑换多少其他币种
	private Map<String,BigDecimal> others4Gdp;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Map<String, BigDecimal> getGdp4Others() {
		return gdp4Others;
	}
	public void setGdp4Others(Map<String, BigDecimal> gdp4Others) {
		this.gdp4Others = gdp4Others;
	}
	public Map<String, BigDecimal> getOthers4Gdp() {
		return others4Gdp;
	}
	public void setOthers4Gdp(Map<String, BigDecimal> others4Gdp) {
		this.others4Gdp = others4Gdp;
	}
}
