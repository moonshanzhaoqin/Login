package com.yuyutechnology.exchange.crm.request;

public class AddGoldqPayClientRequset {
	private String areaCode;
	private String userPhone;
	private String userPayToken;
	private String name;
	private String redirectUrl;
	private String customDomain;

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

	public String getUserPayToken() {
		return userPayToken;
	}

	public void setUserPayToken(String userPayToken) {
		this.userPayToken = userPayToken;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getCustomDomain() {
		return customDomain;
	}

	public void setCustomDomain(String customDomain) {
		this.customDomain = customDomain;
	}

}
