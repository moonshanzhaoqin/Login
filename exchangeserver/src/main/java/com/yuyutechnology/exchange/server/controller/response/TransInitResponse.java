package com.yuyutechnology.exchange.server.controller.response;

public class TransInitResponse extends BaseResponse {
	private String transferId;
	private String transferId4Fee;

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public String getTransferId4Fee() {
		return transferId4Fee;
	}

	public void setTransferId4Fee(String transferId4Fee) {
		this.transferId4Fee = transferId4Fee;
	}
}
