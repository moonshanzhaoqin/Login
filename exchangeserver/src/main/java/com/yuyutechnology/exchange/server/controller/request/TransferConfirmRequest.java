package com.yuyutechnology.exchange.server.controller.request;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class TransferConfirmRequest {

	private String transferId;
	private String pinCode;

	@ApiModelProperty(required=true,value="订单Id")
	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	@ApiModelProperty(required=true,value="验证码")
	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
}
