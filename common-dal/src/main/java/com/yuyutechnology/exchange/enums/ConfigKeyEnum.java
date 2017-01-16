/**
 * 
 */
package com.yuyutechnology.exchange.enums;

/**
 * @author silent.sun
 *
 */
public enum ConfigKeyEnum {

	DAILYTRANSFERTHRESHOLD("daily_transfer_threshold"), EACHTRANSFERTHRESHOLD(
			"each_transfer_threshold"), TOTALBALANCETHRESHOLD("total_balance_threshold"), ENTERMAXIMUMAMOUNT(
					"enter_maximum_amount"), TPPSCLIENTID(
							"tpps_client_id"), TPPSCLIENTKEY("tpps_client_key"), TPPSTRANSTOKEN(
									"tpps_trans_token"), REFUNTIME("refund_time"), DOWNLOADLINK(
											"download_link"), CHANGEPHONETIME("change_phone_time"), VERIFYTIME("verify_time")
												,RESERVEFUNDS("reserve_funds");

	private String key;

	private ConfigKeyEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
