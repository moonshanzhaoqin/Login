package com.yuyutechnology.exchange.server.controller.request;

public class TransConfirmRequest {
	
	private Integer userId;
	private String transferId;
	private String userPayPwd;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

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
