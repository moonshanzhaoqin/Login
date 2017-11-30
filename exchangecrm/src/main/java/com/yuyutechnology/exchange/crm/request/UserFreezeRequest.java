package com.yuyutechnology.exchange.crm.request;

public class UserFreezeRequest {

	private Integer userId;
	private Integer operate;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getOperate() {
		return operate;
	}

	public void setOperate(Integer operate) {
		this.operate = operate;
	}

}
