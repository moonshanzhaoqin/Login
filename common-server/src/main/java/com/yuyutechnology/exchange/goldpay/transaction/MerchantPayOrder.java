package com.yuyutechnology.exchange.goldpay.transaction;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic=true)
public class MerchantPayOrder {
	private String fromAccountNum;
	private String fromAccountName; 
	private String fromAccountToken; 
	private String toAccountNum; 
	
	private String orderId;
	private String clientId; 
	private Integer payAmount; 
	private Integer type; 
	private String nonceStr; 
	private String sign; 
	private String itemDesc; 
	private String attach;
	
	public String getFromAccountNum() {
		return fromAccountNum;
	}
	public void setFromAccountNum(String fromAccountNum) {
		this.fromAccountNum = fromAccountNum;
	}
	public String getFromAccountName() {
		return fromAccountName;
	}
	public void setFromAccountName(String fromAccountName) {
		this.fromAccountName = fromAccountName;
	}
	public String getFromAccountToken() {
		return fromAccountToken;
	}
	public void setFromAccountToken(String fromAccountToken) {
		this.fromAccountToken = fromAccountToken;
	}
	public String getToAccountNum() {
		return toAccountNum;
	}
	public void setToAccountNum(String toAccountNum) {
		this.toAccountNum = toAccountNum;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public Integer getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(Integer payAmount) {
		this.payAmount = payAmount;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	} 
}
