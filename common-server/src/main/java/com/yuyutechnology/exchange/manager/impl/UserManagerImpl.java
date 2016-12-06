package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.dao.BindDAO;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.form.UserInfo;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Bind;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.utils.MathUtils;

@Service
public class UserManagerImpl implements UserManager {
	@Autowired
	UserDAO userDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	CurrencyDAO currencyDAO;
	@Autowired
	BindDAO bindDAO;
	public static Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);

	@Override
	public boolean isUser(String userPhone) {
		User user = userDAO.getUserByUserPhone(userPhone);
		if (user == null) {
			return false;
		}
		return true;
	}

	@Override
	public void getPinCode(String userPhone) {
		// 随机生成六位数
		final String random = MathUtils.randomFixedLengthStr(6);
		final String md5random = DigestUtils.md5Hex(random);
		// TODO 存入redis userPhone:md5random

		// TODO 发送验证码 (userPhone,random)

	}

	@Override
	public boolean testPinCode(String userPhone, String verificationCode) {

		// TODO 查redis userPhone: verificationCode

		return false;
	}

	@Override
	public Integer register(String userPhone, String userName, String userPassword) {
		//添加用户

		Integer userId = userDAO.addUser(new User(userPhone, userName, DigestUtils.md5Hex(userPassword), new Date(), 0));
		// 添加钱包信息
		List<Currency> currencies = currencyDAO.getCurrencys();
		for (Currency currency : currencies) {
			walletDAO.addwallet(new Wallet(userId, currency.getCurrency(), new BigDecimal(0)));
		}
		return userId;
	}

	@Override
	public Integer login(String userPhone, String userPassword) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserInfo getUserInfo(Integer userId) {
		User user = userDAO.getUser(userId);
		UserInfo userInfo = null;
		if (user != null) {
			userInfo = new UserInfo();
			userInfo.setPhone(user.getUserPhone());
			userInfo.setName(user.getUserName());
			if (StringUtils.isBlank(user.getUserPayPwd())) {
				userInfo.setPayPwd(false);
			} else {
				userInfo.setPayPwd(true);
			}

			// 判断是否绑定goldpay
			Bind bind = bindDAO.getBind(userId);
			if (bind == null) {
				userInfo.setPayPwd(false);
			} else {
				userInfo.setPayPwd(true);
			}

		}
		return userInfo;
	}

}
