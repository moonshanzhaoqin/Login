package com.yuyutechnology.exchange;

import org.apache.logging.log4j.message.Message;

public class MessageConsts {
	// RetCode: 通用 00; 用户 01;兑换 02; 转账 03

	// 通用 00
	/**
	 * 成功
	 */
	public static final String RET_CODE_SUCCESS = "OPERATION_SUCCEEDED";
	/**
	 * 失败
	 */
	public static final String RET_CODE_FAILUE = "OPERATION_FAILED";

	/**
	 * Session过期
	 */
	public static final String SESSION_TIMEOUT = "SESSION_TIMEOUT";
	/**
	 * 参数为空
	 */
	public static final String PARAMETER_IS_EMPTY = "PARAMETER_IS_EMPTY";
	/**
	 * 短信发送次数超过限制
	 */
	public static final String SEND_MORE_THAN_LIMIT = "SEND_MORE_THAN_LIMIT";
	
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
	 * 手机号为自己
	 */
	public static final String PHONE_ID_YOUR_OWEN = "PHONE_ID_YOUR_OWEN";
	/**
	 * 账号冻结
	 */
	public static final String USER_BLOCKED = "USER_BLOCKED";
	/**
	 * 登录token失效
	 */
	public static final String TOKEN_NOT_MATCH = "TOKEN_NOT_MATCH";
	/**
	 * 版本号已是最新
	 */
	public static final String VERSION_NUM_IS_LATEST = "VERSION_NUM_IS_LATEST";
	/**
	 * 支付密码不正确
	 */
	public static final String PAY_PWD_NOT_MATCH = "PAY_PWD_NOT_MATCH";
	/**
	 * 好友已添加
	 */
	public static final String FRIEND_HAS_ADDED = "FRIEND_HAS_ADDED";
	/**
	 * 新旧密码相同
	 */
	public static final String NEW_PWD_EQUALS_OLD = "NEW_PWD_EQUALS_OLD";
	/**
	 * 换绑手机时间未到
	 */
	public static final String TIME_NOT_ARRIVED = "TIME_NOT_ARRIVED";
	/**
	 * 该手机号不是好友
	 */
	public static final String PHONE_IS_NOT_FRIEND = "PHONE_IS_NOT_FRIEND";
	/**
	 *  Goldpay校验错误
	 */
	public static final String GOLDPAY_IS_INCORRECT = "GOLDPAY_IS_INCORRECT";
	/**
	 * Goldpay未绑定
	 */
	public static final String GOLDPAY_NOT_BIND = "GOLDPAY_NOT_BIND";
	/**
	 *新设备登录
	 */
	public static final String NEW_DEVICE = "NEW_DEVICE";
	/**
	 * 登录冻结
	 */
	public static final String LOGIN_FREEZE = "LOGIN_FREEZE";
	/**
	 * 支付冻结
	 */
	public static final String PAY_FREEZE = "PAY_FREEZE";
	/**
	 * Goldpay与已绑定账号不符
	 */
	public static final String GOLDPAY_NOT_MATCH_BIND = "GOLDPAY_NOT_MATCH_BIND";
	
	
	
	
	
	
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

	public static final String EXCHANGE_THE_SAME_CURRENCY_CAN_NOT_BE_EXCHANGED = "EXCHANGE_THE_SAME_CURRENCY_CAN_NOT_BE_EXCHANGED";
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

	public static final String TRANSFER_PROHIBIT_TRANSFERS_TO_YOURSELF = "TRANSFER_PROHIBIT_TRANSFERS_TO_YOURSELF";

	public static final String TRANSFER_HISTORY_NOT_ACQUIRED = "TRANSFER_HISTORY_NOT_ACQUIRED";

	public static final String TRANSFER_NOTIFICATION_NOT_ACQUIRED = "TRANSFER_NOTIFICATION_NOT_ACQUIRED";
	

}
