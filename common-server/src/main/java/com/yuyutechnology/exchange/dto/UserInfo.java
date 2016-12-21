package com.yuyutechnology.exchange.dto;

public class UserInfo {

	@Override
	public String toString() {
		return "UserInfo [areaCode=" + areaCode + ", phone=" + phone + ", name=" + name + ", goldpayName=" + goldpayName
				+ ", isPayPwd=" + isPayPwd + "]";
	}

	private String areaCode;
	private String phone;
	private String name;
	private String goldpayName;
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

	public String getGoldpayName() {
		return goldpayName;
	}

	public void setGoldpayName(String goldpayName) {
		this.goldpayName = goldpayName;
	}

	public boolean isPayPwd() {
		return isPayPwd;
	}

	public void setPayPwd(boolean isPayPwd) {
		this.isPayPwd = isPayPwd;
	}

}
