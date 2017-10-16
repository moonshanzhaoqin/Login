package com.yuyutechnology.exchange.goldpay.trans4merge;

public class ConfirmTransactionS2C {

	private Integer retCode;
	private TransactionDTO transInfo;
	
	public Integer getRetCode() {
		return retCode;
	}
	public void setRetCode(Integer retCode) {
		this.retCode = retCode;
	}
	public TransactionDTO getTransInfo() {
		return transInfo;
	}
	public void setTransInfo(TransactionDTO transInfo) {
		this.transInfo = transInfo;
	}
	
}
