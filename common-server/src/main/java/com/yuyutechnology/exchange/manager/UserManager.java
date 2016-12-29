package com.yuyutechnology.exchange.manager;

import java.text.ParseException;
import java.util.List;

import com.yuyutechnology.exchange.dto.UserInfo;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.Friend;
import com.yuyutechnology.exchange.pojo.User;

public interface UserManager {
	/**
	 * 生成验证码并发送
	 * 
	 * @param areaCode
	 * @param userPhone
	 */
	public void getPinCode(String func, String areaCode, String userPhone);

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
	// public Integer login(String areaCode, String userPhone, String
	// userPassword, String ip);

	/**
	 * 添加新用户
	 * 
	 * @param areaCode
	 * @param userPhone
	 * @param userName
	 * @param userPassword
	 * @return
	 */
	public Integer register(String areaCode, String userPhone, String userName, String userPassword, String loginIp,
			String pushId, String language);

	/**
	 * 验证手机号与验证码是否匹配
	 * 
	 * @param areaCode
	 * @param userPhone
	 * @param verificationCode
	 * @return
	 */
	public boolean testPinCode(String func, String areaCode, String userPhone, String verificationCode);

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
	
	/**
	 * @param userId
	 * @param goldpayPassword
	 * @return
	 */
	public boolean checkGoldpayPwd(Integer userId, String goldpayPassword);

	/**
	 * 获取好友列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<Friend> getFriends(Integer userId);

	/**
	 * 添加好友
	 * 
	 * @param userId
	 * @param friendId
	 * @param areaCode
	 * @param userPhone
	 * @return
	 */
	public String addfriend(Integer userId, String areaCode, String userPhone);

	/**
	 * 换绑手机
	 * 
	 * @param userId
	 * @param areaCode
	 * @param userPhone
	 */
	public void changePhone(Integer userId, String areaCode, String userPhone);

	/**
	 * 更新登录信息
	 * 
	 * @param userId
	 * @param loginIp
	 * @param pushId
	 * @param pushTag
	 */
	public void updateUser(Integer userId, String loginIp, String pushId, String pushTag);

	/**
	 * 更新钱包
	 * 
	 * @param userId
	 */
	public void updateWallet(Integer userId);

	/**
	 * 校验并增加用户钱包 如果没有该currency的钱包，就新增一个，否则不做任何变化
	 * 
	 * @param userId
	 * @param currency
	 */
	void checkWallet(Integer userId, Currency currency);

	/**
	 * 更新用户名
	 * 
	 * @param userId
	 * @param newUserName
	 */
	public void updateUserName(Integer userId, String newUserName);

	/**
	 * 切换语言
	 * 
	 * @param userId
	 * @param language
	 */
	public void switchLanguage(Integer userId, String language);

	/**
	 * 退出账号处理相关数据
	 * 
	 * @param userId
	 */
	public void logout(Integer userId);

	/**
	 * 清除验证码
	 * 
	 * @param func
	 * @param areaCode
	 * @param userPhone
	 */
	void clearPinCode(String func, String areaCode, String userPhone);

	/**
	 * 检查换绑手机的时间限制是否已到
	 * 
	 * @param userId
	 * @return
	 * @throws ParseException
	 */
	public long checkChangePhoneTime(Integer userId) throws ParseException;

	/**
	 * 删除好友
	 * 
	 * @param userId
	 * @param areaCode
	 * @param phone
	 * @return
	 */
	public String deleteFriend(Integer userId, String areaCode, String phone);
	
	public User getSystemUser();
	
	public List<User> getUserList();
}
