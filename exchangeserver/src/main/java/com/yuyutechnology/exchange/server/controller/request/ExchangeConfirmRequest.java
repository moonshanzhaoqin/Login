package com.yuyutechnology.exchange.server.controller.request;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel
public class ExchangeConfirmRequest {

	private String currencyOut;
	private String currencyIn;
	private double amountOut;
	private double amountIn;

	@ApiModelProperty(required=true,value="兑换货币类型")
	public String getCurrencyOut() {
		return currencyOut;
	}

	public void setCurrencyOut(String currencyOut) {
		this.currencyOut = currencyOut;
	}
	@ApiModelProperty(required=true,value="目标货币类型")
	public String getCurrencyIn() {
		return currencyIn;
	}

	public void setCurrencyIn(String currencyIn) {
		this.currencyIn = currencyIn;
	}
	@ApiModelProperty(required=true,value="兑换金额")
	public double getAmountOut() {
		return amountOut;
	}

	public void setAmountOut(double amountOut) {
		this.amountOut = amountOut;
	}
	@ApiModelProperty(required=true,value="目标金额")
	public double getAmountIn() {
		return amountIn;
	}

	public void setAmountIn(double amountIn) {
		this.amountIn = amountIn;
	}

}
