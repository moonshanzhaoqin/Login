package com.yuyutechnology.exchange.manager;

import com.yuyutechnology.exchange.form.UserInfo;

public interface UserManager {
	/**
	 * 判断手机号是否为注册用户
	 * 
	 * @param userPhone
	 * @return
	 */
	public boolean isUser(String userPhone);

	/**
	 * 生成验证码并发送
	 * 
	 * @param userPhone
	 */
	public void getPinCode(String userPhone);

	/**
	 * 验证手机号与验证码是否匹配
	 * 
	 * @param userPhone
	 * @param verificationCode
	 * @return
	 */
	public boolean testPinCode(String userPhone, String verificationCode);

	/**
	 * 添加新用户
	 * 
	 * @param userPhone
	 * @param userName
	 * @param userPassword
	 * @return
	 */
	public Integer register(String userPhone, String userName, String userPassword);

	/**
	 * 登录
	 * 
	 * @param userPhone
	 * @param userPassword
	 * @return
	 */
	public Integer login(String userPhone, String userPassword);

	/**
	 * 获取用户基本信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserInfo getUserInfo(Integer userId);

	/**
	 * 重置密码
	 * 
	 * @param userPhone
	 * @param newPassword
	 */
	public void resetPassword(String userPhone, String newPassword);

}
