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
	 * 获取用户基本信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserInfo getUserInfo(Integer userId);

	/**
	 * 判断手机号是否为注册用户
	 * 
	 * @param areaCode
	 * @param userPhone
	 * @return
	 */
	public boolean isUser(String areaCode, String userPhone);

	/**
	 * 登录
	 * 
	 * @param areaCode
	 * @param userPhone
	 * @param userPassword
	 * @return
	 */
	public Integer login(String areaCode, String userPhone, String userPassword);

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
	 * 重置密码
	 * 
	 * @param areaCode
	 * @param userPhone
	 * @param newPassword
	 * @return 
	 */
	public Integer resetPassword(String areaCode, String userPhone, String newPassword);

	/**
	 * 验证手机号与验证码是否匹配
	 * 
	 * @param areaCode
	 * @param userPhone
	 * @param verificationCode
	 * @return
	 */
	public boolean testPinCode(String areaCode, String userPhone, String verificationCode);

}
