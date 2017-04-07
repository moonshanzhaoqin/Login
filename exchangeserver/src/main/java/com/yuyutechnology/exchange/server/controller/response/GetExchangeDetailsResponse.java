package com.yuyutechnology.exchange.server.controller.response;

import java.math.BigDecimal;
import java.util.Date;

public class GetExchangeDetailsResponse extends BaseResponse {
	
	private String exchangeId;
	private String currencyOut;
	private String currencyIn;
	private String currencyOutUnit;
	private String currencyInUnit;
	private BigDecimal amountOut;
	private BigDecimal amountIn;
	private Date createTime;
	
	public String getExchangeId() {
		return exchangeId;
	}
	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}
	public String getCurrencyOut() {
		return currencyOut;
	}
	public void setCurrencyOut(String currencyOut) {
		this.currencyOut = currencyOut;
	}
	public String getCurrencyIn() {
		return currencyIn;
	}
	public void setCurrencyIn(String currencyIn) {
		this.currencyIn = currencyIn;
	}
	public String getCurrencyOutUnit() {
		return currencyOutUnit;
	}
	public void setCurrencyOutUnit(String currencyOutUnit) {
		this.currencyOutUnit = currencyOutUnit;
	}
	public String getCurrencyInUnit() {
		return currencyInUnit;
	}
	public void setCurrencyInUnit(String currencyInUnit) {
		this.currencyInUnit = currencyInUnit;
	}
	public BigDecimal getAmountOut() {
		return amountOut;
	}
	public void setAmountOut(BigDecimal amountOut) {
		this.amountOut = amountOut;
	}
	public BigDecimal getAmountIn() {
		return amountIn;
	}
	public void setAmountIn(BigDecimal amountIn) {
		this.amountIn = amountIn;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


}
