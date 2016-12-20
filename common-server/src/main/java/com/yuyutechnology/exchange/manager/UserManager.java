package com.yuyutechnology.exchange.manager;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.scheduling.annotation.Scheduled;

import com.yuyutechnology.exchange.dto.CurrencyInfo;
import com.yuyutechnology.exchange.dto.UserInfo;
import com.yuyutechnology.exchange.pojo.AppVersion;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.Friend;

public interface UserManager {
	@PostConstruct
	@Scheduled(cron = "0 1/10 * * * ?")
	void init();
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
	public Integer register(String areaCode, String userPhone, String userName, String userPassword, String loginIp, String pushId, String language);

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

	public AppVersion getAppVersion(String platformType, String updateWay);

	public List<CurrencyInfo> getCurrency();

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

	

}
