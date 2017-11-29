package com.yuyutechnology.exchange.crm.request;

public class UpdateGoldqPayClientRequest {
	private int exId;
	private String userPayToken;
	private String clientId;
	private String name;
	private String redirectUrl;
	private String customDomain;

	public int getExId() {
		return exId;
	}

	public void setExId(int exId) {
		this.exId = exId;
	}

	public String getUserPayToken() {
		return userPayToken;
	}

	public void setUserPayToken(String userPayToken) {
		this.userPayToken = userPayToken;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
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
