package com.yuyutechnology.exchange.goldpay.msg;

public class GoldpayTransaction4FeeC2S {
	private String payOrderId;
	private String toAccountNum;
	private String fromAccountNum;
	private long balance;
	
	private String feePayOrderId;
	private String feeToAccountNum;
	private String feeFromAccountNum;
	private long feeBalance;
	
	private String comment;
	
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getPayOrderId() {
		return payOrderId;
	}
	public void setPayOrderId(String payOrderId) {
		this.payOrderId = payOrderId;
	}
	public String getToAccountNum() {
		return toAccountNum;
	}
	public void setToAccountNum(String toAccountNum) {
		this.toAccountNum = toAccountNum;
	}
	public String getFromAccountNum() {
		return fromAccountNum;
	}
	public void setFromAccountNum(String fromAccountNum) {
		this.fromAccountNum = fromAccountNum;
	}
	public String getFeePayOrderId() {
		return feePayOrderId;
	}
	public void setFeePayOrderId(String feePayOrderId) {
		this.feePayOrderId = feePayOrderId;
	}
	public String getFeeToAccountNum() {
		return feeToAccountNum;
	}
	public void setFeeToAccountNum(String feeToAccountNum) {
		this.feeToAccountNum = feeToAccountNum;
	}
	public String getFeeFromAccountNum() {
		return feeFromAccountNum;
	}
	public void setFeeFromAccountNum(String feeFromAccountNum) {
		this.feeFromAccountNum = feeFromAccountNum;
	}
	public long getFeeBalance() {
		return feeBalance;
	}
	public void setFeeBalance(long feeBalance) {
		this.feeBalance = feeBalance;
	}
}
