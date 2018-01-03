package com.yuyutechnology.exchange.server.controller.request;

public class HappyLivesVIPRequest extends BaseRequest{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7344256361211606704L;
	private Integer userId;
	private String happyLivesId;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getHappyLivesId() {
		return happyLivesId;
	}
	public void setHappyLivesId(String happyLivesId) {
		this.happyLivesId = happyLivesId;
	}
}
