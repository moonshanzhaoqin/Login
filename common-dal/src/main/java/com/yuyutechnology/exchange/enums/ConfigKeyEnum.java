/**
 * 
 */
package com.yuyutechnology.exchange.enums;

/**
 * @author silent.sun
 *
 */
/**
 * @author suzan.wu
 *
 */
public enum ConfigKeyEnum {

	/**
	 * 短信验证每日转账最大阈值（美元）
	 */
	DAILYTRANSFERTHRESHOLD("daily_transfer_threshold"), 
	/**
	 * 短信验证每笔转账阈值（美元）
	 */
	EACHTRANSFERTHRESHOLD("each_transfer_threshold"), 
	/**
	 * 短信验证总账阈值（美元）
	 */
	TOTALBALANCETHRESHOLD("total_balance_threshold"), 
	/**
	 * 最大输入
	 */
	ENTERMAXIMUMAMOUNT("enter_maximum_amount"), 
	/**
	 * 第三方支付商户ID
	 */
	TPPSCLIENTID("tpps_client_id"), 
	/**
	 * 第三方支付商户Key
	 */
	TPPSCLIENTKEY("tpps_client_key"), 
	/**
	 * 第三方支付商户Token
	 */
	TPPSTRANSTOKEN("tpps_trans_token"), 
	/**
	 * refund_time
	 */
	REFUNTIME("refund_time"), 
	/**
	 * download_link
	 */
	DOWNLOADLINK("download_link"), 
	/**
	 * 换绑手机间隔（DAY）
	 */
	CHANGEPHONETIME("change_phone_time"), 
	/**
	 * 验证码过期时间（MIN）
	 */
	VERIFYTIME("verify_time"),
	/**
	 * 预备金金额（GDQ）
	 */
	RESERVEFUNDS("reserve_funds"),
	/**
	 * 每次支付金额限制（美元）
	 */
	TRANSFERLIMITPERPAY("transfer_limit_each_time"),
	/**
	 * 每天支付金额限制（美元）
	 */
	TRANSFERLIMITDAILYPAY("transfer_limit_daily_pay"),
	/**
	 * 每天支付次数限制
	 */
	TRANSFERLIMITNUMBEROFPAYPERDAY("transfer_limit_number_of_pay_per_day"),
	/**
	 * 每次兑换金额限制（美元）
	 */
	EXCHANGELIMITPERPAY("exchange_limit_each_time"),
	/**
	 * 每天兑换金额限制（美元）
	 */
	EXCHANGELIMITDAILYPAY("exchange_limit_daily_pay"),
	/**
	 * 每天兑换次数限制
	 */
	EXCHANGELIMITNUMBEROFPAYPERDAY("exchange_limit_number_of_pay_per_day"),
	/**
	 * 登录冻结时间（H）
	 */
	LOGIN_UNAVAILIABLE_TIME("login_unavailiable_time"), 
	/**
	 * 错误支付密码次数
	 */
	WRONG_PAYPWD_FREQUENCY("wrong_paypwd_frequency"),
	/**
	 * 支付冻结时间（H）
	 */
	PAY_UNAVAILIABLE_TIME("pay_unavailiable_time"),
	/**
	 * 兑换手续分（千分比）
	 */
	EXCHANGEFEE("exchange_fee"),
	/**
	 * 错误支付密码次数
	 */
	WRONG_PASSWORD_FREQUENCY("wrong_password_frequency"),
	/**
	 * 开启Paypal充值Goldpay
	 */
	PAYPAL_RECHARGE("paypal_recharge"),
	/**
	 * 可售沛金条总量
	 */
	TOTALGDQCANBESOLD("total_gdq_can_be_sold");

	private String key;

	private ConfigKeyEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
