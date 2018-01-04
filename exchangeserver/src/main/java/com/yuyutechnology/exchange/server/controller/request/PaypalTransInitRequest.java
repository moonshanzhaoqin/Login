package com.yuyutechnology.exchange.server.controller.request;

import java.math.BigDecimal;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel
public class PaypalTransInitRequest extends BaseRequest{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8854191402507300487L;
	private String currency;
	private BigDecimal amount;

	@ApiModelProperty(required=true,value="货币类型")
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
