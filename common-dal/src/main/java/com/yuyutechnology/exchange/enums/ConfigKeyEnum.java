/**
 * 
 */
package com.yuyutechnology.exchange.enums;

/**
 * @author silent.sun
 *
 */
public enum ConfigKeyEnum {

	DAILYTRANSFERTHRESHOLD("daily_transfer_threshold"), 
	EACHTRANSFERTHRESHOLD("each_transfer_threshold"), 
	TOTALBALANCETHRESHOLD("total_balance_threshold"), 
	ENTERMAXIMUMAMOUNT("enter_maximum_amount"), 
	TPPSCLIENTID("tpps_client_id"), 
	TPPSCLIENTKEY("tpps_client_key"), 
	TPPSTRANSTOKEN("tpps_trans_token"), 
	REFUNTIME("refund_time"), 
	DOWNLOADLINK("download_link"), 
	CHANGEPHONETIME("change_phone_time"), 
	VERIFYTIME("verify_time"),
	RESERVEFUNDS("reserve_funds"),
	TRANSFERLIMITPERPAY("transfer_limit_each_time"),
	TRANSFERLIMITDAILYPAY("transfer_limit_daily_pay"),
	TRANSFERLIMITNUMBEROFPAYPERDAY("transfer_limit_number_of_pay_per_day"),
	EXCHANGELIMITPERPAY("exchange_limit_each_time"),
	EXCHANGELIMITDAILYPAY("exchange_limit_daily_pay"),
	EXCHANGELIMITNUMBEROFPAYPERDAY("exchange_limit_number_of_pay_per_day"),
	LOGIN_UNAVAILIABLE_TIME("login_unavailiable_time"), 
	WRONG_PAYPWD_FREQUENCY("wrong_paypwd_frequency"),
	PAY_UNAVAILIABLE_TIME("pay_unavailiable_time"),
	EXCHANGEFEE("exchange_fee"),
	WRONG_PASSWORD_FREQUENCY("wrong_password_frequency"),
	PAYPAL_RECHARGE("paypal_recharge");

	private String key;

	private ConfigKeyEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
