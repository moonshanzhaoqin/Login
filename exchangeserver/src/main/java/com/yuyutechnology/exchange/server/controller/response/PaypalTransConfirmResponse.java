package com.yuyutechnology.exchange.server.controller.response;

public class PaypalTransConfirmResponse extends BaseResponse {
	
	private String transId;

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

}
