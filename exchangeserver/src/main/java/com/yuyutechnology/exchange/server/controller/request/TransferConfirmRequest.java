package com.yuyutechnology.exchange.server.controller.request;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class TransferConfirmRequest {

	private String transferId;
	private String userPayPwd;
	private String pinCode;
	@ApiModelProperty(value="是否需要添加好友",notes="")
	private String addFriends;
	
	@ApiModelProperty(required=true,value="订单Id")
	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
	@ApiModelProperty(required=true,value="交易密码")
	public String getUserPayPwd() {
		return userPayPwd;
	}

	public void setUserPayPwd(String userPayPwd) {
		this.userPayPwd = userPayPwd;
	}

	@ApiModelProperty(required=true,value="验证码")
	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	
	public String getAddFriends() {
		return addFriends;
	}

	public void setAddFriends(String addFriends) {
		this.addFriends = addFriends;
	}
}
