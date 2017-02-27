package com.yuyutechnology.exchange.goldpay.transaction;


public class CalculateChargeReturnModel {
	
	private Integer resultCode;
	private String resultMessage;
	private Object resultData;
	private long chargeAmount;
	private int chargeType;//1=发款方, 2=收款方
	public long getChargeAmount() {
		return chargeAmount;
	}
	public void setChargeAmount(long chargeAmount) {
		this.chargeAmount = chargeAmount;
	}
	public int getChargeType() {
		return chargeType;
	}
	public void setChargeType(int chargeType) {
		this.chargeType = chargeType;
	}
	public Integer getResultCode() {
		return resultCode;
	}
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	public Object getResultData() {
		return resultData;
	}
	public void setResultData(Object resultData) {
		this.resultData = resultData;
	}
	
}
