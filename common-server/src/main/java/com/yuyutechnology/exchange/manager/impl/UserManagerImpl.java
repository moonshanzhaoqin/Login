package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.InitBinder;

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
import com.yuyutechnology.exchange.push.PushManager;
import com.yuyutechnology.exchange.sms.SmsManager;
import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.LanguageUtils;
import com.yuyutechnology.exchange.utils.MathUtils;
import com.yuyutechnology.exchange.utils.PasswordUtils;
import com.yuyutechnology.exchange.utils.ResourceUtils;

@Service
public class UserManagerImpl implements UserManager {
	public static Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);
	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
	@Autowired
	PushManager pushManager;

	private boolean qaSwitch = false;
	private int verifyTime = 10;
	private int changePhoneTime = 15;
	private String verifyCode = "123456";

	@Override
	public String addfriend(Integer userId, String areaCode, String userPhone) {
		logger.info("Find friend==>");
		User friend = userDAO.getUserByUserPhone(areaCode, userPhone);
		if (friend == null) {
			return ServerConsts.PHONE_NOT_EXIST;
		} else if (friend.getUserId() == userId) {
			return ServerConsts.ADD_FRIEND_OWEN;
		} else if (friendDAO.getFriendByUserIdAndFrindId(userId, friend.getUserId()) != null) {
			return ServerConsts.FRIEND_HAS_ADDED;
		} else {
			friendDAO.addfriend(new Friend(new FriendId(userId, friend.getUserId()), friend, new Date()));
			return ServerConsts.RET_CODE_SUCCESS;
		}
	}

	@Override
	public String bindGoldpay(Integer userId, String goldpayToken) {
		logger.info("getGoldpay==>");
		GoldpayUser goldpayUser = goldpayManager.getGoldpayInfo(goldpayToken);
		if (goldpayUser == null) {
			logger.warn("goldpay account does not exist.");
			return ServerConsts.RET_CODE_FAILUE;
		} else {
			logger.info("goldpayUser=", goldpayUser.toString());
			if (StringUtils.isBlank(goldpayUser.getAreaCode()) || StringUtils.isBlank(goldpayUser.getMobile())) {
				logger.info("Goldpay account does not bind phone number.");
				return ServerConsts.GOLDPAY_PHONE_IS_NOT_EXIST;
			} else {
				Bind bind = bindDAO.getBindByUserId(userId);
				if (bind == null) {
					bind = new Bind(userId, goldpayUser.getId(), goldpayUser.getUsername(), goldpayUser.getAccountNum(),
							goldpayToken);
				} else {
					bind.setGoldpayId(goldpayUser.getId());
					bind.setGoldpayName(goldpayUser.getUsername());
					bind.setGoldpayAcount(goldpayUser.getAccountNum());
					bind.setToken(goldpayToken);
				}
				bindDAO.updateBind(bind);
				return ServerConsts.RET_CODE_SUCCESS;
			}
		}
	}

	@Override
	public void changePhone(Integer userId, String areaCode, String userPhone) {
		logger.info("changePhone {}==>", areaCode + userPhone);
		User user = userDAO.getUser(userId);
		user.setAreaCode(areaCode);
		user.setUserPhone(userPhone);
		userDAO.updateUser(user);
		redisDAO.saveData("changephonetime" + userId, simpleDateFormat.format(new Date()), 300000000);
	}

	@Override
	public boolean checkChangePhoneTime(Integer userId) throws ParseException {
		String timeString = redisDAO.getValueByKey("changephonetime" + userId);
		if (timeString != null
				&& (new Date().getTime() - simpleDateFormat.parse(timeString).getTime()) / (24 * 60 * 60 * 1000) < changePhoneTime) {
			return false;
		}
		return true;
	}

	@Override
	public boolean checkUserPassword(Integer userId, String userPassword) {
		logger.info("Check {}  user's password {} ==>", userId, userPassword);
		User user = userDAO.getUser(userId);
		if (PasswordUtils.check(userPassword, user.getUserPassword(), user.getPasswordSalt())) {
			logger.info("***match***");
			return true;
		}
		logger.info("***Does not match***");
		return false;
	}

	@Override
	public boolean checkUserPayPwd(Integer userId, String userPayPwd) {
		logger.info("Check {}  user's PAY password {} ==>", userId, userPayPwd);
		User user = userDAO.getUser(userId);
		if (PasswordUtils.check(userPayPwd, user.getUserPayPwd(), user.getPasswordSalt())) {
			logger.info("***match***");
			return true;
		}
		logger.info("***Does not match***");
		return false;
	}

	@Override
	public void checkWallet(Integer userId, Currency currency) {
		logger.info("Check whether the user {} has a wallet of {}==>", userId, currency.getCurrency());
		Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, currency.getCurrency());
		if (wallet == null) {
			// 没有该货币的钱包，需要新增
			walletDAO.addwallet(new Wallet(currency, userId, new BigDecimal(0), new Date()));
			logger.info("新增 用户{} 的  {} 钱包  ", userId, currency.getCurrency());
		}
	}

	@Override
	public void clearPinCode(String func, String areaCode, String userPhone) {
		redisDAO.deleteKey(func + areaCode + userPhone);
	}

	/**
	 * 为新用户新建钱包
	 * 
	 * @param userId
	 */
	private void createWallets4NewUser(Integer userId) {
		logger.info("New wallet for newly registered users==>");
		List<Currency> currencies = getCurrentCurrency();
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
					currency.getNameHk(), currency.getCurrencyStatus()));
		}
		// logger.info("currency:{}",list);
		return list;
	}

	@SuppressWarnings("unchecked")
	private List<Currency> getCurrentCurrency() {
		List<Currency> currencies;
		if (redisDAO.getValueByKey("getCurrentCurrency") == null) {
			logger.info("getCurrentCurrency from db");
			currencies = currencyDAO.getCurrentCurrency();
			logger.info("currency={}", currencies);
			redisDAO.saveData("getCurrentCurrency", currencies, 30);
		} else {
			logger.info("getCurrentCurrency from redis:");
			currencies = (List<Currency>) JsonBinder.getInstance()
					.fromJsonToList(redisDAO.getValueByKey("getCurrentCurrency"), Currency.class);
		}
		return currencies;
	}

	@Override
	public List<Friend> getFriends(Integer userId) {
		List<Friend> friends = friendDAO.getFriendsByUserId(userId);
		return friends;
	}

	@Override
	public void getPinCode(String func, String areaCode, String userPhone) {
		// 随机生成六位数
		final String random;
		if (qaSwitch) {
			random = verifyCode;
		} else {
			random = MathUtils.randomFixedLengthStr(6);
		}
		logger.info("getPinCode : phone={}, pincode={}", areaCode + userPhone, random);
		final String md5random = DigestUtils.md5Hex(random);
		// 存入redis userPhone:md5random
		redisDAO.saveData(func + areaCode + userPhone, md5random, verifyTime);
		// 发送验证码
		smsManager.sendSMS4PhoneVerify(areaCode, userPhone, random);
	}

	@PostConstruct
	@Scheduled(cron = "0 1/10 * * * ?")
	@Override
	public void getResource() {
		logger.info("=========init UserManager=========");
		qaSwitch = Boolean.parseBoolean(ResourceUtils.getBundleValue("qa.switch"));
		verifyTime = Integer.parseInt(ResourceUtils.getBundleValue("verify.time"));
		changePhoneTime = Integer.parseInt(ResourceUtils.getBundleValue("changePhone.time"));
		verifyCode = ResourceUtils.getBundleValue("verify.code");
	}

	@Override
	public Integer getUserId(String areaCode, String userPhone) {
		logger.info("getUserId==>phone:{}", areaCode + userPhone);
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
		logger.info("getUserInfo ==>userId:{}", userId);
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
			if (bind != null) {
				userInfo.setGoldpayAcount(bind.getGoldpayAcount());
				userInfo.setGoldpayId(bind.getGoldpayId());
				userInfo.setGoldpayName(bind.getGoldpayName());
			} else {
				userInfo.setGoldpayAcount("");
				userInfo.setGoldpayId("");
				userInfo.setGoldpayName("");
			}
			logger.info("UserInfo={}", userInfo.toString());
		} else {
			logger.warn("Can not find the user!!!");
		}
		return userInfo;
	}

	public void init() {
		getResource();
	}

	@Override
	public void logout(Integer userId) {
		User user = userDAO.getUser(userId);
		logger.info("unbind Tag==>");
		pushManager.unbindPushTag(user);
		user.setPushId(null);
		userDAO.updateUser(user);
	}

	@Override
	public Integer register(String areaCode, String userPhone, String userName, String userPassword, String loginIp,
			String pushId, String language) {
		// 清除验证码
		redisDAO.deleteKey(ServerConsts.PIN_FUNC_REGISTER + areaCode + userPhone);
		// 随机生成盐值
		String passwordSalt = DigestUtils.md5Hex(MathUtils.randomFixedLengthStr(6));
		logger.info("Randomly generated salt values===salt={}", passwordSalt);
		// 添加用户
		Integer userId = userDAO.addUser(new User(areaCode, userPhone, userName,
				PasswordUtils.encrypt(userPassword, passwordSalt), new Date(), ServerConsts.USER_TYPE_OF_CUSTOMER,
				ServerConsts.USER_AVAILABLE_OF_AVAILABLE, passwordSalt, LanguageUtils.standard(language)));
		logger.info("Add user complete");
		// 添加钱包信息
		createWallets4NewUser(userId);
		// 根据UNregistered 更新新用户钱包 将资金从系统帐户划给新用户
		updateWalletsFromUnregistered(userId, areaCode, userPhone);
		return userId;
	}

	@Override
	public void switchLanguage(Integer userId, String language) {
		User user = userDAO.getUser(userId);
		if (!user.getPushTag().equals(LanguageUtils.standard(language)) && StringUtils.isNotBlank(user.getPushId())) {
			// 语言不一致，解绑Tag
			logger.info("Language inconsistency, unbind Tag==>,");
			// pushManager.unbindPushTag(user);
		}
		user.setPushTag(LanguageUtils.standard(language));
		userDAO.updateUser(user);
		if (StringUtils.isNotBlank(user.getPushId()) && user.getPushTag() != null) {
			// 绑定Tag
			logger.info("bind Tag==>");
			// pushManager.bindPushTag(user);
		}
	}

	@Override
	public boolean testPinCode(String func, String areaCode, String userPhone, String verificationCode) {
		logger.info("Check phone number {} and verify code {}", areaCode + userPhone, verificationCode);
		if (StringUtils.equals(DigestUtils.md5Hex(verificationCode),
				redisDAO.getValueByKey(func + areaCode + userPhone))) {
			logger.info("***match***");
			return true;
		}
		logger.info("***Does not match***");
		return false;
	}

	@Override
	public void updatePassword(Integer userId, String newPassword) {
		logger.info("Update user {} password {}", userId, newPassword);
		User user = userDAO.getUser(userId);
		user.setUserPassword(PasswordUtils.encrypt(newPassword, user.getPasswordSalt()));
		userDAO.updateUser(user);
	}

	public void updateUser(Integer userId, String loginIp, String pushId, String language) {
		logger.info("Update user login information==>");
		User user = userDAO.getUser(userId);
		user.setLoginIp(loginIp);
		user.setLoginTime(new Date());
		if (pushId != null && !pushId.equals(user.getPushId())) {
			// 推送消息：设备已下线
			logger.info("Push message: device offline==>");
			pushManager.push4Offline(user);
		}
		if (!user.getPushTag().equals(LanguageUtils.standard(language))) {
			// 语言不一致，解绑Tag
			logger.info("Language inconsistency, unbind Tag==>");
			pushManager.unbindPushTag(user);
		}
		user.setPushId(pushId);
		user.setPushTag(LanguageUtils.standard(language));
		userDAO.updateUser(user);
		// 绑定Tag
		logger.info("bind Tag==>");
		pushManager.bindPushTag(user);
	}

	@Override
	public void updateUserName(Integer userId, String newUserName) {
		User user = userDAO.getUser(userId);
		user.setUserName(newUserName);
		userDAO.updateUser(user);
	}

	@Override
	public void updateUserPayPwd(Integer userId, String userPayPwd) {
		logger.info("Update user {} pay password {}", userId, userPayPwd);
		User user = userDAO.getUser(userId);
		user.setUserPayPwd(PasswordUtils.encrypt(userPayPwd, user.getPasswordSalt()));
		userDAO.updateUser(user);
	}

	@Override
	public void updateWallet(Integer userId) {
		logger.info("Update Wallet==>");
		List<Wallet> wallets = walletDAO.getWalletsByUserId(userId);
		HashMap<String, Wallet> mapwallet = new HashMap<String, Wallet>();
		for (Wallet wallet : wallets) {
			mapwallet.put(wallet.getCurrency().getCurrency(), wallet);
		}
		// logger.info("mapwallet",mapwallet);
		// 获取当前可用的货币
		List<Currency> currencies = getCurrentCurrency();
		for (Currency currency : currencies) {
			// logger.info("{}",currency.getCurrency());
			if (mapwallet.get(currency.getCurrency()) == null) {
				// 没有该货币的钱包，需要新增
				walletDAO.addwallet(new Wallet(currency, userId, new BigDecimal(0), new Date()));
				logger.info("Added {}wallet to user {}", currency.getCurrency(), userId);
			}
		}
	}

	/**
	 * 根据UNregistered 更新新用户钱包 将资金从系统帐户划给新用户
	 * 
	 * @param userId
	 * @param areaCode
	 * @param userPhone
	 */
	private void updateWalletsFromUnregistered(Integer userId, String areaCode, String userPhone) {
		logger.info(
				"Update wallets according to Unregistered. Assign funds from system accounts to  newly registered user==>");
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
			walletSeqDAO.addWalletSeq4Transaction(systemUserId, userId, ServerConsts.TRANSFER_TYPE_TRANSACTION,
					unregistered.getTransferId(), unregistered.getCurrency(), unregistered.getAmount());
			// 更改Transfer状态
			transferDAO.updateTransferStatusAndUserTo(unregistered.getTransferId(),
					ServerConsts.TRANSFER_STATUS_OF_COMPLETED, userId);
			// 更改unregistered状态
			unregistered.setUnregisteredStatus(ServerConsts.UNREGISTERED_STATUS_OF_COMPLETED);
			unregisteredDAO.updateUnregistered(unregistered);
		}
	}
}
