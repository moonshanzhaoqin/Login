package com.yuyutechnology.exchange;

import java.util.ArrayList;
import java.util.List;

public class RetCodeConsts {
	// RetCode: 通用 00; 用户 01;兑换 02; 转账 03

	public static List<String> successCodeList = new ArrayList<String>();
	public static List<String> sessionCodeList = new ArrayList<String>();

	// 通用 00
	/**
	 * 成功
	 */
	public static final String RET_CODE_SUCCESS = "00000";
	/**
	 * 失败
	 */
	public static final String RET_CODE_FAILUE = "00001";
	/**
	 * Session过期
	 */
	public static final String SESSION_TIMEOUT = "00002";
	/**
	 * 参数为空
	 */
	public static final String PARAMETER_IS_EMPTY = "00003";
	/**
	 * 短信发送次数超过限制
	 */
	public static final String SEND_MORE_THAN_LIMIT = "00004";
	// 用户 01
	/**
	 * 手机号已注册
	 */
	public static final String PHONE_IS_REGISTERED = "01001";
	/**
	 * 手机号不存在
	 */
	public static final String PHONE_NOT_EXIST = "01002";
	/**
	 * 手机号与验证码不匹配
	 */
	public static final String PHONE_AND_CODE_NOT_MATCH = "01003";
	/**
	 * goldpay没有绑定手机
	 */
	public static final String GOLDPAY_PHONE_IS_NOT_EXIST = "01004";
	/**
	 * 登录密码错误
	 */
	public static final String PASSWORD_NOT_MATCH = "01005";
	/**
	 * 支付密码不是6位数字
	 */
	public static final String PAY_PASSWORD_IS_ILLEGAL = "01006";
	/**
	 * 手机号与密码不匹配
	 */
	public static final String PHONE_AND_PASSWORD_NOT_MATCH = "01007";
	/**
	 * 手机号为自己
	 */
	public static final String PHONE_ID_YOUR_OWEN = "01008";
	/**
	 * 账号冻结
	 */
	public static final String USER_BLOCKED = "01009";
	/**
	 * 登录token失效
	 */
	public static final String TOKEN_NOT_MATCH = "01010";
	/**
	 * 版本号已是最新
	 */
	public static final String VERSION_NUM_IS_LATEST = "01011";
	/**
	 * 支付密码不正确
	 */
	public static final String PAY_PWD_NOT_MATCH = "01012";
	/**
	 * 好友已添加
	 */
	public static final String FRIEND_HAS_ADDED = "01013";
	/**
	 * 新旧密码相同
	 */
	public static final String NEW_PWD_EQUALS_OLD = "01014";
	/**
	 * 换绑手机时间未到
	 */
	public static final String TIME_NOT_ARRIVED = "01015";
	/**
	 * 该手机号不是好友
	 */
	public static final String PHONE_IS_NOT_FRIEND = "01016";
	/**
	 * Goldpay校验错误
	 */
	public static final String GOLDPAY_IS_INCORRECT = "01017";
	/**
	 * Goldpay未绑定
	 */
	public static final String GOLDPAY_NOT_BIND = "01018";
	/**
	 * 新设备登录
	 */
	public static final String NEW_DEVICE = "01019";
	/**
	 * 登录冻结
	 */
	public static final String LOGIN_FREEZE = "01020";
	/**
	 * 支付冻结
	 */
	public static final String PAY_FREEZE = "01021";
	/**
	 * Goldpay与已绑定账号不符
	 */
	public static final String GOLDPAY_NOT_MATCH_BIND = "01022";
	/**
	 * 没有获取验证码
	 */
	public static final String NOT_GET_CODE = "01023";
	// 兑换 02
	/**
	 * 查询不到该用户的wallet信息
	 */
	public static final String EXCHANGE_WALLET_CAN_NOT_BE_QUERIED = "02001";
	/**
	 * 兑换金额大于余额
	 */
	public static final String EXCHANGE_OUTPUTAMOUNT_BIGGER_THAN_BALANCE = "02002";
	/**
	 * 换算后金额低于最小限额
	 */
	public static final String EXCHANGE_AMOUNT_LESS_THAN_MINIMUM_TRANSACTION_AMOUNT = "02003";

	public static final String EXCHANGE_THE_SAME_CURRENCY_CAN_NOT_BE_EXCHANGED = "02004";

	public static final String EXCHANGE_ENTER_THE_AMOUNT_OF_VIOLATION = "02005";

	public static final String EXCHANGE_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY = "02006";

	public static final String EXCHANGE_LIMIT_EACH_TIME = "02007";

	public static final String EXCHANGE_LIMIT_DAILY_PAY = "02008";

	public static final String EXCHANGE_LIMIT_NUM_OF_PAY_PER_DAY = "02009";

	// 交易03
	/**
	 * 余额不足
	 */
	public static final String TRANSFER_CURRENT_BALANCE_INSUFFICIENT = "03001";
	/**
	 * 超过当日最大限额
	 */
	public static final String TRANSFER_EXCEEDED_TRANSACTION_LIMIT = "03002";
	/**
	 * 支付密码不正确
	 */
	public static final String TRANSFER_PAYMENTPWD_INCORRECT = "03003";

	public static final String TRANSFER_REQUIRES_PHONE_VERIFICATION = "03004";
	/**
	 * 支付手机验证码错误
	 */
	public static final String PIN_CODE_INCORRECT = "03005";

	public static final String TRANSFER_PROHIBIT_TRANSFERS_TO_YOURSELF = "03006";

	public static final String TRANSFER_HISTORY_NOT_ACQUIRED = "03007";

	// public static final String TRANSFER_NOTIFICATION_NOT_ACQUIRED = "03008";

	public static final String TRANSFER_GOLDPAYTRANS_ORDERID_NOT_EXIST = "03009";

	public static final String TRANSFER_GOLDPAYTRANS_CHECK_PIN_CODE_FAIL = "03010";

	public static final String TRANSFER_LESS_THAN_MINIMUM_AMOUNT = "03011";

	public static final String TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED = "03012";

	public static final String TRANSFER_TRANS_ORDERID_NOT_EXIST = "03013";

	public static final String TRANSFER_GOLDPAYTRANS_HAS_COMPLETED = "03014";

	public static final String TRANSFER_GOLDPAYTRANS_GOLDPAY_NOT_ENOUGH = "03015";

	public static final String TRANSFER_PHONE_NUMBER_IS_EMPTY = "03016";

	public static final String TRANSFER_FILL_OUT_THE_ALLOWABLE_AMOUNT = "03017";

	public static final String TRANSFER_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY = "03018";

	public static final String TRANSFER_REQUEST_INFORMATION_NOT_MATCH = "03019";

	public static final String TRANSFER_LIMIT_EACH_TIME = "03020";

	public static final String TRANSFER_LIMIT_DAILY_PAY = "03021";

	public static final String TRANSFER_LIMIT_NUM_OF_PAY_PER_DAY = "03022";
	
	public static final String TRANSFER_PAYPALTRANS_ILLEGAL_DATA = "03023";
	

	// crm 管理员04
	/**
	 * 管理员不存在
	 */
	public static final String ADMIN_NOT_EXIST = "04001";
	/**
	 * 用户名和密码不匹配
	 */
	public static final String PASSWORD_NOT_MATCH_NAME = "04002";

	// crm 币种05
	/**
	 * 币种已存在
	 */
	public static final String CURRENCY_IS_EXIST = "05001";

	static {
		successCodeList.add(RET_CODE_SUCCESS);
		successCodeList.add(TRANSFER_REQUIRES_PHONE_VERIFICATION);
		successCodeList.add(TRANSFER_HISTORY_NOT_ACQUIRED);
		// successCodeList.add(TRANSFER_NOTIFICATION_NOT_ACQUIRED);
		successCodeList.add(TIME_NOT_ARRIVED);
		successCodeList.add(NEW_DEVICE);
		sessionCodeList.add(SESSION_TIMEOUT);
	}
}
