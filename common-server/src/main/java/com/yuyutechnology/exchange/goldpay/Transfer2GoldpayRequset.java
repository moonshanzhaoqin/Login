package com.yuyutechnology.exchange.goldpay;

public class Transfer2GoldpayRequset {
	private String toAccountNum;
	private String fromAccountNum;
	private String token;
	private long balance;
	private String comment;
	private String orderType;
	private String payOrderId;

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

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
