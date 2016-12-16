package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.AppVersionDAO;
import com.yuyutechnology.exchange.dao.BindDAO;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.FriendDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UnregisteredDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.dao.WalletSeqDAO;
import com.yuyutechnology.exchange.dto.CurrencyInfo;
import com.yuyutechnology.exchange.dto.UserInfo;
import com.yuyutechnology.exchange.goldpay.GoldpayManager;
import com.yuyutechnology.exchange.goldpay.GoldpayUser;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.AppVersion;
import com.yuyutechnology.exchange.pojo.Bind;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.Friend;
import com.yuyutechnology.exchange.pojo.FriendId;
import com.yuyutechnology.exchange.pojo.Unregistered;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.sms.SmsManager;
import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.LanguageUtils;
import com.yuyutechnology.exchange.utils.MathUtils;
import com.yuyutechnology.exchange.utils.PasswordUtils;

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
	AppVersionDAO appVersionDAO;
	@Autowired
	RedisDAO redisDAO;
	@Autowired
	UnregisteredDAO unregisteredDAO;
	@Autowired
	FriendDAO friendDAO;
	@Autowired
	SmsManager smsManager;
	@Autowired
	GoldpayManager goldpayManager;

	@Override
	public String addfriend(Integer userId, String areaCode, String userPhone) {
		User friend = userDAO.getUserByUserPhone(areaCode, userPhone);
		if (friend == null) {
			return ServerConsts.PHONE_NOT_EXIST;
		} else if (friend.getUserId() == userId) {
			return ServerConsts.ADD_FRIEND_OWEN;
		} else {
			friendDAO.addfriend(new Friend(new FriendId(userId, friend.getUserId()), friend, new Date()));
			return ServerConsts.RET_CODE_SUCCESS;
		}
	}

	@Override
	public String bindGoldpay(Integer userId, String goldpayToken) {
		logger.info("绑定goldpay账户====");
		GoldpayUser goldpayUser = goldpayManager.getGoldpayInfo(goldpayToken);
		if (goldpayUser == null) {
			logger.info("goldpay账户不存在");
			return ServerConsts.RET_CODE_FAILUE;
		} else {
			logger.info("goldpayUser==", goldpayUser.toString());
			if (StringUtils.isBlank(goldpayUser.getAreaCode()) || StringUtils.isBlank(goldpayUser.getMobile())) {
				logger.info("goldpay账户没有绑定手机号");
				return ServerConsts.GOLDPAY_PHONE_IS_NOT_EXIST;
			} else {
				Bind bind = new Bind(userId, goldpayUser.getId(), goldpayUser.getUsername(),
						goldpayUser.getAccountNum(), goldpayToken);
				bindDAO.saveBind(bind);
				return ServerConsts.RET_CODE_SUCCESS;
			}
		}
	}

	@Override
	public void changePhone(Integer userId, String areaCode, String userPhone) {
		User user = userDAO.getUser(userId);
		user.setAreaCode(areaCode);
		user.setUserPhone(userPhone);
		userDAO.updateUser(user);
	}

	@Override
	public boolean checkUserPassword(Integer userId, String userPassword) {
		logger.info("校验用户 {} 的密码=====", userId);
		User user = userDAO.getUser(userId);
		if (PasswordUtils.check(userPassword, user.getUserPassword(), user.getPasswordSalt())) {
			logger.info("***匹配***");
			return true;
		}
		logger.info("***不匹配***");
		return false;
	}

	@Override
	public boolean checkUserPayPwd(Integer userId, String userPayPwd) {
		logger.info("校验用户{}的支付密码{}", userId, userPayPwd);
		User user = userDAO.getUser(userId);
		if (PasswordUtils.check(userPayPwd, user.getUserPayPwd(), user.getUserPassword())) {
			logger.info("***匹配***");
			return true;
		}
		logger.info("***不匹配***");
		return false;
	}

	@Override
	public List<Friend> getFriends(Integer userId) {
		List<Friend> friends = friendDAO.getFriendsByUserId(userId);
		return friends;
	}

	@Override
	public void getPinCode(String func, String areaCode, String userPhone) {
		// 随机生成六位数
		final String random = MathUtils.randomFixedLengthStr(6);
		logger.info("getPinCode : phone={}, pincode={}", areaCode + userPhone, random);
		final String md5random = DigestUtils.md5Hex(random);
		// 存入redis userPhone:md5random
		// TODO 有效时间可配，单位：min
		redisDAO.saveData(func + areaCode + userPhone, md5random, 10);
		// 发送验证码
		smsManager.sendSMS4PhoneVerify(areaCode, userPhone, random);
	}

	@Override
	public Integer getUserId(String areaCode, String userPhone) {
		logger.info("getUserId : phone={}", areaCode + userPhone);
		User user = userDAO.getUserByUserPhone(areaCode, userPhone);
		if (user != null) {
			if (user.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_AVAILABLE) {
				return user.getUserId();
			} else {
				return 0;
			}
		}
		logger.info("No User!");
		return null;
	}

	@Override
	public UserInfo getUserInfo(Integer userId) {
		logger.info("getUserInfo : userId={}", userId);
		User user = userDAO.getUser(userId);
		UserInfo userInfo = null;
		if (user != null) {
			userInfo = new UserInfo();
			userInfo.setAreaCode(user.getAreaCode());
			userInfo.setPhone(user.getUserPhone());
			userInfo.setName(user.getUserName());
			// 判断是否设置过支付密码
			if (StringUtils.isBlank(user.getUserPayPwd())) {
				userInfo.setPayPwd(false);
			} else {
				userInfo.setPayPwd(true);
			}
			// goldpay
			Bind bind = bindDAO.getBindByUserId(userId);
			userInfo.setGoldpayAcount(bind.getGoldpayAcount());
			userInfo.setGoldpayId(bind.getGoldpayId());
			userInfo.setGoldpayName(bind.getGoldpayName());
			logger.info("UserInfo={}", userInfo.toString());
		} else {
			logger.warn("Can not find the user!!!");
		}
		return userInfo;
	}

	// @Override
	// public Integer login(String areaCode, String userPhone, String
	// userPassword, String ip) {
	// logger.info("login=======");
	// User user = userDAO.getUserByUserPhone(areaCode, userPhone);
	// if (user != null && PasswordUtils.check(userPassword,
	// user.getUserPassword(), user.getPasswordSalt())) {
	// user.setLoginTime(new Date());
	// user.setLoginIp(ip);
	// userDAO.updateUser(user);
	// return user.getUserId();
	// }
	// return null;
	// }

	@Override
	public Integer register(String areaCode, String userPhone, String userName, String userPassword) {
		// 添加用户
		// 随机生成盐值
		String passwordSalt = DigestUtils.md5Hex(MathUtils.randomFixedLengthStr(6));
		logger.info("随机生成盐值===salt={}", passwordSalt);
		logger.info("添加用户");
		Integer userId = userDAO.addUser(
				new User(areaCode, userPhone, userName, PasswordUtils.encrypt(userPassword, passwordSalt), new Date(),
						ServerConsts.USER_TYPE_OF_CUSTOMER, ServerConsts.USER_AVAILABLE_OF_AVAILABLE, passwordSalt));
		// 添加钱包信息
		createWallets4NewUser(userId);
		// 根据UNregistered 更新新用户钱包 将资金从系统帐户划给新用户
		updateWalletsFromUnregistered(userId, areaCode, userPhone);
		return userId;
	}

	@Override
	public boolean testPinCode(String func, String areaCode, String userPhone, String verificationCode) {
		logger.info("校验手机号 {}与验证码{}", areaCode + userPhone, verificationCode);
		if (StringUtils.equals(DigestUtils.md5Hex(verificationCode),
				redisDAO.getValueByKey(func + areaCode + userPhone))) {
			logger.info("***匹配***");
			return true;
		}
		logger.info("***不匹配***");
		return false;
	}

	@Override
	public void updatePassword(Integer userId, String newPassword) {
		logger.info("更新用户{}的密码{}", userId, newPassword);
		User user = userDAO.getUser(userId);
		user.setUserPassword(PasswordUtils.encrypt(newPassword, user.getPasswordSalt()));
		userDAO.updateUser(user);
	}

	@Override
	public void updateUserPayPwd(Integer userId, String userPayPwd) {
		logger.info("更新用户{}的支付密码{}", userId, userPayPwd);
		User user = userDAO.getUser(userId);
		user.setUserPayPwd(PasswordUtils.encrypt(userPayPwd, user.getPasswordSalt()));
		userDAO.updateUser(user);
	}

	/**
	 * 根据UNregistered 更新新用户钱包 将资金从系统帐户划给新用户
	 * 
	 * @param userId
	 * @param areaCode
	 * @param userPhone
	 */
	private void updateWalletsFromUnregistered(Integer userId, String areaCode, String userPhone) {
		logger.info("根据UNregistered 更新新用户钱包 将资金从系统帐户划给新用户");
		Integer systemUserId = userDAO.getSystemUser().getUserId();
		List<Unregistered> unregistereds = unregisteredDAO.getUnregisteredByUserPhone(areaCode, userPhone);
		for (Unregistered unregistered : unregistereds) {
			logger.info("+ {} : {}", unregistered.getCurrency(), unregistered.getAmount());
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
	}

	/**
	 * 为新用户新建钱包
	 * 
	 * @param userId
	 */
	private void createWallets4NewUser(Integer userId) {
		logger.info("为新用户新建钱包");
		List<Currency> currencies = currencyDAO.getCurrencys();
		for (Currency currency : currencies) {
			walletDAO.addwallet(new Wallet(currency, userId, new BigDecimal(0), new Date()));
		}
	}

	@Override
	public AppVersion getAppVersion(String platformType, String updateWay) {
		return appVersionDAO.getAppVersionInfo(platformType, updateWay);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CurrencyInfo> getCurrency() {
		List<Currency> currencies;
		if (redisDAO.getValueByKey("getCurrency") == null) {
			logger.info("getCurrency from db");
			currencies = currencyDAO.getCurrencys();
			redisDAO.saveData("getCurrency", currencies, 30);
		} else {
			logger.info("getCurrency from redis:");
			currencies = (List<Currency>) JsonBinder.getInstance().fromJsonToList(redisDAO.getValueByKey("getCurrency"),
					Currency.class);
		}
		List<CurrencyInfo> list = new ArrayList<>();
		for (Currency currency : currencies) {
			list.add(new CurrencyInfo(currency.getCurrency(), currency.getNameEn(), currency.getNameCn(),
					currency.getNameHk(), currency.getCurrencyImage(), currency.getCurrencyStatus()));
		}
		// logger.info("currency:{}",list);
		return list;
	}

	@Override
	public void updateUser(Integer userId, String loginIp, String pushId, String language) {
		User user = userDAO.getUser(userId);
		user.setLoginIp(loginIp);
		user.setLoginTime(new Date());
		if (StringUtils.isNotBlank(pushId)) {
			user.setPushId(pushId);
		}
		if (StringUtils.isNotBlank(language)) {
			user.setPushTag(LanguageUtils.standard(language));
		}
		userDAO.updateUser(user);
	}

	@Override
	public void updateWallet(Integer userId) {
		logger.info("更新钱包==");
		List<Wallet> wallets = walletDAO.getWalletsByUserId(userId);
		HashMap<Currency, Wallet> mapwallet = new HashMap<Currency, Wallet>();
		for (Wallet wallet : wallets) {
			mapwallet.put(wallet.getCurrency(), wallet);
		}
		// 获取当前可用的货币
		List<Currency> currencies = currencyDAO.getCurrentCurrency();
		for (Currency currency : currencies) {
			if (mapwallet.get(currency) == null) {
				// 没有该货币的钱包，需要新增
				walletDAO.addwallet(new Wallet(currency, userId, new BigDecimal(0), new Date()));
				logger.info("新增 用户{} 的 {} 钱包  ", userId, currency.getCurrency());
			}
		}
	}

	@Override
	public void checkWallet(Integer userId, Currency currency) {
		logger.info("校验 用户{} 是否拥有  {} 钱包", userId, currency.getCurrency());
		Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, currency.getCurrency());
		if (wallet == null) {
			// 没有该货币的钱包，需要新增
			walletDAO.addwallet(new Wallet(currency, userId, new BigDecimal(0), new Date()));
			logger.info("新增 用户{} 的  {} 钱包  ", userId, currency.getCurrency());
		}
	}

}
