package com.yuyutechnology.exchange.dto;

public class UserInfo {
	public UserInfo() {
	}

	public UserInfo(int userId, String areaCode, String phone, String name, boolean payPwd) {
		super();
		this.userId = userId;
		this.areaCode = areaCode;
		this.phone = phone;
		this.name = name;
		PayPwd = payPwd;
	}

	private int userId;
	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", areaCode=" + areaCode + ", phone=" + phone + ", name=" + name
				+ ", PayPwd=" + PayPwd + "]";
	}

	private String areaCode;
	private String phone;
	private String name;
	private boolean PayPwd;

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
