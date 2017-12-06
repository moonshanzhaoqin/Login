package com.yuyutechnology.exchange.server.controller.request;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel()
public class MakeRequestRequest {

	private String areaCode;
	private String phone;
	private String currency;
	private double amount;

	@ApiModelProperty(required=true,value="手机区号")
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	@ApiModelProperty(required=true,value="手机号码")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	@ApiModelProperty(required=true,value="货币种类")
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

}
