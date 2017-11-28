package com.yuyutechnology.exchange.server.controller.response;

public class WithdrawCalculateResponse extends BaseResponse {
	private long goldpay;
	private long fee;
	private boolean vip;

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

	public boolean isVip() {
		return vip;
	}

	public void setVip(boolean vip) {
		this.vip = vip;
	}


}
