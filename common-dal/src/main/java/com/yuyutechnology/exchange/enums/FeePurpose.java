package com.yuyutechnology.exchange.enums;

public enum FeePurpose {
	/**
	 * 提取金条（VIP）
	 */
	Withdraw_GoldBullion_VIP("withdraw_goldbullion_vip"), 
	/**
	 * 提取金条（普通）
	 */
	Withdraw_GoldBullion_Ordinary("withdraw_goldbullion_ordinary"), 
	/**
	 * PayPal购买金条（VIP）
	 */
	PayPal_Purchase_GoldBullion_VIP("paypal_purchase_goldbullion_vip"),
	/**
	 * PayPal购买金条（普通）
	 */
	PayPal_Purchase_GoldBullion_Ordinary("paypal_purchase_goldbullion_ordinary");
	
	private String purpose;


	public String getPurpose() {
		return purpose;
	}

	FeePurpose(String purpose) {
		this.purpose = purpose;
	}
	
	
}
