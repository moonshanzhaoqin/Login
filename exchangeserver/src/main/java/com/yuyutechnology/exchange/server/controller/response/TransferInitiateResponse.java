package com.yuyutechnology.exchange.server.controller.response;

public class TransferInitiateResponse extends BaseResponse {
	
	private String transferId;

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

}
