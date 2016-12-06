package com.yuyutechnology.exchange;

public class ServerConsts {
	
	
	//用户类型
	/**
	 * 普通用户
	 */
	public static final int USER_TYPE_OF_CUSTOMER = 0;
	/**
	 * 系统用户
	 */
	public static final int USER_TYPE_OF_SYSTEM = 1;
	
	//币种
	public static final String CURRENCY_OF_GOLDPAY = "goldpay";
	
	//交易类型
	
	public static final int TRANSFER_TYPE_OF_TRANSACTION = 0;
	public static final int TRANSFER_TYPE_OF_EXCHANGE = 1;
	public static final int TRANSFER_TYPE_OF_GOLDPAYBUY = 2;
	public static final int TRANSFER_TYPE_OF_GIFT= 3;
	
	//通用 00 
	//用户 01
	//兑换 02
	//转账 03
	
	/**
	 * 成功
	 */
	public static final String RET_CODE_SUCCESS = "00_000";
	/**
	 * 失败
	 */
	public static final String RET_CODE_FAILUE = "00_001";
	
	/**
	 * 手机号已注册
	 */
	public static final String PHONE_IS_REGISTERED = "01_001";
	/**
	 * 手机号不存在
	 */
	public static final String PHONE_NOT_EXIST = "01_002";
	/**
	 * 手机号与验证码不匹配
	 */
	public static final String PHONE_AND_CODE_NOT_MATCH = "01_003";
	
	//兑换 02
	/**
	 * 查询不到该用户的wallet信息
	 */
	public static final String EXCHANGE_WALLET_CAN_NOT_BE_QUERIED = "02_001";
	/**
	 * 兑换金额大于余额
	 */
	public static final String EXCHANGE_OUTPUTAMOUNT_BIGGER_THAN_BALANCE = "02_002";
	/**
	 * 换算后金额低于最小限额
	 */
	public static final String EXCHANGE_AMOUNT_LESS_THAN_MINIMUM_TRANSACTION_AMOUNT = "02_003";

	
}
