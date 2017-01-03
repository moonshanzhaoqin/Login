package com.yuyutechnology.exchange;

public class RetCodeConsts {
	/**
	 * 成功
	 */
	public static final int SUCCESS = 00000;
	/**
	 * 失败
	 */
	public static final int FAILUE = 00001;
	/**
	 * Session过期
	 */
	public static final int SESSION_TIMEOUT = 00002;
	/**
	 * 参数为空
	 */
	public static final int PARAMETER_IS_EMPTY = 00003;
	/**
	 * 管理员不存在
	 */
	public static final int ADMIN_NOT_EXIST = 01001;
	/**
	 * 用户名和密码不匹配
	 */
	public static final int PASSWORD_NOT_MATCH_NAME = 01002;

	/**
	 * 货币不存在
	 */
	public static final int CURRENCY_NOT_EXIST = 02001;

	/**
	 * 货币已是可用
	 */

	public static final int CURRENCY_HAS_BEEN_AVAILABLE = 02002;

	/**
	 * 货币已是不可用
	 */
	public static final int CURRENCY_HAS_BEEN_UNAVAILABLE = 02003;

}
