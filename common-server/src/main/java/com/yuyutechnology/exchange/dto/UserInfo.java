package com.yuyutechnology.exchange.dto;

public class UserInfo {
	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", areaCode=" + areaCode + ", phone=" + phone + ", name=" + name
				+ ", portrait=" + portrait + ", PayPwd=" + PayPwd + "]";
	}

	private int userId;
	private String areaCode;
	private String phone;
	private String name;
	private String portrait;
	private boolean PayPwd;

	public UserInfo() {
	}

	public UserInfo(int userId, String areaCode, String phone, String name, String portrait, boolean payPwd) {
		super();
		this.userId = userId;
		this.areaCode = areaCode;
		this.phone = phone;
		this.name = name;
		this.portrait = portrait;
		PayPwd = payPwd;
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

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public boolean isPayPwd() {
		return PayPwd;
	}

	public void setPayPwd(boolean PayPwd) {
		this.PayPwd = PayPwd;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
