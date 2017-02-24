package com.yuyutechnology.exchange.dto;

public class UserInfo {

	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", areaCode=" + areaCode + ", phone=" + phone + ", name=" + name
				+ ", goldpayName=" + goldpayName + ", PayPwd=" + PayPwd + "]";
	}

	private int userId;
	private String areaCode;
	private String phone;
	private String name;
	private String goldpayName;
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

	public String getGoldpayName() {
		return goldpayName;
	}

	public void setGoldpayName(String goldpayName) {
		this.goldpayName = goldpayName;
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
