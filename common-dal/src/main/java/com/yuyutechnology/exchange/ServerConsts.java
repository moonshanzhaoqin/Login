package com.yuyutechnology.exchange;

/**
 * @author suzan.wu
 *
 */
public class ServerConsts {
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
	 * 手续费账户
	 */
	public static final int USER_TYPE_OF_FEE = 2;
	/**
	 * 回收账户
	 */
	public static final int USER_TYPE_OF_RECOVERY = 3;
	/**
	 * 冻结账户
	 */
	public static final int USER_TYPE_OF_FROZEN = 4;

	// 冻结状态
	/**
	 * 用户可用
	 */
	public static final int USER_AVAILABLE_OF_AVAILABLE = 1;
	/**
	 * 用户冻结
	 */
	public static final int USER_AVAILABLE_OF_UNAVAILABLE = 0;
	/**
	 * 登录可用
	 */
	public static final int LOGIN_AVAILABLE_OF_AVAILABLE = 1;
	/**
	 * 登录冻结
	 */
	public static final int LOGIN_AVAILABLE_OF_UNAVAILABLE = 0;
	/**
	 * 支付可用
	 */
	public static final int PAY_AVAILABLE_OF_AVAILABLE = 1;
	/**
	 * 支付冻结
	 */
	public static final int PAY_AVAILABLE_OF_UNAVAILABLE = 0;

	// 检查密码，返回状态
	// public static final int CHECKPWD_STATUS_CORRECT = 0;
	// public static final int CHECKPWD_STATUS_INCORRECT = 1;
	// public static final int CHECKPWD_STATUS_FREEZE = 2;

	// 币种
	public static final String CURRENCY_OF_GOLDPAY = "GDQ";

	public static final String CURRENCY_OF_GOLD = "XAU";

	public static final String CURRENCY_OF_CNH = "CNH";

	public static final String CURRENCY_OF_CNY = "CNY";

	public static final String CURRENCY_OF_USD = "USD";

	public static final String CURRENCY_OF_JPY = "JPY";
	
	public static final String CURRENCY_OF_HKD = "HKD";

	// 交易类型
	/**
	 * 交易
	 */
	public static final int TRANSFER_TYPE_TRANSACTION = 0;
	/**
	 * 兑换
	 */
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
	 * 提取金条
	 */
	public static final int TRANSFER_TYPE_IN_WITHDRAW = 4;
	/**
	 * 黄金宝充值
	 */
	public static final int TRANSFER_TYPE_IN_GOLDPAY_RECHARGE = 5;
	/**
	 * 提现退款
	 */
	public static final int TRANSFER_TYPE_IN_WITHDRAW_REFUND = 6;
	/**
	 * Paypal充值
	 */
	public static final int TRANSFER_TYPE_IN_PAYPAL_RECHAEGE = 7;
	/**
	 * 邀请活动奖励
	 */
	public static final int TRANSFER_TYPE_IN_INVITE_CAMPAIGN = 8;
	/**
	 * 手续费
	 */
	public static final int TRANSFER_TYPE_IN_FEE = 9;
	/**
	 * 回收
	 */
	public static final int TRANSFER_TYPE_IN_RECOVERY = 10;
	// 交易状态
	/**
	 * 交易初始化
	 */
	public static final int TRANSFER_STATUS_OF_INITIALIZATION = 0;
	/**
	 * 交易进行中/待审核
	 */
	public static final int TRANSFER_STATUS_OF_PROCESSING = 1;
	/**
	 * 交易已完成
	 */
	public static final int TRANSFER_STATUS_OF_COMPLETED = 2;
	/**
	 * 交易退回
	 */
	public static final int TRANSFER_STATUS_OF_REFUND = 3;

	public static final int EXCHANGE_STATUS_OF_INITIALIZATION = 1;
	public static final int EXCHANGE_STATUS_OF_PROCESS = 2;
	public static final int EXCHANGE_STATUS_OF_COMPLETED = 0;
	public static final int EXCHANGE_STATUS_OF_INTERRUPTED = 3;

	// notification状态
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
	/**
	 * 非法记录
	 */
	public static final int UNREGISTERED_STATUS_OF_ERROR = 3;

	

	// configKey
	/**
	 * 本位货币，主币(结算单位)
	 */
	public static final String STANDARD_CURRENCY = "USD";
//	public static final String STANDARD_CURRENCY = CURRENCY_OF_GOLDPAY;
	// 短信验证码功能
	/**
	 * 短信验证码功能——注册
	 */
	public static final String PIN_FUNC_REGISTER = "REGISTER";
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
	 * 短信验证码功能——新设备登录
	 */
	public static final String PIN_FUNC_NEWDEVICE = "NEWDEVICE";

	/**
	 * 币种状态——可用
	 */
	public static final int CURRENCY_AVAILABLE = 1;
	/**
	 * 币种状态——不可用
	 */
	public static final int CURRENCY_UNAVAILABLE = 0;

	/**
	 * 坏账记录未处理
	 */
	public static final int BAD_ACCOUNT_STATUS_DEFAULT = 0;
	/**
	 * 坏账记录用户冻结
	 */
	public static final int BAD_ACCOUNT_STATUS_FREEZE_USER = 1;
	/**
	 * 坏账记录处理已失效
	 */
	public static final int BAD_ACCOUNT_STATUS_INVALID = 2;

	/**
	 * crm管理员默认密码
	 */
	public static final String ADMIN_DEFAULT_PASSWORD = "12345678";

	public static final String WRONG_PASSWORD = "WPASSWORD";
	public static final String WRONG_PAYPWD = "WPAYPWD";
	public static final String LOGIN_FREEZE = "LOGIN_FREEZE";
	public static final String PAY_FREEZE = "PAY_FREEZE";

	/**
	 * 校验支付密码功能——换绑手机
	 */
	public static final String PAYPWD_CHANGEPHONE = "PAYPWD_CHANGEPHONE";
	/**
	 * 校验支付密码功能——修改支付密码
	 */
	public static final String PAYPWD_MODIFYPAYPWD = "PAYPWD_MODIFYPAYPWD";
	/**
	 *  校验支付密码功能——提取金条
	 */
	public static final String PAYPWD_WITHDRAW = "PAYPWD_WITHDRAW";
	
	/**
	 * 重置支付密码
	 */
	public static final String RESETPAYPWD = "RESETPAYPWD";

	public static final String REDISS_KEY_OF_TOTAL_ANMOUT_OF_GDQ = "totalAmountOfGDQ";

	/**
	 * 活动开启
	 */
	public static final int CAMPAIGN_STATUS_ON = 1;
	/**
	 * 活动关闭
	 */
	public static final int CAMPAIGN_STATUS_OFF = 0;
	public static final String REDIS_KEY_ACTIVE_CAMPAIGN = "ACTIVE_CAMPAIGN";

	public static final int COLLECT_STATUS_UNREGISTER = 0;
	public static final int COLLECT_STATUS_REGISTER = 1;

	/**
	 * goldpay返回成功
	 */
	public static final int GOLDPAY_RETURN_FAIL = 0;
	public static final int GOLDPAY_RETURN_SUCCESS = 1;

	public static final int ALARM_TYPE_WITHDRAW = 6;
	
	//通知方式
	public static final int ALARM_MODE_SMS = 1;
	public static final int ALARM_MODE_EMAIL = 2;
	public static final int ALARM_MODE_SMS_AND_EMAIL = 3;
	
	
	/*提取状态*/
	public static final byte WITHDRAW_RESULT_DEFAULT = 0;
	public static final byte WITHDRAW_RESULT_APPLY_SUCCESS = 1;
	public static final byte WITHDRAW_RESULT_APPLY_FAIL = 2;
	public static final byte WITHDRAW_RESULT_FINISHT = 3;
	public static final byte WITHDRAW_RESULT_CANCEL = 4;
	/*需要验证码验证*/
	public static final String CODE_VERIFICATION_YES = "0";
	/*不需要验证码验证*/
	public static final String CODE_VERIFICATION_NO = "1";
	
	/*需要验证码验证*/
	public static final String MAKE_FRIENDS_YES = "0";
	/*不需要验证码验证*/
	public static final String MAKE_FRIENDS_NO = "1";
	
}
