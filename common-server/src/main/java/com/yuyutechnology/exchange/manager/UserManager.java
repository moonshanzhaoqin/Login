package com.yuyutechnology.exchange.manager;

import java.text.ParseException;
import java.util.List;

import com.yuyutechnology.exchange.dto.CheckPwdResult;
import com.yuyutechnology.exchange.dto.UserInfo;
import com.yuyutechnology.exchange.enums.UserConfigKeyEnum;
import com.yuyutechnology.exchange.pojo.Friend;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.sms.SendMessageResponse;

public interface UserManager {
	/**
	 * 生成验证码并发送
	 * 
	 * @param areaCode
	 * @param userPhone
	 * @return
	 */
	SendMessageResponse getPinCode(String func, String areaCode, String userPhone);

	/**
	 * 根据手机号码获取userID(用作判断是否为注册用户)
	 * 
	 * @param areaCode
	 * @param userPhone
	 * @return
	 */
	Integer getUserId(String areaCode, String userPhone);

	/**
	 * 获取用户基本信息
	 * 
	 * @param userId
	 * @return
	 */
	UserInfo getUserInfo(Integer userId);

	/**
	 * 新用户注册
	 * 
	 * @param areaCode
	 * @param userPhone
	 * @param userName
	 * @param userPassword
	 * @return
	 */
	Integer register(String areaCode, String userPhone, String userName, String userPassword, String language);

	/**
	 * 验证手机号与验证码是否匹配
	 * 
	 * @param areaCode
	 * @param userPhone
	 * @param verificationCode
	 * @return
	 */
	Boolean testPinCode(String func, String areaCode, String userPhone, String verificationCode);

	/**
	 * 更改密码
	 * 
	 * @param userId
	 * @param newPassword
	 * @return
	 */
	void updatePassword(Integer userId, String newPassword);

	/**
	 * 更改支付密码
	 * 
	 * @param userId
	 * @param userPayPwd
	 * @return
	 */
	void updateUserPayPwd(Integer userId, String userPayPwd);

	/**
	 * 绑定goldpay
	 * 
	 * @param userId
	 * @param goldpayToken
	 * @return
	 */
	String bindGoldpay(Integer userId, String goldpayToken);

	/**
	 * 校验Goldpay
	 * 
	 * @param userId
	 * @param goldpayPassword
	 * @return
	 */
	String checkGoldpay(Integer userId, String goldpayName, String goldpayPassword);

	/**
	 * 获取好友列表
	 * 
	 * @param userId
	 * @return
	 */
	List<Friend> getFriends(Integer userId);

	/**
	 * 添加好友
	 * 
	 * @param userId
	 * @param friendId
	 * @param areaCode
	 * @param userPhone
	 * @return
	 */
	String addfriend(Integer userId, String areaCode, String userPhone);

	/**
	 * 换绑手机
	 * 
	 * @param userId
	 * @param areaCode
	 * @param userPhone
	 */
	void changePhone(Integer userId, String areaCode, String userPhone);

	/**
	 * 更新登录信息
	 * 
	 * @param userId
	 * @param loginIp
	 * @param pushId
	 * @param pushTag
	 */
	void updateUser(Integer userId, String loginIp, String pushId, String pushTag);

	/**
	 * 更新钱包
	 * 
	 * @param userId
	 */
	void updateWallet(Integer userId);

	/**
	 * 更新用户名
	 * 
	 * @param userId
	 * @param newUserName
	 */
	void updateUserName(Integer userId, String newUserName);

	/**
	 * 切换语言
	 * 
	 * @param userId
	 * @param language
	 */
	void switchLanguage(Integer userId, String language);

	/**
	 * 退出账号处理相关数据 清理pushId，pushTag
	 * 
	 * @param userId
	 */
	void logout(Integer userId);

	/**
	 * 清除验证码
	 * 
	 * @param func
	 * @param areaCode
	 * @param userPhone
	 * @return
	 */
	void clearPinCode(String func, String areaCode, String userPhone);

	/**
	 * 获取可换绑时间
	 * 
	 * @param userId
	 * @return
	 * @throws ParseException
	 */
	long getChangePhoneTime(Integer userId) throws ParseException;

	/**
	 * 删除好友
	 * 
	 * @param userId
	 * @param areaCode
	 * @param phone
	 * @return
	 */
	String deleteFriend(Integer userId, String areaCode, String phone);

	/**
	 * 获取系统用户
	 * 
	 * @return
	 */
	User getSystemUser();

	/**
	 * @return
	 */
	List<User> getUserList();

	/**
	 * @param userId
	 * @param userAvailable
	 */
	void userFreeze(Integer userId, int userAvailable);

	/**
	 * @param userId
	 * @param key
	 * @param value
	 * @return
	 */
	String getUserConfigAndUpdate(Integer userId, UserConfigKeyEnum key, String value);

	/**
	 * 判断是否为新设备
	 * 
	 * @param userId
	 * @param deviceId
	 * @return
	 */
	boolean isNewDevice(Integer userId, String deviceId);

	/**
	 * 添加新设备
	 * 
	 * @param userId
	 * @param deviceId
	 * @param deviceName
	 */
	void addDevice(Integer userId, String deviceId, String deviceName);

	/**
	 * 校验登录密码
	 * 
	 * @param userId
	 * @param userPassword
	 * @return
	 */
	CheckPwdResult checkLoginPassword(Integer userId, String userPassword);

	/**
	 * 校验支付密码
	 * 
	 * @param userId
	 * @param userPayPwd
	 * @return
	 */
	CheckPwdResult checkPayPassword(Integer userId, String userPayPwd);

	/**
	 * 判断新支付密码是否与旧支付密码相同
	 * 
	 * @param userId
	 * @param newUserPayPwd
	 * @return
	 */
	boolean isUserPayPwdEqualsOld(Integer userId, String newUserPayPwd);

	

}
