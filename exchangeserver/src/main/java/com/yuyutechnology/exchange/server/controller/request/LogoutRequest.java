package com.yuyutechnology.exchange.server.controller.request;

public class LogoutRequest extends BaseRequest{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4145934379650114899L;
	private int userId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
