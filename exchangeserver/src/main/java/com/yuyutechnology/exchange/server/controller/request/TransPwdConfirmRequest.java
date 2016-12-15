package com.yuyutechnology.exchange.server.controller.request;

public class TransPwdConfirmRequest {
	
	private String transferId;
	private String userPayPwd;
	
	public String getTransferId() {
		return transferId;
	}
	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
	public String getUserPayPwd() {
		return userPayPwd;
	}
	public void setUserPayPwd(String userPayPwd) {
		this.userPayPwd = userPayPwd;
	}
	

}
