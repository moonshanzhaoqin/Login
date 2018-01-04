package com.yuyutechnology.exchange.server.controller.request;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class TransferConfirmRequest extends BaseRequest{

	/**
	 * 
	 */
	private static final long serialVersionUID = -815568183577252349L;
	private String transferId;
	private String userPayPwd;
	private String pinCode;
	private String addFriend;
	
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
	
	public String getAddFriend() {
		return addFriend;
	}

	public void setAddFriend(String addFriend) {
		this.addFriend = addFriend;
	}
}
