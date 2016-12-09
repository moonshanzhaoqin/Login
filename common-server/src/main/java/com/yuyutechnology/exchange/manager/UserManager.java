package com.yuyutechnology.exchange.manager;

import com.yuyutechnology.exchange.form.UserInfo;

public interface UserManager {
	/**
	 * 生成验证码并发送
	 * 
	 * @param areaCode
	 * @param userPhone
	 */
	public void getPinCode(String areaCode, String userPhone);

	/**
	 * 根据手机号码获取userID(用作判断是否为注册用户)
	 * 
	 * @param areaCode
	 * @param userPhone
	 * @return
	 */
	public Integer getUserId(String areaCode, String userPhone);

	/**
	 * 获取用户基本信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserInfo getUserInfo(Integer userId);

	/**
	 * 登录
	 * 
	 * @param areaCode
	 * @param userPhone
	 * @param userPassword
	 * @param ip 
	 * @return
	 */
	public Integer login(String areaCode, String userPhone, String userPassword, String ip);

	/**
	 * 添加新用户
	 * 
	 * @param areaCode
	 * @param userPhone
	 * @param userName
	 * @param userPassword
	 * @return
	 */
	public Integer register(String areaCode, String userPhone, String userName, String userPassword);

	/**
	 * 验证手机号与验证码是否匹配
	 * 
	 * @param areaCode
	 * @param userPhone
	 * @param verificationCode
	 * @return
	 */
	public boolean testPinCode(String areaCode, String userPhone, String verificationCode);

	/**
	 * 更改密码
	 * 
	 * @param userId
	 * @param newPassword
	 * @return
	 */
	public void updatePassword(Integer userId, String newPassword);

	/**
	 * 更改支付密码
	 * 
	 * @param userId
	 * @param userPayPwd
	 */
	public void updateUserPayPwd(Integer userId, String userPayPwd);

	/**
	 * 校验支付密码
	 * 
	 * @param userId
	 * @param userPayPwd
	 * @return
	 */
	public boolean checkUserPayPwd(Integer userId, String userPayPwd);

	/**
	 * 绑定goldpay
	 * 
	 * @param userId
	 * @param goldpayToken
	 * @return
	 */
	public String bindGoldpay(Integer userId, String goldpayToken);

	/**
	 * 校验用户密码
	 * 
	 * @param userId
	 * @param oldPassword
	 */
	public boolean checkUserPassword(Integer userId, String oldPassword);
}
