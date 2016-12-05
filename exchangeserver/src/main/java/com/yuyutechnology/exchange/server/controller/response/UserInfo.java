package com.yuyutechnology.exchange.server.controller.response;

public class UserInfo {
	private String Phone;
	private String Name;
	private boolean isGoldpay;
	private boolean isPayPwd;

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
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
