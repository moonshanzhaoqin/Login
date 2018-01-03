package com.yuyutechnology.exchange.server.controller.request;

public class LogoutRequest extends BaseRequest{

	private int userId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
