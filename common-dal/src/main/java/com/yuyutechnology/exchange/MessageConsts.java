package com.yuyutechnology.exchange;

public class MessageConsts {
	// RetCode: 通用 00; 用户 01;兑换 02; 转账 03

	// 通用 00
	/**
	 * 成功
	 */
	public static final String RET_CODE_SUCCESS = "Operation succeeded";
	/**
	 * 失败
	 */
	public static final String RET_CODE_FAILUE = "Operation failed";

	/**
	 * Session过期
	 */
	public static final String SESSION_TIMEOUT = "SESSION_TIMEOUT";
	/**
	 * 参数为空
	 */
	public static final String PARAMETER_IS_EMPTY = "PARAMETER_IS_EMPTY";

	// 用户 01
	/**
	 * 手机号已注册
	 */
	public static final String PHONE_IS_REGISTERED = "PHONE_IS_REGISTERED";
	/**
	 * 手机号不存在
	 */
	public static final String PHONE_NOT_EXIST = "PHONE_NOT_EXIST";
	/**
	 * 手机号与验证码不匹配
	 */
	public static final String PHONE_AND_CODE_NOT_MATCH = "PHONE_AND_CODE_NOT_MATCH";
	/**
	 * goldpay没有绑定手机
	 */
	public static final String GOLDPAY_PHONE_IS_NOT_EXIST = "GOLDPAY_PHONE_IS_NOT_EXIST";
	/**
	 * 登录失败密码错误或TOKEN失效
	 */
	public static final String PASSWORD_NOT_MATCH = "PASSWORD_NOT_MATCH";
	/**
	 * 支付密码不是6位数字
	 */
	public static final String PAY_PASSWORD_IS_ILLEGAL = "PAY_PASSWORD_IS_ILLEGAL";
	/**
	 * 手机号与密码不匹配
	 */
	public static final String PHONE_AND_PASSWORD_NOT_MATCH = "PHONE_AND_PASSWORD_NOT_MATCH";
	/**
	 * 不能添加自己为好友
	 */
	public static final String ADD_FRIEND_OWEN = "COULD_NOT_ADD_OWEN";
	
	// 兑换 02
	/**
	 * 查询不到该用户的wallet信息
	 */
	public static final String EXCHANGE_WALLET_CAN_NOT_BE_QUERIED = "EXCHANGE_WALLET_CAN_NOT_BE_QUERIED";
	/**
	 * 兑换金额大于余额
	 */
	public static final String EXCHANGE_OUTPUTAMOUNT_BIGGER_THAN_BALANCE = "EXCHANGE_OUTPUTAMOUNT_BIGGER_THAN_BALANCE";
	/**
	 * 换算后金额低于最小限额
	 */
	public static final String EXCHANGE_AMOUNT_LESS_THAN_MINIMUM_TRANSACTION_AMOUNT = "EXCHANGE_AMOUNT_LESS_THAN_MINIMUM_TRANSACTION_AMOUNT";

	// 交易03
	/**
	 * 余额不足
	 */
	public static final String TRANSFER_CURRENT_BALANCE_INSUFFICIENT = "TRANSFER_CURRENT_BALANCE_INSUFFICIENT";
	/**
	 * 超过当日最大限额
	 */
	public static final String TRANSFER_EXCEEDED_TRANSACTION_LIMIT = "TRANSFER_EXCEEDED_TRANSACTION_LIMIT";
	/**
	 * 支付密码不正确
	 */
	public static final String TRANSFER_PAYMENTPWD_INCORRECT = "TRANSFER_PAYMENTPWD_INCORRECT";
	
	public static final String TRANSFER_REQUIRES_PHONE_VERIFICATION = "TRANSFER_REQUIRES_PHONE_VERIFICATION";


}
