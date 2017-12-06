package com.yuyutechnology.exchange.server.controller.response;

import java.util.Date;
import java.util.HashMap;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiParam;
@ApiModel
public class GetExchangeRateResponse extends BaseResponse {

	private String base;
	private Date rateUpdateTime;
	private HashMap<String, Double> exchangeRates;

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}
	@ApiParam(name="",value="",required=true)
	public HashMap<String, Double> getExchangeRates() {
		return exchangeRates;
	}

	public void setExchangeRates(HashMap<String, Double> exchangeRates) {
		this.exchangeRates = exchangeRates;
	}
	@ApiParam(name="汇率更新时间",value="",required=true)
	public Date getRateUpdateTime() {
		return rateUpdateTime;
	}

	public void setRateUpdateTime(Date rateUpdateTime) {
		this.rateUpdateTime = rateUpdateTime;
	}
}
