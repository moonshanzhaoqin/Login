package com.yuyutechnology.exchange.server.controller.response;

public class CheckGoldpayResponse extends BaseResponse {
	private String checkToken;

	public String getCheckToken() {
		return checkToken;
	}

	public void setCheckToken(String checkToken) {
		this.checkToken = checkToken;
	}
}
