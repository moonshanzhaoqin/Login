package com.yuyutechnology.exchange.dto;

import java.math.BigDecimal;
import java.util.Date;

public class TransDetailsDTO {
	
	private Integer userId;
	private String transId;
	private String transCurrency;
	private BigDecimal transAmount;
	private BigDecimal transFee;
	private String transUnit;
	private String transRemarks;
	private Integer transType;
	private Date createTime;
	private Date finishTime;
	private String traderName;
	private String traderAreaCode;
	private String traderPhone;
	private String goldpayName;
	private String paypalCurrency;
	private BigDecimal paypalExchange;
	
	private boolean isFriend;
	private boolean isRegistered;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getTransCurrency() {
		return transCurrency;
	}
	public void setTransCurrency(String transCurrency) {
		this.transCurrency = transCurrency;
	}
	public BigDecimal getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}
	public BigDecimal getTransFee() {
		return transFee;
	}
	public void setTransFee(BigDecimal transFee) {
		this.transFee = transFee;
	}
	public String getTransUnit() {
		return transUnit;
	}
	public void setTransUnit(String transUnit) {
		this.transUnit = transUnit;
	}
	public String getTransRemarks() {
		return transRemarks;
	}
	public void setTransRemarks(String transRemarks) {
		this.transRemarks = transRemarks;
	}
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
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
	public String getTraderName() {
		return traderName;
	}
	public void setTraderName(String traderName) {
		this.traderName = traderName;
	}
	public String getTraderAreaCode() {
		return traderAreaCode;
	}
	public void setTraderAreaCode(String traderAreaCode) {
		this.traderAreaCode = traderAreaCode;
	}
	public String getTraderPhone() {
		return traderPhone;
	}
	public void setTraderPhone(String traderPhone) {
		this.traderPhone = traderPhone;
	}
	public String getGoldpayName() {
		return goldpayName;
	}
	public void setGoldpayName(String goldpayName) {
		this.goldpayName = goldpayName;
	}
	public String getPaypalCurrency() {
		return paypalCurrency;
	}
	public void setPaypalCurrency(String paypalCurrency) {
		this.paypalCurrency = paypalCurrency;
	}
	public BigDecimal getPaypalExchange() {
		return paypalExchange;
	}
	public void setPaypalExchange(BigDecimal paypalExchange) {
		this.paypalExchange = paypalExchange;
	}
	public boolean isFriend() {
		return isFriend;
	}
	public void setFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}
	public boolean isRegistered() {
		return isRegistered;
	}
	public void setRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

}
