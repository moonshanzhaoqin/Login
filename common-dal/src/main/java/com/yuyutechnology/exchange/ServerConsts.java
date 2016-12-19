package com.yuyutechnology.exchange;

public class ServerConsts {
	// RetCode: 通用 00; 用户 01;兑换 02; 转账 03

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
	 * 转账
	 */
	public static final int TRANSFER_TYPE_OF_TRANSACTION = 0;
	/**
	 * 兑换
	 */
	public static final int TRANSFER_TYPE_OF_EXCHANGE = 1;
	public static final int TRANSFER_TYPE_OF_GOLDPAYBUY = 2;
	public static final int TRANSFER_TYPE_OF_GIFT_IN = 3;
	public static final int TRANSFER_TYPE_OF_GIFT_OUT = 4;
	/**
	 * 系统退款
	 */
	public static final int TRANSFER_TYPE_OF_SYSTEM_REFUND = 5;

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
	public static final int CURRENCY_AVAILABLE = 1;
	public static final int CURRENCY_UNAVAILABLE = 0;
	
	
	


}
