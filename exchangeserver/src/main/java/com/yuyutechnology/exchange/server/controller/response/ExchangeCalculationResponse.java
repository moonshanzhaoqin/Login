package com.yuyutechnology.exchange.server.controller.response;

import java.util.Date;

public class ExchangeCalculationResponse extends BaseResponse {

	private Double amountOut;
	private Double amountIn;
	private Date rateUpdateTime;

	public Double getAmountOut() {
		return amountOut;
	}

	public void setAmountOut(Double amountOut) {
		this.amountOut = amountOut;
	}

	public Double getAmountIn() {
		return amountIn;
	}

	public void setAmountIn(Double amountIn) {
		this.amountIn = amountIn;
	}

	public Date getRateUpdateTime() {
		return rateUpdateTime;
	}

	public void setRateUpdateTime(Date rateUpdateTime) {
		this.rateUpdateTime = rateUpdateTime;
	}
}
