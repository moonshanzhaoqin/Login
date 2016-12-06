package com.yuyutechnology.exchange.form;

public class UserInfo {
	private String areaCode;
	private String Phone;
	private String Name;
	private boolean isGoldpay;
	private boolean isPayPwd;

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

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
