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
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UnregisteredDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.dao.WalletSeqDAO;
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
	TransferDAO transferDAO;
	@Autowired
	WalletSeqDAO walletSeqDAO;
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
		logger.info("{}");
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
				DigestUtils.md5Hex(DigestUtils.md5Hex(userPassword) + user.getPasswordSalt()))) {
			return user.getUserId();
		}
		return null;
	}

	@Override
	public Integer register(String areaCode, String userPhone, String userName, String userPassword) {
		// 添加用户
		// 随机生成盐值
		String passwordSalt = DigestUtils.md5Hex(MathUtils.randomFixedLengthStr(6));
		// 加密算法：md5(md5(userPassword)+passwordSalt)
		Integer userId = userDAO.addUser(new User(areaCode, userPhone, userName,
				DigestUtils.md5Hex(DigestUtils.md5Hex(userPassword) + passwordSalt), new Date(),
				ServerConsts.USER_TYPE_OF_CUSTOMER, passwordSalt));
		// 添加钱包信息
		List<Currency> currencies = currencyDAO.getCurrencys();
		for (Currency currency : currencies) {
			walletDAO.addwallet(new Wallet(userId, currency.getCurrency(), new BigDecimal(0)));
		}
		// 根据UNregistered 将资金从系统帐户划给新用户
		Integer systemUserId = userDAO.getSystemUser().getUserId();
		List<Unregistered> unregistereds = unregisteredDAO.getUnregisteredByUserPhone(areaCode, userPhone);
		for (Unregistered unregistered : unregistereds) {
			// 系统账号扣款
			walletDAO.updateWalletByUserIdAndCurrency(systemUserId, unregistered.getCurrency(),
					unregistered.getAmount(), "-");
			// 用户加款
			walletDAO.updateWalletByUserIdAndCurrency(userId, unregistered.getCurrency(), unregistered.getAmount(),
					"+");
			// 增加seq记录
			walletSeqDAO.addWalletSeq4Transaction(systemUserId, userId, ServerConsts.TRANSFER_TYPE_OF_TRANSACTION,
					unregistered.getTransferId(), unregistered.getCurrency(), unregistered.getAmount());
			// 更改Transfer状态
			transferDAO.updateTransferStatusAndUserTo(unregistered.getTransferId(),
					ServerConsts.TRANSFER_STATUS_OF_COMPLETED, userId);
			// 更改unregistered状态
			unregistered.setUnregisteredStatus(ServerConsts.UNREGISTERED_STATUS_OF_COMPLETED);
			unregisteredDAO.updateUnregistered(unregistered);
		}
		return userId;
	}

	@Override
	public Integer resetPassword(String areaCode, String userPhone, String newPassword) {
		User user = userDAO.getUserByUserPhone(areaCode, userPhone);
		if (user != null) {
			// 随机生成盐值
			String passwordSalt = DigestUtils.md5Hex(MathUtils.randomFixedLengthStr(6));
			user.setUserPassword(DigestUtils.md5Hex(DigestUtils.md5Hex(newPassword) + passwordSalt));
			user.setPasswordSalt(passwordSalt);
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
