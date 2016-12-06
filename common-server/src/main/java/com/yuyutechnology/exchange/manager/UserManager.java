package com.yuyutechnology.exchange.manager;

import java.util.List;

import com.yuyutechnology.exchange.form.UserInfo;
import com.yuyutechnology.exchange.pojo.User;

public interface UserManager {
	// 判断手机号是否为注册用户
	public  boolean isUser(String userPhone);

	// 生成验证码并发送
	public void getPinCode(String userPhone);

	// 验证验证码
	public boolean testPinCode(String userPhone, String verificationCode);

	//添加新用户
	public Integer register(String userPhone, String userName, String userPassword);
	
	public Integer login(String userPhone, String userPassword);
	
	public UserInfo getUserInfo(Integer userId);


	

}
