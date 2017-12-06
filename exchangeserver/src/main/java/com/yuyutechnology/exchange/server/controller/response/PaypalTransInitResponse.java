package com.yuyutechnology.exchange.server.controller.response;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="retCode=00000,02006,03027,03028,03030")
public class PaypalTransInitResponse extends BaseResponse {

	private String transId;
	private String accessToken;
	private String currency;
	private Double amount;
	private Double fee;
	private String unit;

	private Date createAt;
	private long expiration;

	@ApiModelProperty(required=true,value="订单Id")
	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	@ApiModelProperty(required=true,value="链接token")
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@ApiModelProperty(required=true,value="货币类型")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@ApiModelProperty(required=true,value="金额")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@ApiModelProperty(required=true,value="手续费")
	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	@ApiModelProperty(required=true,value="货币单位")
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@ApiModelProperty(required=true,value="创建时间")
	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	@ApiModelProperty(required=true,value="PayPal支付过期时间(秒)")
	public long getExpiration() {
		return expiration;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

}
