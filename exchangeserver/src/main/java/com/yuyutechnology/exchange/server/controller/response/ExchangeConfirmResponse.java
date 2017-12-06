package com.yuyutechnology.exchange.server.controller.response;

import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(value="retCode=02001,02002,02003,02006,02007,02008,02009")
public class ExchangeConfirmResponse extends BaseResponse {

	private Double amountOut;
	private Double amountIn;

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

}
