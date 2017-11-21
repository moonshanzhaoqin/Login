package com.yuyutechnology.exchange.crm.request;

public class AddPayClientRequset {
	@Override
	public String toString() {
		return "AddPayClientRequset [areaCode=" + areaCode + ", userPhone=" + userPhone + "]";
	}
	private String areaCode;
	private String userPhone;
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
}
