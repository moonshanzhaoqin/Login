package com.yuyutechnology.exchange.server.controller.response;

public class GoldpayWithdrawResponse extends BaseResponse {

	private String transferId;
	private Double fee;

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

}
