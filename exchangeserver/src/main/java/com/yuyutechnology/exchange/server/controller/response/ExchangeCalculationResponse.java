package com.yuyutechnology.exchange.server.controller.response;

public class ExchangeCalculationResponse extends BaseResponse{

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
