package com.yuyutechnology.exchange.server.controller.request;

public class WithdrawConfirmRequest {
	
	private String payPwd;
	private String transferId;
	public String getPayPwd() {
		return payPwd;
	}
	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}
	public String getTransferId() {
		return transferId;
	}
	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

}
