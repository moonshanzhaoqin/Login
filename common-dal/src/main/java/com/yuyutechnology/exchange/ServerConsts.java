package com.yuyutechnology.exchange;

import java.util.ArrayList;
import java.util.List;

public class ServerConsts {
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
	 * 不能添加自己为好友
	 */
	public static final String ADD_FRIEND_OWEN = "01008";
	/**
	 * 账号冻结
	 */
	public static final String USER_BLOCKED= "01009";
	/**
	 * 登录token失效
	 */
	public static final String TOKEN_NOT_MATCH= "01010";
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
	
	public static final String TRANSFER_NOTIFICATION_NOT_ACQUIRED = "03008";
	
	public static final String TRANSFER_GOLDPAYTRANS_ORDERID_NOT_EXIST = "03009";
	
	public static final String TRANSFER_GOLDPAYTRANS_CHECK_PIN_CODE_FAIL = "03010";
	
	public static final String TRANSFER_LESS_THAN_MINIMUM_AMOUNT = "03011";
	
	public static final String TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED = "03012";
	
	public static final String TRANSFER_TRANS_ORDERID_NOT_EXIST = "03013";
	
	public static final String TRANSFER_GOLDPAYTRANS_HAS_COMPLETED = "03014";
	
	public static final String TRANSFER_GOLDPAYTRANS_GOLDPAY_NOT_ENOUGH = "03015";

	// 用户类型
	/**
	 * 普通用户
	 */
	public static final int USER_TYPE_OF_CUSTOMER = 0;
	/**
	 * 系统用户
	 */
	public static final int USER_TYPE_OF_SYSTEM = 1;
	/**
	 * 用户可用
	 */
	public static final int USER_AVAILABLE_OF_AVAILABLE = 1;
	/**
	 * 用户冻结
	 */
	public static final int USER_AVAILABLE_OF_UNAVAILABLE = 0;

	// 币种
	public static final String CURRENCY_OF_GOLDPAY = "GDQ";

	// 交易类型
	/**
	 * 交易
	 */
	public static final int TRANSFER_TYPE_TRANSACTION = 0;
	
	public static final int TRANSFER_TYPE_EXCHANGE = 1;
	/**
	 * 邀请
	 */
	public static final int TRANSFER_TYPE_OUT_INVITE = 2;
	/**
	 * 系统退款
	 */
	public static final int TRANSFER_TYPE_IN_SYSTEM_REFUND = 3;
	/**
	 * 金沛体现
	 */
	public static final int TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW = 4;
	/**
	 * 金沛充值
	 */
	public static final int TRANSFER_TYPE_IN_GOLDPAY_RECHARGE = 5;

	

	// 交易状态
	/**
	 * 交易初始化
	 */
	public static final int TRANSFER_STATUS_OF_INITIALIZATION = 0;
	/**
	 * 交易进行中
	 */
	public static final int TRANSFER_STATUS_OF_PROCESSING = 1;
	/**
	 * 交易已完成
	 */
	public static final int TRANSFER_STATUS_OF_COMPLETED = 2;
	
	//notification状态
	
	public static final int NOTIFICATION_STATUS_OF_PENDING = 0;
	
	public static final int NOTIFICATION_STATUS_OF_ALREADY_PAID = 1;

	// unregistered状态
	/**
	 * 用户未注册，资金在系统账户
	 */
	public static final int UNREGISTERED_STATUS_OF_PENDING = 0;
	/**
	 * 超时 已退回原账户
	 */
	public static final int UNREGISTERED_STATUS_OF_BACK = 1;
	/**
	 * 用户已注册并取回资金
	 */
	public static final int UNREGISTERED_STATUS_OF_COMPLETED = 2;

	// configKey
	/**
	 * 本位货币，主币(结算单位)
	 */
	public static final String STANDARD_CURRENCY = "standard_currency";
	// 短信验证码功能
	/**
	 * 短信验证码功能——注册
	 */
	public static final String PIN_FUNC_REGISTER = "REGISTER";
	/**
	 * 短信验证码功能——登录
	 */
	// public static final String PIN_FUNC_LOGIN= "LOGIN";
	/**
	 * 短信验证码功能——忘记密码
	 */
	public static final String PIN_FUNC_FORGETPASSWORD = "FORGETPASSWORD";
	/**
	 * 短信验证码功能——换绑手机
	 */
	public static final String PIN_FUNC_CHANGEPHONE = "CHANGEPHONE";
	/**
	 * 短信验证码功能——更换支付密码
	 */
	public static final String PIN_FUNC_MODIFYPAYPWD = "MODIFYPAYPWD";
	/**
	 * 币种状态——可用
	 */
	public static final int CURRENCY_AVAILABLE = 1;
	/**
	 * 币种状态——不可用
	 */
	public static final int CURRENCY_UNAVAILABLE = 0;
	
	
	static {
		successCodeList.add(RET_CODE_SUCCESS);
		successCodeList.add(TRANSFER_EXCEEDED_TRANSACTION_LIMIT);
		successCodeList.add(TRANSFER_HISTORY_NOT_ACQUIRED);
		successCodeList.add(TRANSFER_NOTIFICATION_NOT_ACQUIRED);
		
		sessionCodeList.add(SESSION_TIMEOUT);
	}

}
