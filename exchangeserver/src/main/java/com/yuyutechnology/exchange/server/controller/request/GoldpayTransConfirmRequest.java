package com.yuyutechnology.exchange.server.controller.request;

public class GoldpayTransConfirmRequest {

	private String pin;
	private String transferId;

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
}
