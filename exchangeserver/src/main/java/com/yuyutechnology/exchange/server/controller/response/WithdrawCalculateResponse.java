package com.yuyutechnology.exchange.server.controller.response;

public class WithdrawCalculateResponse extends BaseResponse {
	private long goldpay;
	private long fee;
	public long getGoldpay() {
		return goldpay;
	}
	public void setGoldpay(long goldpay) {
		this.goldpay = goldpay;
	}
	public long getFee() {
		return fee;
	}
	public void setFee(long fee) {
		this.fee = fee;
	}

}
