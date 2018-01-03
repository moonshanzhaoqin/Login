package com.yuyutechnology.exchange.server.controller.request;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel
public class TransferInitiateRequest extends BaseRequest{

	private String areaCode;
	private String userPhone;
	private String currency;
	private double amount;
	private String transferComment;

	@ApiModelProperty(required=true,value="手机区号")
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	@ApiModelProperty(required=true,value="手机号码")
	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	@ApiModelProperty(required=true,value="货币类型")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	@ApiModelProperty(required=true,value="金额")
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	@ApiModelProperty(required=true,value="备注")
	public String getTransferComment() {
		return transferComment;
	}

	public void setTransferComment(String transferComment) {
		this.transferComment = transferComment;
	}


}
