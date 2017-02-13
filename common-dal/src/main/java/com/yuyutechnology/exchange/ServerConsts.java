package com.yuyutechnology.exchange;

import java.util.ArrayList;
import java.util.List;

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
	//检查密码
	public static final int CHECKPWD_STATUS_CORRECT = 0;
	public static final int CHECKPWD_STATUS_INCORRECT  = 1;
	public static final int CHECKPWD_STATUS_FREEZE = 2;
	
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
	/**
	 * 金沛充值
	 */
	public static final int TRANSFER_TYPE_IN_GOLDPAY_REFUND = 6;
	

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
	/**
	 * 交易退回
	 */
	public static final int TRANSFER_STATUS_OF_REFUND = 3;
	
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
	
	public static final String ADMIN_DEFAULT_PASSWORD = "12345678";
	
	public static final String WRONG_PASSWORD = "WPASSWORD";
	public static final String WRONG_PAYPWD= "WPAYPWD";
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

}
