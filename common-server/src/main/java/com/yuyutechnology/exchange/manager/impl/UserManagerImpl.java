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

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.BindDAO;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dao.UnregisteredDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.form.UserInfo;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Bind;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.Unregistered;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.sms.SmsManager;
import com.yuyutechnology.exchange.utils.MathUtils;

@Service
public class UserManagerImpl implements UserManager {
	public static Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);
	@Autowired
	UserDAO userDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	CurrencyDAO currencyDAO;
	@Autowired
	BindDAO bindDAO;
	@Autowired
	RedisDAO redisDAO;
	@Autowired
	UnregisteredDAO unregisteredDAO;
	@Autowired
	SmsManager smsManager;

	@Override
	public void getPinCode(String areaCode, String userPhone) {
		// 随机生成六位数
		final String random = MathUtils.randomFixedLengthStr(6);
		final String md5random = DigestUtils.md5Hex(random);
		// 存入redis userPhone:md5random
		// TODO 有效时间可配，单位：min
		redisDAO.saveData(areaCode + userPhone, md5random, 10);
		// 发送验证码
		smsManager.sendSMS4PhoneVerify(areaCode, userPhone, random);
	}

	@Override
	public UserInfo getUserInfo(Integer userId) {
		User user = userDAO.getUser(userId);
		UserInfo userInfo = null;
		if (user != null) {
			userInfo = new UserInfo();
			userInfo.setAreaCode(user.getAreaCode());
			userInfo.setPhone(user.getUserPhone());
			userInfo.setName(user.getUserName());
			if (StringUtils.isBlank(user.getUserPayPwd())) {
				userInfo.setPayPwd(false);
			} else {
				userInfo.setPayPwd(true);
			}
			// 判断是否绑定goldpay
			List<Bind> binds = bindDAO.getBindByUserId(userId);
			if (binds.isEmpty()) {
				userInfo.setPayPwd(false);
			} else {
				userInfo.setPayPwd(true);
			}
		}
		return userInfo;
	}

	@Override
	public boolean isUser(String areaCode, String userPhone) {
		User user = userDAO.getUserByUserPhone(areaCode, userPhone);
		if (user == null) {
			return false;
		}
		return true;
	}

	@Override
	public Integer login(String areaCode, String userPhone, String userPassword) {
		User user = userDAO.getUserByUserPhone(areaCode, userPhone);
		if (user != null && StringUtils.equals(user.getUserPassword(),
				DigestUtils.md5Hex(DigestUtils.md5Hex(userPassword) + DigestUtils.md5Hex(userPhone)))) {
			return user.getUserId();
		}
		return null;
	}

	@Override
	public Integer register(String areaCode, String userPhone, String userName, String userPassword) {
		// 添加用户
		Integer userId = userDAO.addUser(new User(areaCode, userPhone, userName,
				DigestUtils.md5Hex(DigestUtils.md5Hex(userPassword) + DigestUtils.md5Hex(userPhone)), new Date(),
				ServerConsts.USER_TYPE_OF_CUSTOMER));
		// 添加钱包信息
		List<Currency> currencies = currencyDAO.getCurrencys();
		for (Currency currency : currencies) {
			walletDAO.addwallet(new Wallet(userId, currency.getCurrency(), new BigDecimal(0)));
		}
		//TODO 根据UNregister 获取转账信息更新钱包
		List<Unregistered> unregistereds=unregisteredDAO.getUnregisteredByUserPhone(areaCode, userPhone);
		for (Unregistered unregistered : unregistereds) {
			//TODO 系统账户    扣款   加流水
			//TODO 用户账户  加款  流水 
			//TODO 更新转账记录
		}
		return userId;
	}

	@Override
	public Integer resetPassword(String areaCode, String userPhone, String newPassword) {
		User user = userDAO.getUserByUserPhone(areaCode, userPhone);
		if (user != null) {
			user.setUserPassword(DigestUtils.md5Hex(DigestUtils.md5Hex(newPassword) + DigestUtils.md5Hex(areaCode + userPhone)));
			userDAO.updateUserPassword(user);
		return user.getUserId();
		}
		return null;
	}

	@Override
	public boolean testPinCode(String areaCode, String userPhone, String verificationCode) {
		// 查redis userPhone: verificationCode
		if (StringUtils.equals(DigestUtils.md5Hex(verificationCode), redisDAO.getValueByKey(areaCode + userPhone))) {
			return true;
		}
		return false;
	}

}
