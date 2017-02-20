package com.yuyutechnology.exchange.goldpay.transaction;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic=true)
public class CalculateCharge {
	
	private String clientId;
	private String accountNum;
	private boolean to;
	private long amount;
	private String sign;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	public boolean isTo() {
		return to;
	}
	public void setTo(boolean to) {
		this.to = to;
	}
}
