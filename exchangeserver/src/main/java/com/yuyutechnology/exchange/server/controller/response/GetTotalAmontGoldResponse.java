package com.yuyutechnology.exchange.server.controller.response;

public class GetTotalAmontGoldResponse extends BaseResponse {
	
	private double amountOfGold;
	
	private double amountOfGoldOz;

	public double getAmountOfGold() {
		return amountOfGold;
	}

	public void setAmountOfGold(double amountOfGold) {
		this.amountOfGold = amountOfGold;
	}

	public double getAmountOfGoldOz() {
		return amountOfGoldOz;
	}

	public void setAmountOfGoldOz(double amountOfGoldOz) {
		this.amountOfGoldOz = amountOfGoldOz;
	}
}
