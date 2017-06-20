package com.yuyutechnology.exchange;

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
	public static final int CHECKPWD_STATUS_CORRECT = 0;
	public static final int CHECKPWD_STATUS_INCORRECT = 1;
	public static final int CHECKPWD_STATUS_FREEZE = 2;

	// 币种
	public static final String CURRENCY_OF_GOLDPAY = "GDQ";

	public static final String CURRENCY_OF_GOLD = "XAU";

	public static final String CURRENCY_OF_CNH = "CNH";

	public static final String CURRENCY_OF_CNY = "CNY";

	public static final String CURRENCY_OF_USD = "USD";
	
	public static final String CURRENCY_OF_JPY = "JPY";

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
	 * 金沛提现
	 */
	public static final int TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW = 4;
	/**
	 * 金沛充值
	 */
	public static final int TRANSFER_TYPE_IN_GOLDPAY_RECHARGE = 5;
	/**
	 * 金沛提现退款
	 */
	public static final int TRANSFER_TYPE_IN_GOLDPAY_REFUND = 6;
	/**
	 * Paypal充值
	 */
	public static final int TRANSFER_TYPE_IN_PAYPAL_RECHAEGE = 7;

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
	
	
	/**
	 * 待支付
	 */
	public static final int TRANSFER_STATUS_OF_AUTOREVIEW_SUCCESS = 4;
	/**
	 * 审核失败
	 */
	public static final int TRANSFER_STATUS_OF_AUTOREVIEW_FAIL = 5;
	/**
	 * 支付失败
	 */
	public static final int TRANSFER_STATUS_OF_GOLDPAYREMIT_FAIL = 8;

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
	 * 校验支付密码功能——换绑Goldpay
	 */
	public static final String PAYPWD_CHANGEGOLDPAY = "PAYPWD_CHANGEGOLDPAY";
	/**
	 * 校验支付密码功能——修改支付密码
	 */
	public static final String PAYPWD_MODIFYPAYPWD = "PAYPWD_MODIFYPAYPWD";
	/**
	 * 重置支付密码
	 */
	public static final String RESETPAYPWD = "RESETPAYPWD";

	/**
	 * 提现审核开启
	 * GOLDPAY_WITHDRAW_FORBIDDEN=
	 */
	public static final String ACCOUTING_TASK_OPEN = "false";
	
	/**
	 * 提现审核关闭
	 * GOLDPAY_WITHDRAW_FORBIDDEN=
	 */
	public static final String ACCOUTING_TASK_CLOSED = "true";
	
	
	
	
	
	
}
