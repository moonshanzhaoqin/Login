package com.yuyutechnology.exchange.goldpay.trans4merge;

public class TransactionDTO {

	private String transId;
	private String orderId;
	private String orderStatus;
	private String transStatus;
	private Integer balance;
	private Integer charge;
	private String chargeRate;
	private String orderCreateAt;
	private String orderProcessingAt;
	private String transCompleteAt;
	private Integer accountId;
	private String accountName;
	private String accountNum;
	private boolean out;
	private String comment;
	private boolean fee;
	private boolean feeyes;
	private String createTimeMilis;
	private String processingTimeMilis;
	private String completeTimeMilis;
	
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getTransStatus() {
		return transStatus;
	}
	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}
	public Integer getBalance() {
		return balance;
	}
	public void setBalance(Integer balance) {
		this.balance = balance;
	}
	public Integer getCharge() {
		return charge;
	}
	public void setCharge(Integer charge) {
		this.charge = charge;
	}
	public String getChargeRate() {
		return chargeRate;
	}
	public void setChargeRate(String chargeRate) {
		this.chargeRate = chargeRate;
	}
	public String getOrderCreateAt() {
		return orderCreateAt;
	}
	public void setOrderCreateAt(String orderCreateAt) {
		this.orderCreateAt = orderCreateAt;
	}
	public String getOrderProcessingAt() {
		return orderProcessingAt;
	}
	public void setOrderProcessingAt(String orderProcessingAt) {
		this.orderProcessingAt = orderProcessingAt;
	}
	public String getTransCompleteAt() {
		return transCompleteAt;
	}
	public void setTransCompleteAt(String transCompleteAt) {
		this.transCompleteAt = transCompleteAt;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	public boolean isOut() {
		return out;
	}
	public void setOut(boolean out) {
		this.out = out;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public boolean isFee() {
		return fee;
	}
	public void setFee(boolean fee) {
		this.fee = fee;
	}
	public boolean isFeeyes() {
		return feeyes;
	}
	public void setFeeyes(boolean feeyes) {
		this.feeyes = feeyes;
	}
	public String getCreateTimeMilis() {
		return createTimeMilis;
	}
	public void setCreateTimeMilis(String createTimeMilis) {
		this.createTimeMilis = createTimeMilis;
	}
	public String getProcessingTimeMilis() {
		return processingTimeMilis;
	}
	public void setProcessingTimeMilis(String processingTimeMilis) {
		this.processingTimeMilis = processingTimeMilis;
	}
	public String getCompleteTimeMilis() {
		return completeTimeMilis;
	}
	public void setCompleteTimeMilis(String completeTimeMilis) {
		this.completeTimeMilis = completeTimeMilis;
	}
	
}
