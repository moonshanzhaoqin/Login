package com.yuyutechnology.exchange.server.controller.response;

public class WithdrawCalculateResponse extends BaseResponse {
	private Double goldpay;
	private Double fee;
	public Double getGoldpay() {
		return goldpay;
	}
	public void setGoldpay(Double goldpay) {
		this.goldpay = goldpay;
	}
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}

}
