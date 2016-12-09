package com.yuyutechnology.exchange.server.controller.response;

public class ExchangeCalculationResponse extends BaseResponse{
	
	private double convertedAmount;

	public double getConvertedAmount() {
		return convertedAmount;
	}

	public void setConvertedAmount(double convertedAmount) {
		this.convertedAmount = convertedAmount;
	}

}
