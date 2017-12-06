package com.yuyutechnology.exchange.server.controller.response;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="retCode=00000,00001,02001,02002,02004,02005,02006,02007,02008,02009,")
public class ExchangeCalculationResponse extends BaseResponse {

	private Double amountOut;
	private Double amountIn;
	private Date rateUpdateTime;

	@ApiModelProperty(required=true,value="兑换金额")
	public Double getAmountOut() {
		return amountOut;
	}

	public void setAmountOut(Double amountOut) {
		this.amountOut = amountOut;
	}

	@ApiModelProperty(required=true,value="目标金额")
	public Double getAmountIn() {
		return amountIn;
	}

	public void setAmountIn(Double amountIn) {
		this.amountIn = amountIn;
	}

	@ApiModelProperty(required=true,value="汇率跟新时间")
	public Date getRateUpdateTime() {
		return rateUpdateTime;
	}

	public void setRateUpdateTime(Date rateUpdateTime) {
		this.rateUpdateTime = rateUpdateTime;
	}
}
