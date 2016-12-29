package com.yuyutechnology.exchange.server.controller.dto;

import java.math.BigDecimal;
import java.util.Date;

public class ExchangeDTO {

	private String currencyOut;
	private String currencyIn;
	private String currencyOutUnit;
	private String currencyInUnit;
	private BigDecimal amountOut;
	private BigDecimal amountIn;
	private Date createTime;
	private Date finishTime;
	private int exchangeStatus;
	
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
	public Date getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	public int getExchangeStatus() {
		return exchangeStatus;
	}
	public void setExchangeStatus(int exchangeStatus) {
		this.exchangeStatus = exchangeStatus;
	}
}
