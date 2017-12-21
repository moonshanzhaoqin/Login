package com.yuyutechnology.exchange.server.controller.response;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.yuyutechnology.exchange.dto.UserInfo4Transfer;

public class GetUserInfo4TransferResponse extends BaseResponse {
	@ApiModelProperty(value = "用户信息")
	private UserInfo4Transfer userInfo;

	public UserInfo4Transfer getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo4Transfer userInfo) {
		this.userInfo = userInfo;
	}

}
