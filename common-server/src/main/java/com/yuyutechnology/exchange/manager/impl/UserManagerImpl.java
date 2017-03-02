package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.AppVersionDAO;
import com.yuyutechnology.exchange.dao.BindDAO;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.FriendDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UnregisteredDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.UserDeviceDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.dao.WalletSeqDAO;
import com.yuyutechnology.exchange.dto.CheckPwdResult;
import com.yuyutechnology.exchange.dto.UserInfo;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.enums.UserConfigKeyEnum;
import com.yuyutechnology.exchange.goldpay.GoldpayManager;
import com.yuyutechnology.exchange.goldpay.GoldpayUser;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Bind;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.Friend;
import com.yuyutechnology.exchange.pojo.FriendId;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.Unregistered;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.UserConfig;
import com.yuyutechnology.exchange.pojo.UserDevice;
import com.yuyutechnology.exchange.pojo.UserDeviceId;
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
	public static Logger logger = LogManager.getLogger(UserManagerImpl.class);
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
	UserDeviceDAO userDeviceDAO;
	@Autowired
	SmsManager smsManager;
	@Autowired
	GoldpayManager goldpayManager;
	@Autowired
	PushManager pushManager;
	@Autowired
	CommonManager commonManager;
	@Autowired
	ConfigManager configManager;

	@Override
	public String addfriend(Integer userId, String areaCode, String userPhone) {
		logger.info("Find friend==>");
		User friend = userDAO.getUserByUserPhone(areaCode, userPhone);
		if (friend == null) {
			return RetCodeConsts.PHONE_NOT_EXIST;
		} else if (friend.getUserId() == userId) {
			return RetCodeConsts.PHONE_ID_YOUR_OWEN;
		} else if (friendDAO.getFriendByUserIdAndFrindId(userId, friend.getUserId()) != null) {
			return RetCodeConsts.FRIEND_HAS_ADDED;
		} else {
			friendDAO.addFriend(new Friend(new FriendId(userId, friend.getUserId()), friend, new Date()));
			return RetCodeConsts.RET_CODE_SUCCESS;
		}
	}

	@Override
	public String bindGoldpay(Integer userId, String goldpayToken) {
		logger.info("get Goldpay==>");
		GoldpayUser goldpayUser = goldpayManager.getGoldpayInfo(goldpayToken);
		if (goldpayUser == null) {
			logger.warn("goldpay account does not exist.");
			return RetCodeConsts.RET_CODE_FAILUE;
		} else {
			logger.info("goldpayUser = ", goldpayUser.toString());
			if (StringUtils.isBlank(goldpayUser.getAreaCode()) || StringUtils.isBlank(goldpayUser.getMobile())) {
				logger.info("Goldpay account does not bind phone number.");
				return RetCodeConsts.GOLDPAY_PHONE_IS_NOT_EXIST;
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
				return RetCodeConsts.RET_CODE_SUCCESS;
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
		redisDAO.saveData("changephonetime" + userId, new Date().getTime());
	}

	@Override
	public long checkChangePhoneTime(Integer userId) throws ParseException {
		String timeString = redisDAO.getValueByKey("changephonetime" + userId);
		if (timeString != null) {
			Calendar time = Calendar.getInstance();
			time.setTimeInMillis(Long.valueOf(timeString).longValue());
			time.add(Calendar.DATE, configManager.getConfigLongValue(ConfigKeyEnum.CHANGEPHONETIME, 10l).intValue());
			logger.info("checkChangePhoneTime ==>{}", time.getTime());
			return time.getTime().getTime();
		}
		logger.info("checkChangePhoneTime ==>{}", new Date());
		return new Date().getTime();

	}

	@Override
	public CheckPwdResult checkLoginPassword(Integer userId, String userPassword) {
		logger.info("Check {}  user's password {} ==>", userId, userPassword);
		CheckPwdResult result = new CheckPwdResult();
		User user = userDAO.getUser(userId);
		// 判断冻结
		if (user.getLoginAvailable() == ServerConsts.LOGIN_AVAILABLE_OF_UNAVAILABLE) {
			String timeString = redisDAO.getValueByKey(ServerConsts.LOGIN_FREEZE + userId);
			if (timeString != null) {
				Calendar time = Calendar.getInstance();
				time.setTimeInMillis(Long.valueOf(timeString).longValue());
				time.add(Calendar.HOUR_OF_DAY,
						configManager.getConfigLongValue(ConfigKeyEnum.LOGIN_UNAVAILIABLE_TIME, 24l).intValue());

				if (new Date().before(time.getTime())) {
					logger.info("***Login is frozen!***");
					result.setStatus(ServerConsts.CHECKPWD_STATUS_FREEZE);
					result.setInfo(time.getTime().getTime());
					return result;
				}
				redisDAO.deleteData(ServerConsts.LOGIN_FREEZE + userId);
			}

			user.setLoginAvailable(ServerConsts.LOGIN_AVAILABLE_OF_AVAILABLE);
			userDAO.updateUser(user);
		}
		// 校验密码
		if (PasswordUtils.check(userPassword, user.getUserPassword(), user.getPasswordSalt())) {
			logger.info("***match***");
			redisDAO.deleteData(ServerConsts.WRONG_PASSWORD + userId);
			redisDAO.deleteData(ServerConsts.LOGIN_FREEZE + userId);
			result.setStatus(ServerConsts.CHECKPWD_STATUS_CORRECT);
			return result;
		} else {
			long t = 1;
			String times = redisDAO.getValueByKey(ServerConsts.WRONG_PASSWORD + userId);
			if (times != null) {
				t += Long.valueOf(times).longValue();
			}
			if (t >= configManager.getConfigLongValue(ConfigKeyEnum.WRONG_PASSWORD_FREQUENCY, 3l).longValue()) {
				// 输出超过次数，冻结
				logger.info("***Does not match, login is frozen!***");
				redisDAO.saveData(ServerConsts.LOGIN_FREEZE + userId, new Date().getTime());
				user.setLoginAvailable(ServerConsts.LOGIN_AVAILABLE_OF_UNAVAILABLE);
				userDAO.updateUser(user);
				// 次数清零
				redisDAO.deleteData(ServerConsts.WRONG_PASSWORD + userId);
				// 计算到期时间
				Calendar time = Calendar.getInstance();
				time.add(Calendar.HOUR_OF_DAY,
						configManager.getConfigLongValue(ConfigKeyEnum.LOGIN_UNAVAILIABLE_TIME, 24l).intValue());

				result.setStatus(ServerConsts.CHECKPWD_STATUS_FREEZE);
				result.setInfo(time.getTime().getTime());
				return result;
			}
			// 每日24点错误次数清零
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 24);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.MILLISECOND, 0);
			// 记录错误次数
			redisDAO.saveData(ServerConsts.WRONG_PASSWORD + userId, t, cal.getTime());
			logger.info("***Does not match,{}***", t);
			result.setStatus(ServerConsts.CHECKPWD_STATUS_INCORRECT);
			result.setInfo(
					configManager.getConfigLongValue(ConfigKeyEnum.WRONG_PASSWORD_FREQUENCY, 3l).longValue() - t);
			return result;
		}
	}

	@Override
	public CheckPwdResult checkPayPassword(Integer userId, String userPayPwd) {
		logger.info("Check {}  user's Pay  password {} ==>", userId, userPayPwd);
		CheckPwdResult result = new CheckPwdResult();

		User user = userDAO.getUser(userId);
		if (user.getPayAvailable() == ServerConsts.PAY_AVAILABLE_OF_UNAVAILABLE) {
			String timeString = redisDAO.getValueByKey(ServerConsts.PAY_FREEZE + userId);
			if (timeString != null) {
				Calendar time = Calendar.getInstance();
				time.setTimeInMillis(Long.valueOf(timeString).longValue());
				time.add(Calendar.HOUR_OF_DAY,
						configManager.getConfigLongValue(ConfigKeyEnum.PAY_UNAVAILIABLE_TIME, 24l).intValue());

				if (new Date().before(time.getTime())) {
					logger.info("***Pay is frozen!***");
					result.setStatus(ServerConsts.CHECKPWD_STATUS_FREEZE);
					result.setInfo(time.getTime().getTime());
					return result;
				}
				redisDAO.deleteData(ServerConsts.PAY_FREEZE + userId);
			}
			user.setPayAvailable(ServerConsts.PAY_AVAILABLE_OF_AVAILABLE);
			userDAO.updateUser(user);
		}

		if (PasswordUtils.check(userPayPwd, user.getUserPayPwd(), user.getPasswordSalt())) {
			logger.info("***match***");

			redisDAO.deleteData(ServerConsts.WRONG_PAYPWD + userId);
			result.setStatus(ServerConsts.CHECKPWD_STATUS_CORRECT);
			return result;
		} else {

			long t = 1;
			String times = redisDAO.getValueByKey(ServerConsts.WRONG_PAYPWD + userId);
			if (times != null) {
				t += Long.valueOf(times).longValue();
			}
			if (t >= configManager.getConfigLongValue(ConfigKeyEnum.WRONG_PAYPWD_FREQUENCY, 3l).longValue()) {
				// 输出超过次数，冻结
				logger.info("***Does not match, pay is frozen!***");
				redisDAO.saveData(ServerConsts.PAY_FREEZE + userId, new Date().getTime());
				user.setPayAvailable(ServerConsts.PAY_AVAILABLE_OF_UNAVAILABLE);
				userDAO.updateUser(user);
				// 次数清零
				redisDAO.deleteData(ServerConsts.WRONG_PAYPWD + userId);
				// 计算到期时间
				Calendar time = Calendar.getInstance();
				time.add(Calendar.HOUR_OF_DAY,
						configManager.getConfigLongValue(ConfigKeyEnum.PAY_UNAVAILIABLE_TIME, 24l).intValue());

				result.setStatus(ServerConsts.CHECKPWD_STATUS_FREEZE);
				result.setInfo(time.getTime().getTime());
				return result;
			}
			// 每日24点错误次数清零
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 24);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.MILLISECOND, 0);
			// 记录错误次数
			redisDAO.saveData(ServerConsts.WRONG_PAYPWD + userId, t, cal.getTime());
			logger.info("***Does not match,{}***", t);
			result.setStatus(ServerConsts.CHECKPWD_STATUS_INCORRECT);
			result.setInfo(configManager.getConfigLongValue(ConfigKeyEnum.WRONG_PAYPWD_FREQUENCY, 3l).longValue() - t);
			return result;
		}
	}

	@Override
	public String checkGoldpay(Integer userId, String goldpayName, String goldpayPassword) {
		logger.info("Check {}  user's Goldpay password {} ==>", userId, goldpayPassword);
		GoldpayUser goldpayUser = goldpayManager.checkGoldpay(goldpayName, goldpayPassword);
		if (goldpayUser == null) {
			logger.info("goldpayName goldpayPassword not match");
			return RetCodeConsts.GOLDPAY_IS_INCORRECT;
		}
		Bind bind = bindDAO.getBindByUserId(userId);
		if (bind == null) {
			logger.info("goldpay not bind");
			return RetCodeConsts.GOLDPAY_NOT_BIND;
		}
		if (!goldpayUser.getUsername().equals(bind.getGoldpayName())) {
			logger.info("goldpay not match bind");
			return RetCodeConsts.GOLDPAY_NOT_MATCH_BIND;
		}
		return RetCodeConsts.RET_CODE_SUCCESS;
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
		redisDAO.deleteData(func + areaCode + userPhone);
	}

	/**
	 * 为新用户新建钱包
	 * 
	 * @param userId
	 */
	private void createWallets4NewUser(Integer userId) {
		logger.info("New wallet for newly registered users==>");
		List<Currency> currencies = commonManager.getCurrentCurrencies();
		for (Currency currency : currencies) {
			walletDAO.addwallet(new Wallet(currency, userId, new BigDecimal(0), new Date()));
		}
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
		if (ResourceUtils.getBundleValue4Boolean("qa.switch")) {
			random = ResourceUtils.getBundleValue4String("verify.code", "654321");
		} else {
			random = MathUtils.randomFixedLengthStr(6);
		}
		logger.info("getPinCode : phone={}, pincode={}", areaCode + userPhone, random);
		final String md5random = DigestUtils.md5Hex(random);
		// 存入redis userPhone:md5random
		redisDAO.saveData(func + areaCode + userPhone, md5random,
				configManager.getConfigLongValue(ConfigKeyEnum.VERIFYTIME, 10l).intValue(), TimeUnit.MINUTES);
		// 发送验证码
		smsManager.sendSMS4PhoneVerify(areaCode, userPhone, random);
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
			userInfo.setUserId(user.getUserId());
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
				userInfo.setGoldpayName(MathUtils.hideString(bind.getGoldpayName()));
			} else {
				userInfo.setGoldpayName("");
			}
			logger.info("UserInfo={}", userInfo.toString());
		} else {
			logger.warn("Can not find the user!!!");
		}
		return userInfo;
	}

	@Override
	public void logout(Integer userId) {
		if (userId != 0) {
			User user = userDAO.getUser(userId);
			logger.info("unbind Tag==>");
			pushManager.unbindPushTag(user.getPushId(), user.getPushTag());
			user.setPushId(null);
			userDAO.updateUser(user);
		}
	}

	@Override
	public Integer register(String areaCode, String userPhone, String userName, String userPassword, String loginIp,
			String pushId, String language) {
		// 清除验证码
		redisDAO.deleteData(ServerConsts.PIN_FUNC_REGISTER + areaCode + userPhone);
		// 随机生成盐值
		String passwordSalt = DigestUtils.md5Hex(MathUtils.randomFixedLengthStr(6));
		logger.info("Randomly generated salt values===salt={}", passwordSalt);
		// 添加用户
		Integer userId = userDAO.addUser(new User(areaCode, userPhone, userName,
				PasswordUtils.encrypt(userPassword, passwordSalt), new Date(), ServerConsts.USER_TYPE_OF_CUSTOMER,
				ServerConsts.USER_AVAILABLE_OF_AVAILABLE, ServerConsts.LOGIN_AVAILABLE_OF_AVAILABLE,
				ServerConsts.PAY_AVAILABLE_OF_AVAILABLE, passwordSalt, LanguageUtils.standard(language)));
		logger.info("Add user complete!");
		redisDAO.saveData("changephonetime" + userId, new Date().getTime());
		// 添加钱包信息
		createWallets4NewUser(userId);
		// 根据UNregistered 更新新用户钱包 将资金从系统帐户划给新用户
		updateWalletsFromUnregistered(userId, areaCode, userPhone);
		return userId;
	}

	@Override
	public void switchLanguage(Integer userId, String language) {
		User user = userDAO.getUser(userId);
		if (!user.getPushTag().equals(LanguageUtils.standard(language))) {
			// 语言不一致，解绑Tag
			logger.info("Language inconsistency, unbind Tag==>,");
			pushManager.unbindPushTag(user.getPushId(), user.getPushTag());
		}
		user.setPushTag(LanguageUtils.standard(language));
		userDAO.updateUser(user);
		// 绑定Tag
		logger.info("bind Tag==>");
		pushManager.bindPushTag(user.getPushId(), user.getPushTag());
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
		redisDAO.deleteData(ServerConsts.WRONG_PASSWORD + userId);
	}

	public void updateUser(Integer userId, String loginIp, String pushId, String language) {
		logger.info("Update user login information==>");
		User user = userDAO.getUser(userId);
		user.setLoginIp(loginIp);
		user.setLoginTime(new Date());
		if (StringUtils.isNotBlank(pushId) && !pushId.equals(user.getPushId())) {
			// 更换设备 推送消息：设备已下线
			logger.info("Push message: device offline==> oldPushId : {} , newPushId : {} ",
					new Object[] { user.getPushId(), pushId });
			pushManager.push4Offline(user.getPushId(), user.getPushTag(), String.valueOf(new Date().getTime()));
		}
		if (!user.getPushTag().equals(LanguageUtils.standard(language))) {
			// 语言不一致，解绑Tag
			logger.info("Language inconsistency, unbind Tag==>");
			pushManager.unbindPushTag(user.getPushId(), user.getPushTag());
			user.setPushTag(LanguageUtils.standard(language));
		}
		if (StringUtils.isNotBlank(pushId)) {
			user.setPushId(pushId);
		}
		userDAO.updateUser(user);
		// 绑定Tag
		logger.info("bind Tag==>");
		pushManager.bindPushTag(user.getPushId(), user.getPushTag());
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
		List<Currency> currencies = commonManager.getCurrentCurrencies();
		for (Currency currency : currencies) {
			// logger.info("{}",currency.getCurrency());
			if (mapwallet.get(currency.getCurrency()) == null) {
				// 没有该货币的钱包，需要新增
				walletDAO.addwallet(new Wallet(currency, userId, BigDecimal.ZERO, new Date()));
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

			Transfer payerTransfer = transferDAO.getTransferById(unregistered.getTransferId());

			if (payerTransfer == null) {
				logger.warn("Did not find the corresponding transfer information");
				return;
			}

			User payer = userDAO.getUser(payerTransfer.getUserFrom());

			// 系统账号扣款
			walletDAO.updateWalletByUserIdAndCurrency(systemUserId, unregistered.getCurrency(),
					unregistered.getAmount(), "-");
			// 用户加款
			walletDAO.updateWalletByUserIdAndCurrency(userId, unregistered.getCurrency(), unregistered.getAmount(),
					"+");

			// 生成TransId
			String transferId = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_TRANSACTION);
			Transfer transfer = new Transfer();
			transfer.setTransferId(transferId);
			transfer.setUserFrom(systemUserId);
			transfer.setUserTo(userId);
			transfer.setAreaCode(payer.getAreaCode());
			transfer.setPhone(payer.getUserPhone());
			transfer.setCurrency(unregistered.getCurrency());
			transfer.setTransferAmount(unregistered.getAmount());
			transfer.setCreateTime(new Date());
			transfer.setFinishTime(new Date());
			transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
			transfer.setTransferType(ServerConsts.TRANSFER_TYPE_TRANSACTION);
			transfer.setTransferComment(unregistered.getTransferId());
			transfer.setNoticeId(0);

			transferDAO.addTransfer(transfer);

			// 增加seq记录
			walletSeqDAO.addWalletSeq4Transaction(systemUserId, userId, ServerConsts.TRANSFER_TYPE_TRANSACTION,
					transferId, unregistered.getCurrency(), unregistered.getAmount());

			// 更改unregistered状态
			unregistered.setUnregisteredStatus(ServerConsts.UNREGISTERED_STATUS_OF_COMPLETED);
			unregisteredDAO.updateUnregistered(unregistered);
		}
	}

	@Override
	public String deleteFriend(Integer userId, String areaCode, String phone) {
		logger.info("Find friend==>");
		User friend = userDAO.getUserByUserPhone(areaCode, phone);
		if (friend == null) {
			return RetCodeConsts.PHONE_NOT_EXIST;
		} else if (friend.getUserId() == userId) {
			return RetCodeConsts.PHONE_ID_YOUR_OWEN;
		} else {
			Friend friend2 = friendDAO.getFriendByUserIdAndFrindId(userId, friend.getUserId());
			if (friend2 == null) {
				return RetCodeConsts.PHONE_IS_NOT_FRIEND;
			} else {
				friendDAO.deleteFriend(friend2);
				return RetCodeConsts.RET_CODE_SUCCESS;
			}
		}
	}

	@Override
	public User getSystemUser() {
		return userDAO.getSystemUser();
	}

	@Override
	public List<User> getUserList() {
		return userDAO.getUserList();
	}

	@Override
	public void userFreeze(Integer userId, int userAvailable) {
		User user = userDAO.getUser(userId);
		if (user == null) {
			logger.warn("{} is not exist!!!", userId);
		} else {
			user.setUserAvailable(userAvailable);
			userDAO.updateUser(user);
		}
	}

	@Override
	public String getUserConfigAndUpdate(Integer userId, UserConfigKeyEnum key, String value) {
		String returnValue = getUserConfig(userId, key);
		if (key != null && StringUtils.isNotBlank(value)) {
			saveUserConfig(userId, key, value);
		}
		return returnValue;
	}

	private String getUserConfig(Integer userId, UserConfigKeyEnum key) {
		UserConfig userConfig = userDAO.getUserConfig(userId);
		if (userConfig != null && StringUtils.isNotBlank(userConfig.getUserConfigValue())) {
			@SuppressWarnings("unchecked")
			Map<String, String> config = JsonBinder.getInstance().fromJson(userConfig.getUserConfigValue(),
					HashMap.class);
			return config.get(key.ordinal() + "") == null ? "" : config.get(key.ordinal() + "");
		}
		return "";
	}

	private void saveUserConfig(Integer userId, UserConfigKeyEnum key, String value) {
		UserConfig userConfig = userDAO.getUserConfig(userId);
		if (userConfig != null && StringUtils.isNotBlank(userConfig.getUserConfigValue())) {
			@SuppressWarnings("unchecked")
			Map<String, String> config = JsonBinder.getInstance().fromJson(userConfig.getUserConfigValue(),
					HashMap.class);
			config.put(key.ordinal() + "", value);
			userConfig.setUserConfigValue(JsonBinder.getInstance().toJson(config));
		} else {
			userConfig = new UserConfig();
			userConfig.setUserId(userId);
			Map<String, String> config = new HashMap<String, String>();
			config.put(key.ordinal() + "", value);
			userConfig.setUserConfigValue(JsonBinder.getInstance().toJson(config));
		}
		userDAO.saveUserConfig(userConfig);
	}

	@Override
	public boolean isNewDevice(Integer userId, String deviceId) {
		if (userDeviceDAO.getUserDeviceByUserIdAndDeviceId(userId, deviceId) == null) {
			return true;
		}
		return false;
	}

	@Override
	public void addDevice(Integer userId, String deviceId, String deviceName) {
		userDeviceDAO.addUserDevice(new UserDevice(new UserDeviceId(userId, deviceId), deviceName));
	}

	// @Override
	// public boolean isLoginAvailiable(Integer userId) {
	// User user = userDAO.getUser(userId);
	// if (user.getLoginAvailable() ==
	// ServerConsts.LOGIN_AVAILABLE_OF_UNAVAILABLE) {
	//
	// }
	// return false;
	// }

	@Override
	public boolean isUserPayPwdEqualsOld(Integer userId, String newUserPayPwd) {
		logger.info("is UserPayPwd Equals Old==>");
		User user = userDAO.getUser(userId);
		if (PasswordUtils.check(newUserPayPwd, user.getUserPayPwd(), user.getPasswordSalt())) {
			logger.info("UserPayPwd Equals Old");
			return true;
		}
		logger.info("UserPayPwd not Equals Old");
		return false;
	}
}