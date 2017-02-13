package com.yuyutechnology.exchange.server.controller.response;

import com.yuyutechnology.exchange.dto.UserInfo;

public class ChangeGoldpayResponse  extends BaseResponse{
	private UserInfo user;

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}
}
