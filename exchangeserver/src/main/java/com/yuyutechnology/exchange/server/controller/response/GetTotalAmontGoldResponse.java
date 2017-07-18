package com.yuyutechnology.exchange.server.controller.response;

public class GetTotalAmontGoldResponse extends BaseResponse {

	private Double amountOfGold;

	private Double amountOfGoldOz;

	public Double getAmountOfGold() {
		return amountOfGold;
	}

	public void setAmountOfGold(Double amountOfGold) {
		this.amountOfGold = amountOfGold;
	}

	public Double getAmountOfGoldOz() {
		return amountOfGoldOz;
	}

	public void setAmountOfGoldOz(Double amountOfGoldOz) {
		this.amountOfGoldOz = amountOfGoldOz;
	}
}
