package com.yuyutechnology.exchange.goldpay.trans4merge;

public class CreateTransactionC2S {
	
	private String signature;
	private String timestamp;
	private String nonce;
	private String toAccountnum;
	private String fromAccountnum;
	private Integer balance;
	private String comment;
	private String orderType;
	private String payOrderId;
	
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getToAccountnum() {
		return toAccountnum;
	}
	public void setToAccountnum(String toAccountnum) {
		this.toAccountnum = toAccountnum;
	}
	public String getFromAccountnum() {
		return fromAccountnum;
	}
	public void setFromAccountnum(String fromAccountnum) {
		this.fromAccountnum = fromAccountnum;
	}
	public Integer getBalance() {
		return balance;
	}
	public void setBalance(Integer balance) {
		this.balance = balance;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getPayOrderId() {
		return payOrderId;
	}
	public void setPayOrderId(String payOrderId) {
		this.payOrderId = payOrderId;
	}
	

}
