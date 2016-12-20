package com.yuyutechnology.exchange.goldpay;

public class GoldpayInfo {

	private int retCode;
	private GoldpayUser oauthGoldqUser;

	public int getRetCode() {
		return retCode;
	}

	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}

	public GoldpayUser getOauthGoldqUser() {
		return oauthGoldqUser;
	}

	public void setOauthGoldqUser(GoldpayUser oauthGoldqUser) {
		this.oauthGoldqUser = oauthGoldqUser;
	}
}
