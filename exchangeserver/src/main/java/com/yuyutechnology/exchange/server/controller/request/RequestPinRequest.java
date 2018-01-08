package com.yuyutechnology.exchange.server.controller.request;

public class RequestPinRequest extends BaseRequest{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3858199335670465069L;
	private String transferId;

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
}
