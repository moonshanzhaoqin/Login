package com.yuyutechnology.exchange.server.controller.dto;

public class FriendDTO {
	
	private String areaCode;
	private String phone;
	private String name;
	
	public FriendDTO() {
		super();
	}
	public FriendDTO(String areaCode, String phone, String name) {
		super();
		this.areaCode = areaCode;
		this.phone = phone;
		this.name = name;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
