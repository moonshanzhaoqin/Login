package com.yuyutechnology.exchange.manager.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
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
import com.yuyutechnology.exchange.dao.BindDAO;
import com.yuyutechnology.exchange.dao.CrmUserInfoDAO;
import com.yuyutechnology.exchange.dao.FriendDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dao.TransDetailsDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UnregisteredDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.UserDeviceDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.dto.CheckPwdResult;
import com.yuyutechnology.exchange.dto.FriendDTO;
import com.yuyutechnology.exchange.dto.FriendInitial;
import com.yuyutechnology.exchange.dto.UserDTO;
import com.yuyutechnology.exchange.dto.UserInfo;
import com.yuyutechnology.exchange.dto.UserInfo4Transfer;
import com.yuyutechnology.exchange.enums.CheckPWDStatus;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.enums.UserConfigKeyEnum;
import com.yuyutechnology.exchange.goldpay.msg.GoldpayUserDTO;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;
import com.yuyutechnology.exchange.manager.TransDetailsManager;
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
import com.yuyutechnology.exchange.sms.SendMessageResponse;
import com.yuyutechnology.exchange.sms.SmsManager;
import com.yuyutechnology.exchange.util.DateFormatUtils;
import com.yuyutechnology.exchange.util.JsonBinder;
import com.yuyutechnology.exchange.util.LanguageUtils;
import com.yuyutechnology.exchange.util.LanguageUtils.Language;
import com.yuyutechnology.exchange.util.MathUtils;
import com.yuyutechnology.exchange.util.PasswordUtils;
import com.yuyutechnology.exchange.util.PinyinUtils;
import com.yuyutechnology.exchange.util.ResourceUtils;
import com.yuyutechnology.exchange.util.S3Utils;
import com.yuyutechnology.exchange.util.UidUtils;

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
	BindDAO bindDAO;
	@Autowired
	RedisDAO redisDAO;
	@Autowired
	UnregisteredDAO unregisteredDAO;
	@Autowired
	FriendDAO friendDAO;
	@Autowired
	UserDeviceDAO userDeviceDAO;
	@Autowired
	CrmUserInfoDAO crmUserInfoDAO;
	@Autowired
	TransDetailsDAO transDetailsDAO;
	@Autowired
	SmsManager smsManager;
	@Autowired
	PushManager pushManager;
	@Autowired
	CommonManager commonManager;
	@Autowired
	ConfigManager configManager;
	@Autowired
	TransDetailsManager transDetailsManager;
	@Autowired
	GoldpayTrans4MergeManager goldpayTrans4MergeManager;

	@Override
	public String addfriend(Integer userId, String areaCode, String userPhone) {
		logger.info("{} add friend {} -->", userId, areaCode + userPhone);
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

	/**
	 * 创建新的Goldpay 并绑定
	 * 
	 * @param userId
	 */
	private void bindGoldpay(String areaCode, String userPhone, String userName, Integer userId) {
		/* 创建Goldpay账号 */
		GoldpayUserDTO goldpayUser = goldpayTrans4MergeManager.createGoldpay(areaCode, userPhone, userName, true);
		if (goldpayUser != null) {
			bindDAO.updateBind(
					new Bind(userId, goldpayUser.getId() + "", goldpayUser.getUsername(), goldpayUser.getAccountNum()));
		} else {
			throw new RuntimeException("goldpay user wrong!");
		}

	}

	@Override
	public void changePhone(Integer userId, String areaCode, String userPhone) {
		logger.info("{} changePhone to {} ==>", userId, areaCode + userPhone);
		User user = userDAO.getUser(userId);
		user.setAreaCode(areaCode);
		user.setUserPhone(userPhone);
		userDAO.updateUser(user);
		redisDAO.saveData("changephonetime" + userId, new Date().getTime());
		redisDAO.expireAtData("changephonetime" + userId, DateFormatUtils.getIntervalMinute(new Date(), 1));
		/* 根据Unregistered表 更新新用户钱包 将资金从系统帐户划给新用户 */
		updateWalletsFromUnregistered(userId, areaCode, userPhone, user.getUserName());
	}

	@Override
	public long getChangePhoneTime(Integer userId) throws ParseException {
		String timeString = redisDAO.getValueByKey("changephonetime" + userId);
		if (timeString != null) {
			Calendar time = Calendar.getInstance();
			time.setTimeInMillis(Long.valueOf(timeString).longValue());
			time.add(Calendar.DATE, configManager.getConfigLongValue(ConfigKeyEnum.CHANGEPHONETIME, 10l).intValue());
			logger.info("ChangePhoneTime : {}", time.getTime());
			return time.getTime().getTime();
		}
		logger.info("ChangePhoneTime : {}", new Date());
		return new Date().getTime();

	}

	@Override
	public CheckPwdResult checkLoginPassword(Integer userId, String userPassword) {
		logger.info("Check {}  user's password {} ==>", userId, userPassword);
		CheckPwdResult result = new CheckPwdResult();
		User user = userDAO.getUser(userId);

		/* 判断冻结 */
		if (user.getLoginAvailable() == ServerConsts.LOGIN_AVAILABLE_OF_UNAVAILABLE) {
			String timeString = redisDAO.getValueByKey(ServerConsts.LOGIN_FREEZE + userId);
			if (timeString != null) {
				Calendar time = Calendar.getInstance();
				time.setTimeInMillis(Long.valueOf(timeString).longValue());
				time.add(Calendar.HOUR_OF_DAY,
						configManager.getConfigLongValue(ConfigKeyEnum.LOGIN_UNAVAILIABLE_TIME, 24L).intValue());

				if (new Date().before(time.getTime())) {
					logger.info("***Login is frozen!***");
					result.setStatus(CheckPWDStatus.FREEZE);
					result.setInfo(time.getTime().getTime());
					return result;
				}
				redisDAO.deleteData(ServerConsts.LOGIN_FREEZE + userId);
			}

			user.setLoginAvailable(ServerConsts.LOGIN_AVAILABLE_OF_AVAILABLE);
			userDAO.updateUser(user);
		}

		/* 校验密码 */
		if (PasswordUtils.check(userPassword, user.getUserPassword(), user.getPasswordSalt())) {
			logger.info("***match***");
			redisDAO.deleteData(ServerConsts.WRONG_PASSWORD + userId);
			redisDAO.deleteData(ServerConsts.LOGIN_FREEZE + userId);
			result.setStatus(CheckPWDStatus.CORRECT);
			return result;
		} else {
			long t = 1;
			String times = redisDAO.getValueByKey(ServerConsts.WRONG_PASSWORD + userId);
			if (times != null) {
				t += Long.valueOf(times).longValue();
			}
			if (t >= configManager.getConfigLongValue(ConfigKeyEnum.WRONG_PASSWORD_FREQUENCY, 3L).longValue()) {
				/* 输出超过次数，冻结 */
				logger.info("***Does not match, login is frozen!***");
				redisDAO.saveData(ServerConsts.LOGIN_FREEZE + userId, new Date().getTime());
				user.setLoginAvailable(ServerConsts.LOGIN_AVAILABLE_OF_UNAVAILABLE);
				userDAO.updateUser(user);
				/* 次数清零 */
				redisDAO.deleteData(ServerConsts.WRONG_PASSWORD + userId);
				/* 计算到期时间 */
				Calendar time = Calendar.getInstance();
				time.add(Calendar.HOUR_OF_DAY,
						configManager.getConfigLongValue(ConfigKeyEnum.LOGIN_UNAVAILIABLE_TIME, 24l).intValue());

				result.setStatus(CheckPWDStatus.FREEZE);
				result.setInfo(time.getTime().getTime());
				return result;
			}
			/* 每日24点错误次数清零 */
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 24);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.MILLISECOND, 0);
			/* 记录错误次数 */
			redisDAO.saveData(ServerConsts.WRONG_PASSWORD + userId, t, cal.getTime());
			logger.info("***Does not match,{}***", t);
			result.setStatus(CheckPWDStatus.INCORRECT);
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
					result.setStatus(CheckPWDStatus.FREEZE);
					result.setInfo(time.getTime().getTime());
					return result;
				}
				redisDAO.deleteData(ServerConsts.PAY_FREEZE + userId);
			}
			user.setPayAvailable(ServerConsts.PAY_AVAILABLE_OF_AVAILABLE);
			userDAO.updateUser(user);
		}

		if (PasswordUtils.check(userPayPwd, user.getUserPayPwd(), user.getPasswordSalt())
				|| (StringUtils.isNotBlank(user.getUserPayToken()) && userPayPwd.equals(user.getUserPayToken()))) {
			logger.info("***match***");
			redisDAO.deleteData(ServerConsts.WRONG_PAYPWD + userId);
			result.setStatus(CheckPWDStatus.CORRECT);
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

				result.setStatus(CheckPWDStatus.FREEZE);
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
			result.setStatus(CheckPWDStatus.INCORRECT);
			result.setInfo(configManager.getConfigLongValue(ConfigKeyEnum.WRONG_PAYPWD_FREQUENCY, 3l).longValue() - t);
			return result;
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
		logger.info("New wallets for newly registered user {}==>", userId);
		List<Currency> currencies = commonManager.getCurrentCurrencies();
		for (Currency currency : currencies) {
			walletDAO.addwallet(new Wallet(currency, userId, BigDecimal.ZERO, new Date(), 0));
		}
	}

	@Override
	public List<FriendInitial> getFriends(Integer userId) {
		List<FriendDTO> friendDTOs = new ArrayList<>();
		List<FriendInitial> friendInitials = new ArrayList<>();
		List<Friend> friends = friendDAO.getFriendsByUserId(userId);
		char index = 'A';
		for (Friend friend : friends) {
			if (friend.getUser().getNamePinyin().charAt(0) == index) {
				FriendDTO friendDTO = new FriendDTO();
				friendDTO.setAreaCode(friend.getUser().getAreaCode());
				friendDTO.setPhone(friend.getUser().getUserPhone());
				friendDTO.setName(friend.getUser().getUserName());
				friendDTO.setPortrait(friend.getUser().getUserPortrait() == null ? null
						: S3Utils.getImgUrl(friend.getUser().getUserPortrait()));
				friendDTOs.add(friendDTO);
			} else {
				/* 将上一个字母存入List<FriendInitial> */
				if (friendDTOs.size() > 0) {
					FriendInitial friendInitial = new FriendInitial();
					friendInitial.setInitial(index);
					friendInitial.setFriends(friendDTOs);
					friendInitials.add(friendInitial);
					/* 清空 List<FriendDTO> */
					friendDTOs = new ArrayList<>();
				}
				/* 处理当前Friend信息 */
				FriendDTO friendDTO = new FriendDTO();
				friendDTO.setAreaCode(friend.getUser().getAreaCode());
				friendDTO.setPhone(friend.getUser().getUserPhone());
				friendDTO.setName(friend.getUser().getUserName());
				friendDTO.setPortrait(friend.getUser().getUserPortrait() == null ? null
						: S3Utils.getImgUrl(friend.getUser().getUserPortrait()));
				friendDTOs.add(friendDTO);
				index = friend.getUser().getNamePinyin().charAt(0);
			}

		}
		/* 处理最后一个字母 */
		FriendInitial friendInitial = new FriendInitial();
		friendInitial.setInitial(index);
		friendInitial.setFriends(friendDTOs);
		friendInitials.add(friendInitial);
		return friendInitials;
	}

	@Override
	public List<FriendDTO> searchFriend(Integer userId, String keyWords) {
		List<FriendDTO> friendDTOs = new ArrayList<>();
		List<Friend> friends = friendDAO.getFriendByUserIdAndKeyWords(userId, keyWords);
		for (Friend friend : friends) {
			FriendDTO friendDTO = new FriendDTO();
			friendDTO.setAreaCode(friend.getUser().getAreaCode());
			friendDTO.setPhone(friend.getUser().getUserPhone());
			friendDTO.setName(friend.getUser().getUserName());
			friendDTO.setPortrait(friend.getUser().getUserPortrait() == null ? null
					: S3Utils.getImgUrl(friend.getUser().getUserPortrait()));
			friendDTOs.add(friendDTO);
		}
		return friendDTOs;
	}

	@Override
	public SendMessageResponse getPinCode(String func, String areaCode, String userPhone) {
		String pinCode = redisDAO.getValueByKey(func + areaCode + userPhone);
		if (pinCode == null) {
			if (ResourceUtils.getBundleValue4Boolean("qa.switch")) {
				pinCode = ResourceUtils.getBundleValue4String("verify.code", "654321");
			} else {
				/* 随机生成六位数 */
				pinCode = MathUtils.randomFixedLengthStr(6);
			}
		}
		logger.info("getPinCode : phone={}, pincode={}", areaCode + userPhone, pinCode);
		/* 存入redis */
		redisDAO.saveData(func + areaCode + userPhone, pinCode,
				configManager.getConfigLongValue(ConfigKeyEnum.VERIFYTIME, 10l).intValue(), TimeUnit.MINUTES);
		return smsManager.sendSMS4PhoneVerify(areaCode, userPhone, pinCode, func);
	}

	@Override
	public Integer getUserId(String areaCode, String userPhone) {
		logger.info("getUserId - phone:{} --> ", areaCode + userPhone);
		User user = userDAO.getUserByUserPhone(areaCode, userPhone);
		if (user != null) {
			if (user.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_AVAILABLE) {
				logger.info("*** UserId = {} ", user.getUserId());
				return user.getUserId();
			}
			logger.info("User is frozen!");
			return 0;
		}
		logger.info("No User!");
		return null;
	}

	@Override
	public UserInfo getUserInfo(Integer userId) {
		logger.info("getUserInfo userId:{} -->", userId);
		User user = userDAO.getUser(userId);
		UserInfo userInfo = null;
		if (user != null) {
			userInfo = new UserInfo(user.getUserId(), user.getAreaCode(), user.getUserPhone(), user.getUserName(),
					user.getUserPortrait() == null ? null : S3Utils.getImgUrl(user.getUserPortrait()),
					StringUtils.isNotBlank(user.getUserPayPwd()));
			logger.info("*** {}", userInfo.toString());
		} else {
			logger.info("Can not find the user!!!");
		}
		return userInfo;
	}

	@Override
	public void logout(Integer userId) {
		if (userId != 0) {
			User user = userDAO.getUser(userId);
			// logger.info("unbind Tag==>");
			// pushManager.unbindPushTag(user.getPushId(), user.getPushTag());
			user.setPushId(null);
			userDAO.updateUser(user);
		}
	}

	@Override
	public Integer register(String areaCode, String userPhone, String userName, String userPassword, String language) {
		/* 随机生成盐值 */
		String passwordSalt = DigestUtils.md5Hex(MathUtils.randomFixedLengthStr(6));
		logger.info("Randomly generated salt values : salt={}", passwordSalt);
		/* 添加用户 */
		Integer userId = userDAO.addUser(new User(areaCode, userPhone, userName, PinyinUtils.toPinyin(userName),
				PasswordUtils.encrypt(userPassword, passwordSalt), new Date(), ServerConsts.USER_TYPE_OF_CUSTOMER,
				ServerConsts.USER_AVAILABLE_OF_AVAILABLE, ServerConsts.LOGIN_AVAILABLE_OF_AVAILABLE,
				ServerConsts.PAY_AVAILABLE_OF_AVAILABLE, passwordSalt, LanguageUtils.standard(language)));
		logger.info("Add user complete!");
		/* 记录换绑手机时间 */
		redisDAO.saveData("changephonetime" + userId, new Date().getTime());
		/* 添加钱包信息 */
		createWallets4NewUser(userId);
		/* 创建Goldpay账号 */
		bindGoldpay(areaCode, userPhone, userName, userId);

		// accountingManager.snapshotToBefore(userId);
		/* 根据Unregistered表 更新新用户钱包 将资金从系统帐户划给新用户 */
		updateWalletsFromUnregistered(userId, areaCode, userPhone, userName);
		return userId;
	}

	@Override
	public void switchLanguage(Integer userId, String language) {
		Language newLanguage = LanguageUtils.standard(language);
		logger.info("USER {} switchLanguage to {} -->", userId, newLanguage.toString());
		User user = userDAO.getUser(userId);
		if (!user.getPushTag().equals(newLanguage)) {
			logger.info("***Language inconsistency***");
			user.setPushTag(newLanguage);
			userDAO.updateUser(user);
			/* 绑定Tag */
			pushManager.switchTag(user.getPushId(), newLanguage);
		} else {
			logger.info("***Language consistency,do nothing!***");
		}
	}

	@Override
	public Boolean testPinCode(String func, String areaCode, String userPhone, String verificationCode) {
		logger.info("Check phone number {} and verify code {} -->", areaCode + userPhone, verificationCode);

		String pinCode = redisDAO.getValueByKey(func + areaCode + userPhone);
		if (pinCode == null) {
			logger.info("***not get verify code***");
			return null;
		}
		if (pinCode.equals(verificationCode)) {
			logger.info("***match***");
			return true;
		}
		logger.info("***Does not match***");
		return false;
	}

	@Override
	public void updatePassword(Integer userId, String newPassword) {
		logger.info("Update USER {} password {} -->", userId, newPassword);
		User user = userDAO.getUser(userId);
		user.setUserPassword(PasswordUtils.encrypt(newPassword, user.getPasswordSalt()));
		userDAO.updateUser(user);
		redisDAO.deleteData(ServerConsts.WRONG_PASSWORD + userId);
	}

	@Override
	public void updateUser(Integer userId, String loginIp, String pushId, String language) {
		logger.info("Update USER {} login information -->", userId);
		User user = userDAO.getUser(userId);
		if (user == null) {
			return;
		}
		user.setLoginIp(loginIp);
		user.setLoginTime(new Date());

		/* 换设备 */
		if (StringUtils.isNotBlank(pushId) && !pushId.equals(user.getPushId())) {
			/* 推送消息：设备已下线 */
			logger.info("Push message: device offline -- oldPushId : {} , newPushId : {} ",
					new Object[] { user.getPushId(), pushId });
			pushManager.push4Offline(user.getPushId(), user.getPushTag(), String.valueOf(new Date().getTime()));
			user.setPushId(pushId);
		}

		/* 切换语言 */
		Language newLanguage = LanguageUtils.standard(language);
		logger.info("USER {} switchLanguage to {} -->", userId, newLanguage.toString());
		if (!user.getPushTag().equals(newLanguage)) {
			logger.info("***Language inconsistency***");
			user.setPushTag(newLanguage);
			/* 绑定Tag */
			pushManager.switchTag(user.getPushId(), newLanguage);
		} else {
			logger.info("***Language consistency,do nothing!***");
		}

		userDAO.updateUser(user);

		/* 清除其他账号的此pushId */
		if (StringUtils.isNotBlank(pushId)) {
			clearPushId(userId, pushId);
		}
	}

	private void clearPushId(Integer userId, String pushId) {
		logger.info("clearPushId : pushId={}, userId={}", pushId, userId);
		String hql = "update e_user set push_id = ? where push_id = ? and user_id != ?";
		userDAO.updateSQL(hql, new Object[] { "", pushId, userId });
	}

	@Override
	public void updateUserName(Integer userId, String newUserName) {
		User user = userDAO.getUser(userId);
		user.setUserName(newUserName);
		user.setNamePinyin(PinyinUtils.toPinyin(newUserName));
		userDAO.updateUser(user);
	}

	@Override
	public void updateUserPayPwd(Integer userId, String userPayPwd) {
		logger.info("Update user {} pay password {} -->", userId, userPayPwd);
		User user = userDAO.getUser(userId);
		user.setUserPayPwd(PasswordUtils.encrypt(userPayPwd, user.getPasswordSalt()));
		userDAO.updateUser(user);
	}

	@Override
	public void updateWallet(Integer userId) {
		logger.info("Update Wallet -->");
		List<Wallet> wallets = walletDAO.getWalletsByUserId(userId);
		HashMap<String, Wallet> mapwallet = new HashMap<String, Wallet>();
		for (Wallet wallet : wallets) {
			mapwallet.put(wallet.getCurrency().getCurrency(), wallet);
		}
		/* 获取当前可用的货币 */
		List<Currency> currencies = commonManager.getCurrentCurrencies();
		for (Currency currency : currencies) {
			if (mapwallet.get(currency.getCurrency()) == null) {
				/* 没有该货币的钱包，需要新增 */
				walletDAO.addwallet(new Wallet(currency, userId, BigDecimal.ZERO, new Date(), 0));
				logger.info("Added {} wallet to user {}", currency.getCurrency(), userId);
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
	private void updateWalletsFromUnregistered(Integer userId, String areaCode, String userPhone, String userName) {
		logger.info(
				"Update wallets according to Unregistered. Assign funds from system accounts to newly registered user -->");
		Integer systemUserId = userDAO.getSystemUser().getUserId();
		List<Unregistered> unregistereds = unregisteredDAO.getUnregisteredByUserPhone(areaCode, userPhone);
		for (Unregistered unregistered : unregistereds) {
			logger.info("+ {} : {}", unregistered.getCurrency(), unregistered.getAmount());

			Transfer payerTransfer = transferDAO.getTransferById(unregistered.getTransferId());

			if (payerTransfer == null || !unregistered.getUserPhone().equals(payerTransfer.getPhone())
					|| !unregistered.getAreaCode().equals(payerTransfer.getAreaCode())
					|| unregistered.getAmount().compareTo(payerTransfer.getTransferAmount()) != 0
					|| !unregistered.getCurrency().equals(payerTransfer.getCurrency())) {
				logger.info("Did not find the corresponding transfer information");
				unregistered.setUnregisteredStatus(ServerConsts.UNREGISTERED_STATUS_OF_ERROR);
				unregisteredDAO.updateUnregistered(unregistered);
				return;
			}

			String transferId = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_TRANSACTION);
			User payer = userDAO.getUser(payerTransfer.getUserFrom());
			String goldpayOrderId = null;
			if (ServerConsts.CURRENCY_OF_GOLDPAY.equals(unregistered.getCurrency())) {
				goldpayOrderId = goldpayTrans4MergeManager.getGoldpayOrderId();
				if (!StringUtils.isNotBlank(goldpayOrderId)) {
					return;
				}
			}

			// walletDAO.updateWalletByUserIdAndCurrency(systemUserId,
			// unregistered.getCurrency(),
			// unregistered.getAmount(), "-", ServerConsts.TRANSFER_TYPE_TRANSACTION,
			// transferId);
			// walletDAO.updateWalletByUserIdAndCurrency(userId, unregistered.getCurrency(),
			// unregistered.getAmount(), "+",
			// ServerConsts.TRANSFER_TYPE_TRANSACTION, transferId);

			/* 生成TransId */
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
			transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
			transfer.setTransferType(ServerConsts.TRANSFER_TYPE_TRANSACTION);
			transfer.setTransferComment(unregistered.getTransferId());
			transfer.setNoticeId(0);
			transfer.setGoldpayOrderId(goldpayOrderId);
			transferDAO.addTransfer(transfer);

			goldpayTrans4MergeManager.updateWallet4GoldpayTrans(transferId);

			// walletDAO.updateWalletByUserIdAndCurrency(systemUserId,
			// unregistered.getCurrency(),
			// unregistered.getAmount(), "-", ServerConsts.TRANSFER_TYPE_TRANSACTION,
			// transferId);
			// walletDAO.updateWalletByUserIdAndCurrency(userId, unregistered.getCurrency(),
			// unregistered.getAmount(), "+",
			// ServerConsts.TRANSFER_TYPE_TRANSACTION, transferId);

			transDetailsManager.addTransDetails(transferId, userId, payer.getUserId(), payer.getUserName(),
					payer.getAreaCode(), payer.getUserPhone(), unregistered.getCurrency(), unregistered.getAmount(),
					BigDecimal.ZERO, null, payerTransfer.getTransferComment(),
					ServerConsts.TRANSFER_TYPE_TRANSACTION - 1);

			/* 更改unregistered状态 */
			unregistered.setUnregisteredStatus(ServerConsts.UNREGISTERED_STATUS_OF_COMPLETED);
			unregisteredDAO.updateUnregistered(unregistered);
		}

		// 更新details信息

		transDetailsDAO.updateTransDetails(userName, areaCode, userPhone);

	}

	@Override
	public String deleteFriend(Integer userId, String areaCode, String phone) {
		logger.info("Find friend -->");
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
	public void userFreeze(Integer userId, int userAvailable) {
		User user = userDAO.getUser(userId);
		if (user == null || user.getUserType() == ServerConsts.USER_TYPE_OF_SYSTEM) {
			logger.info("{} is not exist !", userId);
		} else {
			user.setUserAvailable(userAvailable);
			userDAO.updateUser(user);
		}
		crmUserInfoDAO.userFreeze(userId, userAvailable);
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
		if (deviceId.equals(ResourceUtils.getBundleValue4String("device.id.web", "WEB"))) {
			return false;
		}
		return userDeviceDAO.getUserDeviceByUserIdAndDeviceId(userId, deviceId) == null ? true : false;
	}

	@Override
	public void addDevice(Integer userId, String deviceId, String deviceName) {
		logger.info("User{} add device {}-{} -->", userId, deviceName, deviceId);
		userDeviceDAO.addUserDevice(new UserDevice(new UserDeviceId(userId, deviceId), deviceName));
	}

	@Override
	public boolean isUserPayPwdEqualsOld(Integer userId, String newUserPayPwd) {
		logger.info("is {} user's newPayPwd {} equals old==>", userId, newUserPayPwd);
		User user = userDAO.getUser(userId);
		if (PasswordUtils.check(newUserPayPwd, user.getUserPayPwd(), user.getPasswordSalt())) {
			logger.info("***equal***");
			return true;
		}
		logger.info("***not equal***");
		return false;
	}

	@Override
	public UserDTO getUser(String areaCode, String userPhone) {
		User user = userDAO.getUserByUserPhone(areaCode, userPhone);
		if (user != null) {
			UserDTO userDTO = new UserDTO();
			userDTO.setUserId(user.getUserId());
			userDTO.setAreaCode(user.getAreaCode());
			userDTO.setUserPhone(user.getUserPhone());
			userDTO.setUserName(user.getUserName());
			userDTO.setUserPassword(user.getUserPassword());
			userDTO.setPasswordSalt(user.getPasswordSalt());
			userDTO.setUserAvailable(user.getUserAvailable());
			Bind bind = bindDAO.getBind(user.getUserId());
			userDTO.setGoldpayAccount(bind == null ? "" : bind.getGoldpayAcount());
			userDTO.setGoldpayId(bind == null ? 0L : Long.valueOf(bind.getGoldpayId()));
			userDTO.setGoldpayUserName(bind == null ? "" : bind.getGoldpayName());
			return userDTO;
		}
		return null;
	}

	@Override
	public User getUserById(Integer userId) {
		User user = userDAO.getUser(userId);
		return user;
	}

	@Override
	public Integer getSystemUserId() {
		return userDAO.getSystemUser().getUserId();
	}

	@Override
	public User getUserByPhone(String areaCode, String phone) {
		return userDAO.getUserByUserPhone(areaCode, phone);
	}

	@Override
	public void updateHappyLivesVIP(String happyLivesId, Integer userId) {
		Bind bind = bindDAO.getBind(userId);
		if (bind != null) {
			bindDAO.clearHappyLivesId(happyLivesId);
			bind.setHappyLivesId(happyLivesId);
			bindDAO.updateBind(bind);
		}
	}

	@Override
	public boolean isHappyLivesVIP(Integer userId) {
		Bind bind = bindDAO.getBind(userId);
		if (bind == null) {
			return false;
		}
		return StringUtils.isNotBlank(bind.getHappyLivesId());
	}

	@Override
	public void updatePayToken(int userId, String userPayToken) {
		User user = userDAO.getUser(userId);
		user.setUserPayToken(userPayToken);
		userDAO.updateUser(user);
	}

	@Override
	public String updateUserPortrait(Integer userId, File uploadFile) {
		User user = userDAO.getUser(userId);
		String portrait = userId + "/" + UidUtils.genUid()+".jpg";
		String imgUrl = S3Utils.uploadFile(portrait, uploadFile);
		if (imgUrl != null) {
			user.setUserPortrait(portrait);
			userDAO.updateUser(user);
		}
		return imgUrl;
	}

	@Override
	public UserInfo4Transfer findFriend(Integer userId, String areaCode, String userPhone) {
		User user = userDAO.getUserByUserPhone(areaCode, userPhone);
		UserInfo4Transfer userInfo4Transfer = null;
		if (user != null) {
			Friend friend = friendDAO.getFriendByUserIdAndFrindId(userId, user.getUserId());
			userInfo4Transfer = new UserInfo4Transfer();
			userInfo4Transfer.setName(user.getUserName());
			userInfo4Transfer.setPortrait(user.getUserPortrait());
			userInfo4Transfer.setFriend(friend != null);
		}
		return userInfo4Transfer;
	}
}