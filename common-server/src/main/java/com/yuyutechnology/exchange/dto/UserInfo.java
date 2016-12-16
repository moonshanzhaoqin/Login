package com.yuyutechnology.exchange.dto;

import com.yuyutechnology.exchange.pojo.Bind;

public class UserInfo {

	@Override
	public String toString() {
		return "UserInfo [areaCode=" + areaCode + ", phone=" + phone + ", name=" + name + ", goldpay=" + goldpay
				+ ", isPayPwd=" + isPayPwd + "]";
	}

	private String areaCode;
	private String phone;
	private String name;
	private Bind goldpay;
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

	public Bind getGoldpay() {
		return goldpay;
	}

	public void setGoldpay(Bind goldpay) {
		this.goldpay = goldpay;
	}

	public boolean isPayPwd() {
		return isPayPwd;
	}

	public void setPayPwd(boolean isPayPwd) {
		this.isPayPwd = isPayPwd;
	}

}
