package com.yuyutechnology.exchange.dto;

public class UserInfo {


	@Override
	public String toString() {
		return "UserInfo [areaCode=" + areaCode + ", phone=" + phone + ", name=" + name + ", goldpayId=" + goldpayId
				+ ", goldpayName=" + goldpayName + ", goldpayAcount=" + goldpayAcount + ", isPayPwd=" + isPayPwd + "]";
	}

	private String areaCode;
	private String phone;
	private String name;
	private String goldpayId;
	private String goldpayName;
	private String goldpayAcount;
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

	public String getGoldpayId() {
		return goldpayId;
	}

	public void setGoldpayId(String goldpayId) {
		this.goldpayId = goldpayId;
	}

	public String getGoldpayName() {
		return goldpayName;
	}

	public void setGoldpayName(String goldpayName) {
		this.goldpayName = goldpayName;
	}

	public String getGoldpayAcount() {
		return goldpayAcount;
	}

	public void setGoldpayAcount(String goldpayAcount) {
		this.goldpayAcount = goldpayAcount;
	}

	public boolean isPayPwd() {
		return isPayPwd;
	}

	public void setPayPwd(boolean isPayPwd) {
		this.isPayPwd = isPayPwd;
	}

}
