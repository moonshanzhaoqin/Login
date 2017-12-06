package com.yuyutechnology.exchange.server.controller.request;

import java.math.BigDecimal;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel
public class RegenerateQRCodeRequest {

	private String currency;
	private BigDecimal amount;

	@ApiModelProperty(required=true,value="货币种类")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@ApiModelProperty(required=true,value="金额")
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
