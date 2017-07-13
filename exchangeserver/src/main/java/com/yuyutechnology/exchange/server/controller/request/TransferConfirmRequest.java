package com.yuyutechnology.exchange.server.controller.request;

public class TransferConfirmRequest {

	private String transferId;
	private String pinCode;

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
}
