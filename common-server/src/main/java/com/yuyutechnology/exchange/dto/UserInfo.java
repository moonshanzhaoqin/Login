package com.yuyutechnology.exchange.dto;

public class UserInfo {

	@Override
	public String toString() {
		return "UserInfo [areaCode=" + areaCode + ", phone=" + phone + ", name=" + name + ", isGoldpay=" + isGoldpay
				+ ", isPayPwd=" + isPayPwd + "]";
	}

	private String areaCode;
	private String phone;
	private String name;
	private boolean isGoldpay;
	private boolean isPayPwd;

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

	public boolean isGoldpay() {
		return isGoldpay;
	}

	public void setGoldpay(boolean isGoldpay) {
		this.isGoldpay = isGoldpay;
	}

	public boolean isPayPwd() {
		return isPayPwd;
	}

	public void setPayPwd(boolean isPayPwd) {
		this.isPayPwd = isPayPwd;
	}

}