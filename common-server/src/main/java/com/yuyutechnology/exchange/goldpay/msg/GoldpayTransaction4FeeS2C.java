package com.yuyutechnology.exchange.goldpay.msg;

public class GoldpayTransaction4FeeS2C {
	
	private int retCode;
	private TransactionDTO transInfo;
	private TransactionDTO feeTransInfo;
	
	public int getRetCode() {
		return retCode;
	}
	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}
	public TransactionDTO getTransInfo()
	{
		return transInfo;
	}
	public void setTransInfo(TransactionDTO transInfo)
	{
		this.transInfo = transInfo;
	}
	public TransactionDTO getFeeTransInfo() {
		return feeTransInfo;
	}
	public void setFeeTransInfo(TransactionDTO feeTransInfo) {
		this.feeTransInfo = feeTransInfo;
	}

}
