package com.yuyutechnology.exchange.server.controller.response;

import java.util.Date;
import java.util.HashMap;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
@ApiModel(description="retCode=00000,00001")
public class GetExchangeRateResponse extends BaseResponse {

	private String base;
	private Date rateUpdateTime;
	private HashMap<String, Double> exchangeRates;

	@ApiModelProperty(required=true,value="货币类型")
	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}
	@ApiModelProperty(required=true,value="汇率更新时间")
	public HashMap<String, Double> getExchangeRates() {
		return exchangeRates;
	}

	public void setExchangeRates(HashMap<String, Double> exchangeRates) {
		this.exchangeRates = exchangeRates;
	}
	@ApiModelProperty(required=true,value="汇率列表")
	public Date getRateUpdateTime() {
		return rateUpdateTime;
	}

	public void setRateUpdateTime(Date rateUpdateTime) {
		this.rateUpdateTime = rateUpdateTime;
	}
}
