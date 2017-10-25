package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CrmAlarmDAO;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.FriendDAO;
import com.yuyutechnology.exchange.dao.NotificationDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dao.TransDetailsDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UnregisteredDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.dto.CheckPwdResult;
import com.yuyutechnology.exchange.dto.TransDetailsDTO;
import com.yuyutechnology.exchange.dto.TransferDTO;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.goldpay.msg.GoldpayUserDTO;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.manager.TransDetailsManager;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.CrmAlarm;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.Friend;
import com.yuyutechnology.exchange.pojo.TransactionNotification;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.Unregistered;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.push.PushManager;
import com.yuyutechnology.exchange.sms.SmsManager;
import com.yuyutechnology.exchange.util.DateFormatUtils;
import com.yuyutechnology.exchange.util.page.PageBean;

@Service
public class TransferManagerImpl implements TransferManager {

	@Autowired
	UserDAO userDAO;
	@Autowired
	RedisDAO redisDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	FriendDAO friendDAO;
	@Autowired
	TransferDAO transferDAO;
	@Autowired
	CurrencyDAO currencyDAO;
	@Autowired
	UnregisteredDAO unregisteredDAO;
	@Autowired
	NotificationDAO notificationDAO;
	@Autowired
	CrmAlarmDAO crmAlarmDAO;
	@Autowired
	TransDetailsDAO transDetailsDAO;
	@Autowired
	OandaRatesManager oandaRatesManager;
	@Autowired
	UserManager userManager;
	@Autowired
	PushManager pushManager;
	@Autowired
	SmsManager smsManager;
	@Autowired
	CommonManager commonManager;
	@Autowired
	ConfigManager configManager;
	@Autowired
	CrmAlarmManager crmAlarmManager;
	@Autowired
	TransDetailsManager transDetailsManager;
	
	@Autowired
	GoldpayTrans4MergeManager goldpayTrans4MergeManager;

	public static Logger logger = LogManager.getLogger(TransferManagerImpl.class);

	@SuppressWarnings("serial")
	@Override
	public HashMap<String, String> transferInitiate(final int userId, String areaCode, String userPhone,
			final String currency, final BigDecimal amount, String transferComment, int noticeId) {

		// 干扰条件过滤
		LinkedHashMap<String, Object> args = new LinkedHashMap<>();
		args.put("isTradableCurrency", currency);
		args.put("isAccountFrozened", userId);
		args.put("isInsufficientBalance", new HashMap<String, Object>() {
			{
				put("userId", userId);
				put("currency", currency);
				put("amount", amount);
			}
		});
		HashMap<String, String> map = test(args);
		if (!map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)) {
			return map;
		}

		map = checkTransferLimit(currency, amount, userId);
		if (!map.isEmpty()) {
			return map;
		}

		User receiver = userDAO.getUserByUserPhone(areaCode, userPhone);
		// 不用给自己转账
		if (receiver != null && userId == receiver.getUserId()) {
			logger.warn("Prohibit transfers to yourself");
			map.put("retCode", RetCodeConsts.TRANSFER_PROHIBIT_TRANSFERS_TO_YOURSELF);
			map.put("msg", "Prohibit transfers to yourself");
			return map;
		}
		
		String goldpayOrderId = null;
		
		if(ServerConsts.CURRENCY_OF_GOLDPAY.equals(currency)){
			goldpayOrderId = goldpayTrans4MergeManager.getGoldpayOrderId();
			if(!StringUtils.isNotBlank(goldpayOrderId)){
				map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
				map.put("msg", "Failed to create goldpay order");
				return map;
			}
		}
		

		// 生成TransId
		String transferId = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_TRANSACTION);

		Transfer transfer = new Transfer();
		transfer.setTransferId(transferId);
		transfer.setCreateTime(new Date());
		transfer.setCurrency(currency);
		transfer.setTransferAmount(amount);
		transfer.setTransferComment(transferComment);
		transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		transfer.setUserFrom(userId);
		transfer.setAreaCode(areaCode);
		transfer.setPhone(userPhone);
		transfer.setGoldpayOrderId(goldpayOrderId);

		// 判断接收人是否是已注册账号
		if (receiver != null) {
			transfer.setUserTo(receiver.getUserId());
			transfer.setTransferType(ServerConsts.TRANSFER_TYPE_TRANSACTION);
			// 判断对方是否有该种货币
			commonManager.checkAndUpdateWallet(receiver.getUserId(), currency);

			// add by Niklaus.chi at 2017/07/07
			transDetailsManager.addTransDetails(transferId, userId, receiver.getUserId(), receiver.getUserName(),
					areaCode, userPhone, currency, amount, transferComment, ServerConsts.TRANSFER_TYPE_TRANSACTION);

		} else {
			User systemUser = userDAO.getSystemUser();
			transfer.setUserTo(systemUser.getUserId());
			transfer.setTransferType(ServerConsts.TRANSFER_TYPE_OUT_INVITE);

			// add by Niklaus.chi at 2017/07/07
			transDetailsManager.addTransDetails(transferId, userId, null, null, areaCode, userPhone, currency, amount,
					transferComment, ServerConsts.TRANSFER_TYPE_OUT_INVITE);

		}
		transfer.setNoticeId(noticeId);
		// 保存
		transferDAO.addTransfer(transfer);

		map.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		map.put("msg", "ok");
		map.put("transferId", transferId);

		return map;
	}

	@Override
	public HashMap<String, String> payPwdConfirm(int userId, String transferId, String userPayPwd) {

		HashMap<String, String> result = new HashMap<>();
		User user = userDAO.getUser(userId);
		if (user == null || user.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE) {
			logger.warn("The user does not exist or the account is blocked");
			result.put("msg", "The user does not exist or the account is blocked");
			result.put("retCode", RetCodeConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED);
			return result;
		}

		// 验证支付密码
		CheckPwdResult checkPwdResult = userManager.checkPayPassword(userId, userPayPwd);
		switch (checkPwdResult.getStatus()) {
		case ServerConsts.CHECKPWD_STATUS_FREEZE:
			result.put("msg", String.valueOf(checkPwdResult.getInfo()));
			result.put("retCode", RetCodeConsts.PAY_FREEZE);
			return result;

		case ServerConsts.CHECKPWD_STATUS_INCORRECT:
			logger.warn("payPwd is wrong !");
			result.put("msg", String.valueOf(checkPwdResult.getInfo()));
			result.put("retCode", RetCodeConsts.PAY_PWD_NOT_MATCH);
			return result;

		default:
			break;
		}

		Transfer transfer = transferDAO.getTranByIdAndStatus(transferId,
				ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		if (transfer == null) {
			logger.warn("The transaction order does not exist");
			result.put("msg", "The transaction order does not exist");
			result.put("retCode", RetCodeConsts.TRANSFER_TRANS_ORDERID_NOT_EXIST);
			return result;
		}

		// 总账大于设置安全基数，弹出需要短信验证框===============================================
		BigDecimal totalBalance = oandaRatesManager.getTotalBalance(userId);
		BigDecimal totalBalanceMax = BigDecimal
				.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.TOTALBALANCETHRESHOLD, 100000d));
		// 当天累计转出总金额大于设置安全基数，弹出需要短信验证框
		BigDecimal accumulatedAmount = transferDAO.getAccumulatedAmount("transfer_" + userId);
		BigDecimal accumulatedAmountMax = BigDecimal
				.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.DAILYTRANSFERTHRESHOLD, 100000d));
		// 单笔转出金额大于设置安全基数，弹出需要短信验证框
		BigDecimal singleTransferAmount = oandaRatesManager.getDefaultCurrencyAmount(transfer.getCurrency(),
				transfer.getTransferAmount());
		BigDecimal singleTransferAmountMax = BigDecimal
				.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.EACHTRANSFERTHRESHOLD, 100000d));

		logger.info("totalBalance : {},totalBalanceMax: {} ", totalBalance, totalBalanceMax);
		logger.info("accumulatedAmount : {},accumulatedAmountMax: {} ", accumulatedAmount, accumulatedAmountMax);
		logger.info("singleTransferAmount : {},singleTransferAmountMax: {} ", singleTransferAmount,
				singleTransferAmountMax);

		if (totalBalance.compareTo(totalBalanceMax) == 1 || (accumulatedAmount.compareTo(accumulatedAmountMax) == 1
				|| singleTransferAmount.compareTo(singleTransferAmountMax) == 1)) {
			logger.warn("The transaction amount exceeds the limit");
			result.put("msg", "The transaction amount exceeds the limit");
			result.put("retCode", RetCodeConsts.TRANSFER_REQUIRES_PHONE_VERIFICATION);
			return result;
		} else {
			String retCode = transferConfirm(userId, transferId);
			result.put("msg", retCode);
			result.put("retCode", retCode);
			return result;
		}
	}

//	@Override
//	public String transferConfirm(int userId, String transferId) {
//
//		Transfer transfer = transferDAO.getTranByIdAndStatus(transferId,
//				ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
//		if (transfer == null) {
//			logger.warn("The transaction order does not exist");
//			return RetCodeConsts.TRANSFER_TRANS_ORDERID_NOT_EXIST;
//		}
//
//		User payer = userDAO.getUser(userId);
//		if (payer == null || payer.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE) {
//			logger.warn("The user does not exist or the account is blocked");
//			return RetCodeConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED;
//		}
//
//		if (userId != transfer.getUserFrom()) {
//			logger.warn("userId is different from UserFromId");
//			return RetCodeConsts.RET_CODE_FAILUE;
//		}
//
//		// 每次支付金额限制
//		BigDecimal transferLimitPerPay = BigDecimal
//				.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITPERPAY, 100000d));
//		logger.warn("transferLimitPerPay : {}", transferLimitPerPay);
//		if ((oandaRatesManager.getDefaultCurrencyAmount(transfer.getCurrency(), transfer.getTransferAmount()))
//				.compareTo(transferLimitPerPay) == 1) {
//			logger.warn("Exceeds the maximum amount of each transaction");
//			return RetCodeConsts.TRANSFER_LIMIT_EACH_TIME;
//		}
//
//		// 每天累计金额限制
//		BigDecimal transferLimitDailyPay = BigDecimal
//				.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITDAILYPAY, 100000d));
//		BigDecimal accumulatedAmount = transferDAO.getAccumulatedAmount("transfer_" + userId);
//		logger.warn("transferLimitDailyPay : {},accumulatedAmount : {} ", transferLimitDailyPay, accumulatedAmount);
//		if ((accumulatedAmount
//				.add(oandaRatesManager.getDefaultCurrencyAmount(transfer.getCurrency(), transfer.getTransferAmount())))
//						.compareTo(transferLimitDailyPay) == 1) {
//			logger.warn("More than the maximum daily transaction limit");
//			return RetCodeConsts.TRANSFER_LIMIT_DAILY_PAY;
//		}
//		// 每天累计给付次数限制
//		Double transferLimitNumOfPayPerDay = configManager
//				.getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITNUMBEROFPAYPERDAY, 100000d);
//		// Integer dayTradubgVolume =
//		// transferDAO.getDayTradubgVolume(ServerConsts.TRANSFER_TYPE_TRANSACTION);
//		Integer dayTradubgVolume = transferDAO.getCumulativeNumofTimes("transfer_" + userId);
//		logger.warn("transferLimitNumOfPayPerDay : {},dayTradubgVolume : {} ", transferLimitNumOfPayPerDay,
//				dayTradubgVolume);
//		if (transferLimitNumOfPayPerDay <= new Double(dayTradubgVolume)) {
//			logger.warn("Exceeds the maximum number of transactions per day");
//			return RetCodeConsts.TRANSFER_LIMIT_NUM_OF_PAY_PER_DAY;
//		}
//
//		// 获取系统账号
//		User systemUser = userDAO.getSystemUser();
//
//		if (transfer.getUserTo() == systemUser.getUserId()) { // 交易对象没有注册账号
//			// 扣款
//			Integer updateCount = walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(),
//					transfer.getCurrency(), transfer.getTransferAmount(), "-", ServerConsts.TRANSFER_TYPE_OUT_INVITE,
//					transfer.getTransferId());
//
//			if (updateCount == 0) {
//				return RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT;
//			}
//
//			// 加款
//			walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), transfer.getCurrency(),
//					transfer.getTransferAmount(), "+", ServerConsts.TRANSFER_TYPE_OUT_INVITE, transfer.getTransferId());
//
//			// 添加gift记录
//			Unregistered unregistered = unregisteredDAO.getUnregisteredByTransId(transfer.getTransferId());
//
//			if (unregistered == null) {
//				unregistered = new Unregistered();
//				unregistered.setCreateTime(new Date());
//				unregistered.setUnregisteredStatus(ServerConsts.UNREGISTERED_STATUS_OF_PENDING);
//				unregistered.setTransferId(transfer.getTransferId());
//				unregistered.setAreaCode(transfer.getAreaCode());
//				unregistered.setUserPhone(transfer.getPhone());
//				unregistered.setCurrency(transfer.getCurrency());
//				unregistered.setAmount(transfer.getTransferAmount());
//				unregisteredDAO.addUnregistered(unregistered);
//			}
//
//			// 更改Transfer状态
//			// transferDAO.updateTransferStatus(transferId,
//			// ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
//
//			// 向未注册用户发送短信
//			smsManager.sendSMS4Transfer(transfer.getAreaCode(), transfer.getPhone(), payer, transfer.getCurrency(),
//					amountFormatByCurrency(transfer.getCurrency(), transfer.getTransferAmount()));
//
//		} else { // 交易对象注册账号,交易正常进行，无需经过系统账户
//
//			// 扣款
//			Integer updateCount = walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(),
//					transfer.getCurrency(), transfer.getTransferAmount(), "-", ServerConsts.TRANSFER_TYPE_TRANSACTION,
//					transfer.getTransferId());
//
//			if (updateCount == 0) {
//				return RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT;
//			}
//
//			// 加款
//			walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserTo(), transfer.getCurrency(),
//					transfer.getTransferAmount(), "+", ServerConsts.TRANSFER_TYPE_TRANSACTION,
//					transfer.getTransferId());
//
//			// 如果是请求转账还需要更改消息通知中的状态
//			if (transfer.getNoticeId() != 0) {
//				TransactionNotification notification = notificationDAO.getNotificationById(transfer.getNoticeId());
//				notification.setTradingStatus(ServerConsts.NOTIFICATION_STATUS_OF_ALREADY_PAID);
//				notificationDAO.updateNotification(notification);
//			}
//			// 推送到账通知
//
//			User payee = userDAO.getUser(transfer.getUserTo());
//			pushManager.push4Transfer(transfer.getTransferId(), payer, payee, transfer.getCurrency(),
//					amountFormatByCurrency(transfer.getCurrency(), transfer.getTransferAmount()));
//		}
//		// 更改Transfer状态
//		transferDAO.updateTransferStatus(transferId, ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
//
//		// 转换金额
//		BigDecimal exchangeResult = oandaRatesManager.getDefaultCurrencyAmount(transfer.getCurrency(),
//				transfer.getTransferAmount());
//		transferDAO.updateAccumulatedAmount("transfer_" + transfer.getUserFrom(),
//				exchangeResult.setScale(2, BigDecimal.ROUND_FLOOR));
//		// 更改累计次数
//		transferDAO.updateCumulativeNumofTimes("transfer_" + transfer.getUserFrom(), new BigDecimal("1"));
//
//		// 预警
//		largeTransWarn(payer, transfer);
//
//		return RetCodeConsts.RET_CODE_SUCCESS;
//	}

	@Override
	public String transferConfirm(int userId, String transferId) {

		Transfer transfer = transferDAO.getTranByIdAndStatus(transferId,
				ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		if (transfer == null) {
			logger.warn("The transaction order does not exist");
			return RetCodeConsts.TRANSFER_TRANS_ORDERID_NOT_EXIST;
		}

		User payer = userDAO.getUser(userId);
		if (payer == null || payer.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE) {
			logger.warn("The user does not exist or the account is blocked");
			return RetCodeConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED;
		}

		if (userId != transfer.getUserFrom()) {
			logger.warn("userId is different from UserFromId");
			return RetCodeConsts.RET_CODE_FAILUE;
		}

		// 每次支付金额限制
		BigDecimal transferLimitPerPay = BigDecimal
				.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITPERPAY, 100000d));
		logger.warn("transferLimitPerPay : {}", transferLimitPerPay);
		if ((oandaRatesManager.getDefaultCurrencyAmount(transfer.getCurrency(), transfer.getTransferAmount()))
				.compareTo(transferLimitPerPay) == 1) {
			logger.warn("Exceeds the maximum amount of each transaction");
			return RetCodeConsts.TRANSFER_LIMIT_EACH_TIME;
		}

		// 每天累计金额限制
		BigDecimal transferLimitDailyPay = BigDecimal
				.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITDAILYPAY, 100000d));
		BigDecimal accumulatedAmount = transferDAO.getAccumulatedAmount("transfer_" + userId);
		logger.warn("transferLimitDailyPay : {},accumulatedAmount : {} ", transferLimitDailyPay, accumulatedAmount);
		if ((accumulatedAmount
				.add(oandaRatesManager.getDefaultCurrencyAmount(transfer.getCurrency(), transfer.getTransferAmount())))
						.compareTo(transferLimitDailyPay) == 1) {
			logger.warn("More than the maximum daily transaction limit");
			return RetCodeConsts.TRANSFER_LIMIT_DAILY_PAY;
		}
		// 每天累计给付次数限制
		Double transferLimitNumOfPayPerDay = configManager
				.getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITNUMBEROFPAYPERDAY, 100000d);
		
		Integer dayTradubgVolume = transferDAO.getCumulativeNumofTimes("transfer_" + userId);
		logger.warn("transferLimitNumOfPayPerDay : {},dayTradubgVolume : {} ", transferLimitNumOfPayPerDay,
				dayTradubgVolume);
		if (transferLimitNumOfPayPerDay <= new Double(dayTradubgVolume)) {
			logger.warn("Exceeds the maximum number of transactions per day");
			return RetCodeConsts.TRANSFER_LIMIT_NUM_OF_PAY_PER_DAY;
		}
		
		// 获取系统账号
		User systemUser = userDAO.getSystemUser();

		if (transfer.getUserTo() == systemUser.getUserId()) { // 交易对象没有注册账号
			
			goldpayTrans4MergeManager.updateWallet4GoldpayTrans(transferId);
			// 添加gift记录
			Unregistered unregistered = unregisteredDAO.getUnregisteredByTransId(transfer.getTransferId());

			if (unregistered == null) {
				unregistered = new Unregistered();
				unregistered.setCreateTime(new Date());
				unregistered.setUnregisteredStatus(ServerConsts.UNREGISTERED_STATUS_OF_PENDING);
				unregistered.setTransferId(transfer.getTransferId());
				unregistered.setAreaCode(transfer.getAreaCode());
				unregistered.setUserPhone(transfer.getPhone());
				unregistered.setCurrency(transfer.getCurrency());
				unregistered.setAmount(transfer.getTransferAmount());
				unregisteredDAO.addUnregistered(unregistered);
			}

			// 向未注册用户发送短信
			smsManager.sendSMS4Transfer(transfer.getAreaCode(), transfer.getPhone(), payer, transfer.getCurrency(),
					amountFormatByCurrency(transfer.getCurrency(), transfer.getTransferAmount()));

		} else { // 交易对象注册账号,交易正常进行，无需经过系统账户
			goldpayTrans4MergeManager.updateWallet4GoldpayTrans(transferId);

			// 如果是请求转账还需要更改消息通知中的状态
			if (transfer.getNoticeId() != 0) {
				TransactionNotification notification = notificationDAO.getNotificationById(transfer.getNoticeId());
				notification.setTradingStatus(ServerConsts.NOTIFICATION_STATUS_OF_ALREADY_PAID);
				notificationDAO.updateNotification(notification);
			}
			
			// 推送到账通知
			User payee = userDAO.getUser(transfer.getUserTo());
			pushManager.push4Transfer(transfer.getTransferId(), payer, payee, transfer.getCurrency(),
					amountFormatByCurrency(transfer.getCurrency(), transfer.getTransferAmount()));
		}
		// 更改Transfer状态
		transferDAO.updateTransferStatus(transferId, ServerConsts.TRANSFER_STATUS_OF_COMPLETED);

		// 转换金额
		BigDecimal exchangeResult = oandaRatesManager.getDefaultCurrencyAmount(transfer.getCurrency(),
				transfer.getTransferAmount());
		transferDAO.updateAccumulatedAmount("transfer_" + transfer.getUserFrom(),
				exchangeResult.setScale(2, BigDecimal.ROUND_FLOOR));
		// 更改累计次数
		transferDAO.updateCumulativeNumofTimes("transfer_" + transfer.getUserFrom(), new BigDecimal("1"));

		// 预警
		largeTransWarn(payer, transfer);

		return RetCodeConsts.RET_CODE_SUCCESS;
	}

	
	
//	@Override
//	public void systemRefund(Unregistered unregistered) {
//
//		Transfer transfer = transferDAO.getTransferById(unregistered.getTransferId());
//		if (transfer == null || transfer.getTransferStatus() != ServerConsts.TRANSFER_STATUS_OF_COMPLETED) {
//			logger.warn("Did not find the corresponding transfer information");
//			return;
//		}
//		String transferId2 = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_TRANSACTION);
//		User systemUser = userDAO.getSystemUser();
//		// 系统扣款
//		walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), transfer.getCurrency(),
//				transfer.getTransferAmount(), "-", ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND, transferId2);
//		// 用户加款
//		walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(), transfer.getCurrency(),
//				transfer.getTransferAmount(), "+", ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND, transferId2);
//
//		/////////////////////////// 生成transfer系统退款订单////////////////////////////
//		Transfer transfer2 = new Transfer();
//		// 生成TransId
//		transfer2.setTransferId(transferId2);
//		transfer2.setUserFrom(systemUser.getUserId());
//		transfer2.setUserTo(transfer.getUserFrom());
//		transfer2.setAreaCode(unregistered.getAreaCode());
//		transfer2.setPhone(unregistered.getUserPhone());
//		transfer2.setCurrency(transfer.getCurrency());
//		transfer2.setTransferAmount(transfer.getTransferAmount());
//		transfer2.setTransferComment(unregistered.getUserPhone() + "对方逾期未注册,系统退款");
//		transfer2.setTransferType(ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND);
//		transfer2.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
//		transfer2.setCreateTime(new Date());
//		transfer2.setFinishTime(new Date());
//		transfer2.setNoticeId(0);
//
//		transferDAO.addTransfer(transfer2);
//		/////////////////////////// end////////////////////////////
//
//		// add by Niklaus.chi at 2017/07/07
//		transDetailsManager.addTransDetails(transferId2, transfer.getUserFrom(), null, null, unregistered.getAreaCode(),
//				unregistered.getUserPhone(), transfer.getCurrency(), transfer.getTransferAmount(),
//				unregistered.getUserPhone() + "对方逾期未注册,系统退款", ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND);
//
//		// 修改gift记录
//		unregistered.setUnregisteredStatus(ServerConsts.UNREGISTERED_STATUS_OF_BACK);
//		unregistered.setRefundTransId(transferId2);
//		unregisteredDAO.updateUnregistered(unregistered);
//
//		// 发送推送
//		User payee = userDAO.getUser(transfer.getUserFrom());
//
//		pushManager.push4Refund(payee, transfer.getAreaCode(), transfer.getPhone(), transfer.getCurrency(),
//				amountFormatByCurrency(transfer.getCurrency(), transfer.getTransferAmount()));
//
//	}
	
	@Override
	public void systemRefund(Unregistered unregistered) {

		Transfer transfer = transferDAO.getTransferById(unregistered.getTransferId());
		if (transfer == null || transfer.getTransferStatus() != ServerConsts.TRANSFER_STATUS_OF_COMPLETED) {
			logger.warn("Did not find the corresponding transfer information");
			return;
		}
		
		String transferId2 = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_TRANSACTION);
		User systemUser = userDAO.getSystemUser();
		
		String goldpayOrderId = null;
		if(ServerConsts.CURRENCY_OF_GOLDPAY.equals(transfer.getCurrency())){
			goldpayOrderId = goldpayTrans4MergeManager.getGoldpayOrderId();
		}
			
		//生成transfer系统退款订单
		Transfer transfer2 = new Transfer();
		// 生成TransId
		transfer2.setTransferId(transferId2);
		transfer2.setUserFrom(systemUser.getUserId());
		transfer2.setUserTo(transfer.getUserFrom());
		transfer2.setAreaCode(unregistered.getAreaCode());
		transfer2.setPhone(unregistered.getUserPhone());
		transfer2.setCurrency(transfer.getCurrency());
		transfer2.setTransferAmount(transfer.getTransferAmount());
		transfer2.setTransferComment(unregistered.getUserPhone() + "对方逾期未注册,系统退款");
		transfer2.setTransferType(ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND);
		transfer2.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
		transfer2.setCreateTime(new Date());
		transfer2.setFinishTime(new Date());
		transfer2.setNoticeId(0);
		transfer2.setGoldpayOrderId(goldpayOrderId);
		transferDAO.addTransfer(transfer2);
		/////////////////////////// end////////////////////////////

		goldpayTrans4MergeManager.updateWallet4GoldpayTrans(transferId2);
//		walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), transfer.getCurrency(),
//		transfer.getTransferAmount(), "-", ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND, transferId2);
//walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(), transfer.getCurrency(),
//		transfer.getTransferAmount(), "+", ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND, transferId2);

		transDetailsManager.addTransDetails(transferId2, transfer.getUserFrom(), null, null, unregistered.getAreaCode(),
				unregistered.getUserPhone(), transfer.getCurrency(), transfer.getTransferAmount(),
				unregistered.getUserPhone() + "对方逾期未注册,系统退款", ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND);

		// 修改gift记录
		unregistered.setUnregisteredStatus(ServerConsts.UNREGISTERED_STATUS_OF_BACK);
		unregistered.setRefundTransId(transferId2);
		unregisteredDAO.updateUnregistered(unregistered);

		// 发送推送
		User payee = userDAO.getUser(transfer.getUserFrom());

		pushManager.push4Refund(payee, transfer.getAreaCode(), transfer.getPhone(), transfer.getCurrency(),
				amountFormatByCurrency(transfer.getCurrency(), transfer.getTransferAmount()));

	}

	@Override
	public void systemRefundBatch() {

		// 获取所有未完成的订单
		List<Unregistered> list = unregisteredDAO.getAllUnfinishedTransaction();
		if (list.isEmpty()) {
			return;
		}
		for (Unregistered unregistered : list) {
			// 判断是否超过期限
			long deadline = configManager.getConfigLongValue(ConfigKeyEnum.REFUNTIME, 3l) * 24 * 60 * 60 * 1000;
			if (new Date().getTime() - unregistered.getCreateTime().getTime() >= deadline) {

				logger.info("deadline : {},Difference : {}", deadline,
						new Date().getTime() - unregistered.getCreateTime().getTime());

				logger.info(
						"Invitation ID: {}, The invitee has not registered for the due "
								+ "date and the system is being refunded ,{}",
						unregistered.getUnregisteredId(), new SimpleDateFormat("HH:mm:ss").format(new Date()));

				systemRefund(unregistered);
			}
		}
	}

	@Override
	public HashMap<String, Object> makeRequest(int userId, String payerAreaCode, String payerPhone, String currency,
			BigDecimal amount) {

		HashMap<String, Object> result = new HashMap<>();

		BigDecimal transferLimitPerPay = BigDecimal
				.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITPERPAY, 100000d));

		if (StringUtils.isNotBlank(currency) && StringUtils.isNotBlank(amount.toString())) {
			if ((oandaRatesManager.getDefaultCurrencyAmount(currency, amount)).compareTo(transferLimitPerPay) == 1) {

				Currency unit = commonManager.getCurreny(ServerConsts.CURRENCY_OF_USD);

				logger.warn("Exceeds the maximum amount of each transaction");
				result.put("retCode", RetCodeConsts.TRANSFER_LIMIT_EACH_TIME);
				result.put("msg", "Exceeds the maximum amount of each transaction");
				result.put("transferLimitPerPay", transferLimitPerPay.toString());
				result.put("unit", unit.getCurrencyUnit());
				return result;
			}
		}

		// 请求对方不是己方好友

		// Currency不在激活列表
		if (StringUtils.isNotBlank(currency) && !commonManager.verifyCurrency(currency)) {
			logger.warn("This currency is not a tradable currency");
			result.put("retCode", RetCodeConsts.TRANSFER_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY);
			result.put("msg", "This currency is not a tradable currency");
			return result;
		}

		// Goldpay不是整数
		if ((StringUtils.isNotBlank(currency) && currency.equals(ServerConsts.CURRENCY_OF_GOLDPAY))
				&& (amount.doubleValue() % 1 > 0)) {
			logger.warn("The GDQ must be an integer value");
			result.put("retCode", RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
			result.put("msg", "The GDQ must be an integer value");
			return result;
		}

		User payer = userDAO.getUserByUserPhone(payerAreaCode, payerPhone);
		if (payer != null) {

			if (userId == payer.getUserId()) {
				logger.info("bu neng gei zi ji fa qi qingqiu");
				result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
				result.put("msg", "Sharing failed");
				return result;
			}

			TransactionNotification transactionNotification = new TransactionNotification();
			transactionNotification.setSponsorId(userId);
			transactionNotification.setPayerId(payer.getUserId());
			transactionNotification.setCurrency(currency);
			transactionNotification.setAmount(amount);
			transactionNotification.setCreateAt(new Date());
			transactionNotification.setRemarks("");
			transactionNotification.setNoticeStatus(0);
			transactionNotification.setTradingStatus(ServerConsts.NOTIFICATION_STATUS_OF_PENDING);

			notificationDAO.addNotification(transactionNotification);

			// 推送请求付款
			User payee = userDAO.getUser(userId);
			pushManager.push4TransferRuquest(payer, payee, currency, amountFormatByCurrency(currency, amount));
			result.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
			result.put("msg", MessageConsts.RET_CODE_SUCCESS);
			return result;

		}
		result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
		result.put("msg", MessageConsts.RET_CODE_FAILUE);
		return result;
	}

	@Override
	public HashMap<String, Object> getTransactionRecord(String period, String type, int userId, int currentPage,
			int pageSize) {

		HashMap<String, Object> map = getTransactionRecordByPage(period, type, userId, currentPage, pageSize);

		if (map.isEmpty()) {
			return null;
		}

		User system = userDAO.getSystemUser();
		List<?> list = (List<?>) map.get("list");
		List<TransferDTO> dtos = new ArrayList<>();

		for (Object object : list) {
			Object[] obj = (Object[]) object;

			TransferDTO dto = new TransferDTO();

			dto.setTransferId((String) obj[0]);
			dto.setCurrency((String) obj[1]);
			dto.setAmount(((BigDecimal) obj[2]).doubleValue());
			dto.setCurrencyUnit((String) obj[3]);

			if ((int) obj[4] == ServerConsts.TRANSFER_TYPE_TRANSACTION && (((BigDecimal) obj[2]).doubleValue()) > 0) {
				dto.setTransferType(1);
			} else {
				dto.setTransferType((int) obj[4]);
			}

			dto.setFinishAt((Date) obj[5]);

			if ((int) obj[6] == system.getUserId()) {

				if (StringUtils.isNotBlank((String) obj[10])) {
					dto.setPhoneNum((String) obj[10]);
					dto.setTrader("");
				} else {
					dto.setPhoneNum((String) obj[11]);
					dto.setTrader("");
				}

			} else {
				dto.setPhoneNum((String) obj[7] + " " + (String) obj[8]);
				dto.setTrader((String) obj[9]);
			}

			dtos.add(dto);
		}

		map.put("dtos", dtos);

		return map;
	}

	@Override
	public HashMap<String, Object> getTransactionRecordByPage(String period, String type, int userId, int currentPage,
			int pageSize) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String sql = "SELECT " + "t1.transaction_id," + "t1.currency," + "t1.amount," + "t4.currency_unit,"
				+ "t2.transfer_type," + "t2.finish_time," + "t3.user_id," + "t3.area_code," + "t3.user_phone,"
				+ "t3.user_name," + "CONCAT(t2.area_code,' ',t2.phone) as trader,"
				+ "CONCAT(t6.area_code,' ',t6.user_phone) as invitee ";
		StringBuffer sb = new StringBuffer(
				"" + "FROM `e_wallet_seq` t1 " + "LEFT JOIN e_transfer t2 ON t1.transaction_id = t2.transfer_id "
						+ "LEFT JOIN e_user t3 ON if(t1.user_id = t2.user_from,t2.user_to,t2.user_from) = t3.user_id "
						+ "LEFT JOIN e_currency t4 ON t1.currency = t4.currency "
						+ "LEFT JOIN e_transfer t5 ON t5.transfer_id = t2.transfer_comment "
						+ "LEFT JOIN e_user t6 ON t5.user_from  = t6.user_id " + "where " + "t2.transfer_status = ? "
						+ "AND t1.user_id = ? ");

		List<Object> values = new ArrayList<Object>();
		values.add(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
		values.add(userId);

		if (StringUtils.isNotBlank(type)) {

			switch (type) {
			case "expenses":// 支出
				sb.append("and t1.amount < 0 and t2.transfer_type in (0,?) ");
				values.add(ServerConsts.TRANSFER_TYPE_OUT_INVITE + "");
				break;
			case "income":// 收入
				sb.append("and t1.amount > 0 and t2.transfer_type in (0,?) ");
				values.add(ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND + "");
				break;
//			case "withdraw":// 体现
//				sb.append("and t2.transfer_type = ? ");
//				values.add(ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW + "");
//				break;
//			case "recharge":// 充值
//				sb.append("and t2.transfer_type in (?,?) ");
//				values.add(ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE + "");
//				values.add(ServerConsts.TRANSFER_TYPE_IN_PAYPAL_RECHAEGE + "");
//				break;
			default:
				break;

			}

		}

		if (!period.equals("all")) {
			switch (period) {
			case "today":
				sb.append("and t2.finish_time > ?");
				values.add(DateFormatUtils.getStartTime(sdf.format(new Date())));
				break;

			case "lastMonth":
				sb.append("and t2.finish_time > ?");
				Date date = DateFormatUtils.getpreDays(-30);
				values.add(DateFormatUtils.getStartTime(sdf.format(date)));
				break;
			case "last3Month":
				sb.append("and t2.finish_time > ?");
				date = DateFormatUtils.getpreDays(-90);
				values.add(DateFormatUtils.getStartTime(sdf.format(date)));
				break;
			case "lastYear":
				sb.append("and t2.finish_time > ?");
				date = DateFormatUtils.getpreDays(-365);
				values.add(DateFormatUtils.getStartTime(sdf.format(date)));
				break;
			case "aYearAgo":
				sb.append("and t2.finish_time < ?");
				date = DateFormatUtils.getpreDays(-365);
				values.add(DateFormatUtils.getStartTime(sdf.format(date)));
				break;

			default:
				break;
			}
		}

		sb.append(" order by t2.finish_time desc ,t2.transfer_id desc ");

		HashMap<String, Object> map = transferDAO.getTransactionRecordByPage(sql + sb.toString(), sb.toString(), values,
				currentPage, pageSize);
		// 读交易标记
		commonManager.readMsgFlag(userId, 1);
		return map;
	}

	@Override
	public HashMap<String, Object> getNotificationRecordsByPage(int userId, int currentPage, int pageSize) {
		String sql = "SELECT t1.notice_id,t2.area_code,t2.user_phone,"
				+ "t1.currency,t1.amount,t1.create_at,t1.trading_status ";
		StringBuilder sb = new StringBuilder("FROM `e_transaction_notification` t1,`e_user` t2 "
				+ "where t1.sponsor_id = t2.user_id and t1.payer_id = ?");

		List<Object> values = new ArrayList<Object>();
		values.add(userId);

		sb.append(" order by t1.create_at desc");

		HashMap<String, Object> map = notificationDAO.getNotificationRecordsByPage(sql + sb.toString(), sb.toString(),
				values, currentPage, pageSize);
		// 读请求标记
		commonManager.readMsgFlag(userId, 0);
		return map;
	}

	@SuppressWarnings("serial")
	@Override
	public HashMap<String, String> respond2Request(final int userId, final String areaCode, final String userPhone,
			final String currency, final BigDecimal amount, String transferComment, int noticeId) {

		LinkedHashMap<String, Object> args = new LinkedHashMap<>();
		HashMap<String, String> map = new HashMap<>();
		final TransactionNotification notification = notificationDAO.getNotificationById(noticeId);
		if (notification == null) {
			logger.warn("Can not find the corresponding notification information");
			map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
			map.put("msg", "Can not find the corresponding notification information");
			return map;
		}
		args.put("verifyNotificationStatus", new HashMap<String, Object>() {
			{
				put("notification", notification);
				put("userId", userId);
				put("currency", currency);
				put("amount", amount);
			}
		});
		args.put("isTradableCurrency", currency);
		args.put("isAccountFrozened", userId);
		args.put("verifyRecevierStatus", new HashMap<String, Object>() {
			{
				put("sponsorId", notification.getSponsorId());
				put("userId", userId);
				put("areaCode", areaCode);
				put("userPhone", userPhone);
			}
		});
		args.put("isInsufficientBalance", new HashMap<String, Object>() {
			{
				put("userId", userId);
				put("currency", currency);
				put("amount", amount);
			}
		});

		map = test(args);

		if (!map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)) {
			return map;
		}

		map = checkTransferLimit(currency, amount, userId);
		if (!map.isEmpty()) {
			return map;
		}

		// 生成TransId
		String transferId = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_TRANSACTION);

		Transfer transfer = new Transfer();
		transfer.setTransferId(transferId);
		transfer.setCreateTime(new Date());
		transfer.setCurrency(currency);
		transfer.setTransferAmount(amount);
		transfer.setTransferComment(transferComment);
		transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		transfer.setUserFrom(userId);
		transfer.setUserTo(notification.getSponsorId());
		transfer.setAreaCode(areaCode);
		transfer.setPhone(userPhone);
		transfer.setTransferType(ServerConsts.TRANSFER_TYPE_TRANSACTION);
		transfer.setNoticeId(noticeId);
		// 保存
		transferDAO.addTransfer(transfer);
		
		
		// add by Niklaus.chi at 2017/07/26
		User Sponsor = userDAO.getUser(notification.getSponsorId());
		transDetailsManager.addTransDetails(transferId, userId, notification.getSponsorId(), Sponsor.getUserName(),
				areaCode, userPhone, currency, amount, transferComment, ServerConsts.TRANSFER_TYPE_TRANSACTION);
		

		map.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		map.put("msg", "ok");
		map.put("transferId", transferId);

		return map;

	}

	private BigDecimal amountFormatByCurrency(String currency, BigDecimal amoumt) {
		DecimalFormat df1;
		if (StringUtils.isNotBlank(currency) && currency.equals(ServerConsts.CURRENCY_OF_GOLDPAY)) {
			df1 = new DecimalFormat("0");
		} else {
			df1 = new DecimalFormat("0.00");
		}

		return new BigDecimal(df1.format(amoumt));
	}

	private HashMap<String, String> test(Map<String, Object> map) {

		HashMap<String, String> result = new HashMap<>();

		for (Entry<String, Object> entry : map.entrySet()) {

			switch (entry.getKey()) {
			// 检验传入Currency是否可用
			case "isTradableCurrency":
				logger.info("Currency : {}", (String) entry.getValue());
				if (!commonManager.verifyCurrency((String) entry.getValue())) {
					logger.warn("This currency is not a tradable currency");
					result.put("retCode", RetCodeConsts.TRANSFER_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY);
					result.put("msg", "This currency is not a tradable currency");
					return result;
				}
				break;
			// 检验当前用的账户是否被冻结
			case "isAccountFrozened":
				User payer = userDAO.getUser((Integer) entry.getValue());
				if (payer == null || payer.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE) {
					logger.warn("The user does not exist or the account is blocked");
					result.put("retCode", RetCodeConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED);
					result.put("msg", "The user does not exist or the account is blocked");
					return result;
				}
				break;
			// 检验Notification的状态
			case "verifyNotificationStatus":
				@SuppressWarnings("unchecked")
				HashMap<String, Object> args = (HashMap<String, Object>) entry.getValue();

				TransactionNotification notification = (TransactionNotification) args.get("notification");
				int userId = (int) args.get("userId");
				String currency = (String) args.get("currency");
				BigDecimal amount = (BigDecimal) args.get("amount");

				if (notification == null) {
					logger.warn("Can not find the corresponding notification information");
					result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
					result.put("msg", "Can not find the corresponding notification information");
					return result;
				}
				if (notification.getPayerId() != userId
						|| notification.getTradingStatus() == ServerConsts.NOTIFICATION_STATUS_OF_ALREADY_PAID) {
					logger.warn("Order status exception");
					result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
					result.put("msg", "Order status exception");
					return result;
				}
				if (StringUtils.isBlank(currency) || amount.compareTo(new BigDecimal("0")) == 0) {
					logger.warn("The requestor currency and amount information is blank");
					result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
					result.put("msg", "The requestor currency and amount information is blank");
					return result;
				}
				if ((StringUtils.isNotBlank(notification.getCurrency())
						&& notification.getAmount().compareTo(new BigDecimal("0")) != 0)
						&& (!notification.getCurrency().equals(currency)
								|| notification.getAmount().compareTo(amount) != 0)) {
					logger.warn("The input and order information do not match");
					result.put("retCode", RetCodeConsts.TRANSFER_REQUEST_INFORMATION_NOT_MATCH);
					result.put("msg", "The input and order information do not match");
					return result;
				}
				break;
			// 检验接收人的状态
			case "verifyRecevierStatus":
				@SuppressWarnings("unchecked")
				HashMap<String, Object> recevierArgs = (HashMap<String, Object>) entry.getValue();

				int sponsorId = (int) recevierArgs.get("sponsorId");
				userId = (int) recevierArgs.get("userId");
				String areaCode = (String) recevierArgs.get("areaCode");
				String userPhone = (String) recevierArgs.get("userPhone");

				User receiver = userDAO.getUser(sponsorId);
				// 不用给自己转账
				if (receiver != null && userId == receiver.getUserId()) {
					logger.warn("Prohibit transfers to yourself");
					result.put("retCode", RetCodeConsts.TRANSFER_PROHIBIT_TRANSFERS_TO_YOURSELF);
					result.put("msg", "Prohibit transfers to yourself");
					return result;
				}
				if (!receiver.getAreaCode().equals(areaCode) || !receiver.getUserPhone().equals(userPhone)) {
					logger.warn("Payee phone information does not match");
					result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
					result.put("msg", "Payee phone information does not match");
					return result;
				}
				break;
			// 判断余额是否足够支付
			case "isInsufficientBalance":
				@SuppressWarnings("unchecked")
				HashMap<String, Object> insufficientBalanceArgs = (HashMap<String, Object>) entry.getValue();
				userId = (int) insufficientBalanceArgs.get("userId");
				currency = (String) insufficientBalanceArgs.get("currency");
				amount = (BigDecimal) insufficientBalanceArgs.get("amount");
				
				//add by niklaus.chi at 2017-10-16
				if(currency.equals(ServerConsts.CURRENCY_OF_GOLDPAY)){
					GoldpayUserDTO goldpayUser = goldpayTrans4MergeManager.getGoldpayUserInfo(userId);
					if(new BigDecimal(goldpayUser.getBalance()+"").compareTo(amount) == -1){
						logger.warn("Current balance is insufficient");
						result.put("retCode", RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
						result.put("msg", "Current balance is insufficient");
						return result;
					}
					
					
				}else{
					Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, currency);
					if (wallet == null || wallet.getBalance().compareTo(amount) == -1) {
						logger.warn("Current balance is insufficient");
						result.put("retCode", RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
						result.put("msg", "Current balance is insufficient");
						return result;
					}
				}
				
				
				
				break;

			default:
				break;
			}

		}
		result.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		return result;

	}

	@SuppressWarnings("serial")
	// @Async
	private void largeTransWarn(final User payer, final Transfer transfer) {
		BigDecimal transferLimitPerPay = BigDecimal
				.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITPERPAY, 100000d));
		BigDecimal percentage = (oandaRatesManager.getDefaultCurrencyAmount(transfer.getCurrency(),
				transfer.getTransferAmount())).divide(transferLimitPerPay, 5, RoundingMode.DOWN)
						.multiply(new BigDecimal("100"));

		logger.info("percentage : {}", percentage);

		List<CrmAlarm> list = crmAlarmDAO.getConfigListByTypeAndStatus(1, 1);

		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				CrmAlarm crmAlarm = list.get(i);

				if ((crmAlarm.getLowerLimit().compareTo(percentage) == 0
						|| crmAlarm.getLowerLimit().compareTo(percentage) == -1)
						&& crmAlarm.getUpperLimit().compareTo(percentage) == 1) {

					crmAlarmManager.alarmNotice(crmAlarm.getSupervisorIdArr(), "largeTransactionWarning",
							crmAlarm.getAlarmMode(), new HashMap<String, Object>() {
								{
									put("payerMobile", payer.getAreaCode() + payer.getUserPhone());
									put("payeeMobile", transfer.getAreaCode() + transfer.getPhone());
									put("amount", transfer.getTransferAmount().toString());
									put("currency", transfer.getCurrency());
								}
							});

				}

			}
		}
	}

	@Override
	public HashMap<String, String> regenerateQRCode(String currency, BigDecimal amount) {

		HashMap<String, String> map = new HashMap<>();

		BigDecimal transferLimitPerPay = BigDecimal
				.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITPERPAY, 100000d));

		Currency unit = commonManager.getCurreny("USD");

		if (unit == null) {
			map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
			map.put("msg", "fail");
			return map;
		}

		logger.warn("transferLimitPerPay : {}", transferLimitPerPay);
		if ((oandaRatesManager.getDefaultCurrencyAmount(currency, amount)).compareTo(transferLimitPerPay) == 1) {
			logger.warn("Exceeds the maximum amount of each transaction");
			map.put("retCode", RetCodeConsts.TRANSFER_LIMIT_EACH_TIME);
			map.put("msg", transferLimitPerPay.setScale(2).toString());
			map.put("unit", unit.getCurrencyUnit());
			return map;
		}
		map.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		map.put("msg", "OK");
		return map;
	}

	@Override
	public Object getTransfer(String transferId) {
		return transferDAO.getTransferByIdJoinUser(transferId);
	}

	private HashMap<String, String> checkTransferLimit(String currency, BigDecimal amount, int userId) {
		HashMap<String, String> map = new HashMap<>();
		Currency unit = commonManager.getCurreny("USD");
		// 每次支付金额限制
		BigDecimal transferLimitPerPay = BigDecimal
				.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITPERPAY, 100000d));
		logger.warn("transferLimitPerPay : {}", transferLimitPerPay);
		if ((oandaRatesManager.getDefaultCurrencyAmount(currency, amount)).compareTo(transferLimitPerPay) == 1) {
			logger.warn("Exceeds the maximum amount of each transaction");
			map.put("retCode", RetCodeConsts.TRANSFER_LIMIT_EACH_TIME);
			map.put("msg", transferLimitPerPay.setScale(2).toString());
			map.put("unit", unit.getCurrencyUnit());
			return map;
		}

		// 每天累计金额限制
		BigDecimal transferLimitDailyPay = BigDecimal
				.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITDAILYPAY, 100000d));
		BigDecimal accumulatedAmount = transferDAO.getAccumulatedAmount("transfer_" + userId);
		logger.warn("transferLimitDailyPay : {},accumulatedAmount : {} ", transferLimitDailyPay, accumulatedAmount);
		if ((accumulatedAmount.add(oandaRatesManager.getDefaultCurrencyAmount(currency, amount)))
				.compareTo(transferLimitDailyPay) == 1) {
			logger.warn("More than the maximum daily transaction limit");
			map.put("retCode", RetCodeConsts.TRANSFER_LIMIT_DAILY_PAY);
			map.put("msg", transferLimitDailyPay.setScale(2).toString());
			map.put("thawTime", DateFormatUtils.getIntervalDay(new Date(), 1).getTime() + "");
			map.put("unit", unit.getCurrencyUnit());
			return map;
		}
		// 每天累计给付次数限制
		Double transferLimitNumOfPayPerDay = configManager
				.getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITNUMBEROFPAYPERDAY, 100000d);
		Integer dayTradubgVolume = transferDAO.getCumulativeNumofTimes("transfer_" + userId);
		logger.warn("transferLimitNumOfPayPerDay : {},dayTradubgVolume : {} ", transferLimitNumOfPayPerDay,
				dayTradubgVolume);
		if (transferLimitNumOfPayPerDay <= new Double(dayTradubgVolume)) {
			logger.warn("Exceeds the maximum number of transactions per day");
			map.put("retCode", RetCodeConsts.TRANSFER_LIMIT_NUM_OF_PAY_PER_DAY);
			map.put("msg", (transferLimitNumOfPayPerDay).intValue() + "");
			map.put("thawTime", DateFormatUtils.getIntervalDay(new Date(), 1).getTime() + "");
			return map;
		}
		return map;
	}

	@Override
	@SuppressWarnings("unused")
	public TransDetailsDTO getTransDetails(String transferId, int userId) {

		Transfer transfer = transferDAO.getTransferById(transferId);
		User user = userDAO.getUser(userId);

		if (transfer.getUserFrom() != userId && transfer.getUserTo() != userId) {
			return null;
		}

		List<?> list = transDetailsDAO.getTransDetailsByTransIdAndUserId(userId, transferId);
		if (list == null || list.isEmpty()) {
			return null;
		}

		TransDetailsDTO dto = new TransDetailsDTO();
		Object[] obj = (Object[]) list.get(0);

		dto.setUserId(userId);
		dto.setTransId(transferId);
		dto.setTransCurrency((String) obj[2]);
		dto.setTransAmount((BigDecimal) obj[3]);
		dto.setTransUnit((String) obj[4]);
		dto.setTransRemarks(((String) obj[5] == null? "":(String) obj[5]));
		dto.setTransType((Integer) obj[6]);
		dto.setCreateTime((Date) obj[7]);
		dto.setFinishTime((Date) obj[8]);
		dto.setTraderName((String) obj[9]);
		dto.setTraderAreaCode((String) obj[10]);
		dto.setTraderPhone((String) obj[11]);
		dto.setGoldpayName((String) obj[12]);
		dto.setPaypalCurrency((String) obj[13]);
		dto.setPaypalExchange((BigDecimal) obj[14]);


		if (user.getAreaCode().equals(dto.getTraderAreaCode()) && user.getUserPhone().equals(dto.getTraderPhone())) {
			dto.setFriend(true);
			dto.setRegistered(true);
		} else {
			User otherOne = userDAO.getUserByUserPhone(dto.getTraderAreaCode(), dto.getTraderPhone());
			if (otherOne != null) {
				Friend friend = friendDAO.getFriendByUserIdAndFrindId(userId, otherOne.getUserId());
				if (friend != null) {
					dto.setFriend(true);
				} else {
					dto.setFriend(false);
				}
				dto.setRegistered(true);
			} else {
				dto.setFriend(false);
				dto.setRegistered(false);
//				// 此时有可能用户已经修改手机号
//				User systemUser = userDAO.getSystemUser();
//				if (userId == transfer.getUserFrom() && systemUser.getUserId() != transfer.getUserTo()) {
//					Friend friend = friendDAO.getFriendByUserIdAndFrindId(userId, transfer.getUserTo());
//					if (friend != null) {
//						dto.setFriend(true);
//					} else {
//						dto.setFriend(false);
//					}
//					dto.setRegistered(true);
//				} else if (userId == transfer.getUserFrom() && systemUser.getUserId() == transfer.getUserTo()) {
//					// 对方之前未注册，注册后还改了手机号
//					dto.setFriend(false);
//					dto.setRegistered(false);
//
//				} else if (userId == transfer.getUserTo() && systemUser.getUserId() != transfer.getUserFrom()) {
//					Friend friend = friendDAO.getFriendByUserIdAndFrindId(userId, transfer.getUserFrom());
//					if (friend != null) {
//						dto.setFriend(true);
//					} else {
//						dto.setFriend(false);
//					}
//					dto.setRegistered(true);
//				} else if (userId == transfer.getUserTo() && systemUser.getUserId() == transfer.getUserFrom()) {
//					// 系统退款
//					if (transfer.getTransferType() == ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND) {
//						dto.setFriend(false);
//						dto.setRegistered(false);
//					} else {// 本登陆用户在该交易之前未注册，并且还修改过手机号
//						Transfer transfer2 = transferDAO.getTransferById(transfer.getTransferComment());
//						if (transfer2 == null) {
//							dto.setFriend(false);
//						} else {
//							Friend friend = friendDAO.getFriendByUserIdAndFrindId(userId, transfer2.getUserFrom());
//							if (friend != null) {
//								dto.setFriend(true);
//							} else {
//								dto.setFriend(false);
//							}
//						}
//						dto.setRegistered(true);
//					}
//				} else {
//					dto.setFriend(false);
//					dto.setRegistered(false);
//				}
			}
		}

		return dto;

	}

	@Override
	public String updateSystemPhone(String transferId, String phoneNum) {
		User system = userDAO.getSystemUser();
		if ((system != null && StringUtils.isNotBlank(phoneNum))
				&& (phoneNum.equals(system.getAreaCode() + " " + system.getUserPhone()))) {
			Unregistered unregistered = unregisteredDAO.getUnregisteredByTransId(transferId);
			if (unregistered != null) {
				return unregistered.getAreaCode() + " " + unregistered.getUserPhone();
			}

		}
		return phoneNum;
	}

	@Override
	public BigDecimal getAccumulatedAmount(String key) {
		BigDecimal accumulatedAmount = transferDAO.getAccumulatedAmount(key);
		return accumulatedAmount;
	}

	@Override
	public HashMap<String, Object> getTransactionRecordByPageNew(String period, String type, int userId,
			int currentPage, int pageSize) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		StringBuffer sql = new StringBuffer("select t1.transfer_id,t1.trans_currency,t1.trans_amount, ");
		sql.append("t3.currency_unit,t2.transfer_type,t2.finish_time, ");
		sql.append("t1.trader_name,t1.trader_area_code,t1.trader_phone ");

		StringBuffer sb = new StringBuffer("FROM e_trans_details t1 ");
		sb.append("LEFT JOIN e_transfer t2 ON t1.transfer_id = t2.transfer_id ");
		sb.append("LEFT JOIN e_currency t3 ON t1.trans_currency = t3.currency ");
		sb.append("where ");
		sb.append("t2.transfer_status = ? AND t1.user_id = ? ");

		List<Object> values = new ArrayList<Object>();
		values.add(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
		values.add(userId);

		if (StringUtils.isNotBlank(type)) {

			switch (type) {
			case "expenses":// 支出
				sb.append("and t1.trans_amount < 0 and t2.transfer_type in (0,?) ");
				values.add(ServerConsts.TRANSFER_TYPE_OUT_INVITE + "");
				break;
			case "income":// 收入
				sb.append("and t1.trans_amount > 0 and t2.transfer_type in (0,?,?) ");
				values.add(ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND + "");
				values.add(ServerConsts.TRANSFER_TYPE_IN_INVITE_CAMPAIGN + "");
				break;
			case "withdraw":// 体现
				sb.append("and t2.transfer_type = ? ");
				values.add(ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW + "");
				break;
			case "recharge":// 充值
				sb.append("and t2.transfer_type in (?,?) ");
				values.add(ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE + "");
				values.add(ServerConsts.TRANSFER_TYPE_IN_PAYPAL_RECHAEGE + "");
				break;
			default:
				break;

			}

		}

		if (!period.equals("all")) {
			switch (period) {
			case "today":
				sb.append("and t2.finish_time > ?");
				values.add(DateFormatUtils.getStartTime(sdf.format(new Date())));
				break;

			case "lastMonth":
				sb.append("and t2.finish_time > ?");
				Date date = DateFormatUtils.getpreDays(-30);
				values.add(DateFormatUtils.getStartTime(sdf.format(date)));
				break;
			case "last3Month":
				sb.append("and t2.finish_time > ?");
				date = DateFormatUtils.getpreDays(-90);
				values.add(DateFormatUtils.getStartTime(sdf.format(date)));
				break;
			case "lastYear":
				sb.append("and t2.finish_time > ?");
				date = DateFormatUtils.getpreDays(-365);
				values.add(DateFormatUtils.getStartTime(sdf.format(date)));
				break;
			case "aYearAgo":
				sb.append("and t2.finish_time < ?");
				date = DateFormatUtils.getpreDays(-365);
				values.add(DateFormatUtils.getStartTime(sdf.format(date)));
				break;

			default:
				break;
			}
		}

		sb.append(" order by t2.finish_time desc,t2.transfer_id desc ");

		HashMap<String, Object> map = transferDAO.getTransactionRecordByPage(sql.append(sb).toString(), sb.toString(),
				values, currentPage, pageSize);
		// 读交易标记
		commonManager.readMsgFlag(userId, 1);
		return map;
	}

	@Override
	public HashMap<String, Object> getTransactionRecordNew(String period, String type, int userId, int currentPage,
			int pageSize) {

		HashMap<String, Object> map = getTransactionRecordByPageNew(period, type, userId, currentPage, pageSize);

		if (map.isEmpty()) {
			return null;
		}

		List<?> list = (List<?>) map.get("list");
		List<TransferDTO> dtos = new ArrayList<>();

		for (Object object : list) {
			Object[] obj = (Object[]) object;

			TransferDTO dto = new TransferDTO();

			dto.setTransferId((String) obj[0]);
			dto.setCurrency((String) obj[1]);
			dto.setAmount(((BigDecimal) obj[2]).doubleValue());
			dto.setCurrencyUnit((String) obj[3]);

			if ((int) obj[4] == ServerConsts.TRANSFER_TYPE_TRANSACTION && (((BigDecimal) obj[2]).doubleValue()) > 0) {
				dto.setTransferType(1);
			} else {
				dto.setTransferType((int) obj[4]);
			}

			dto.setFinishAt((Date) obj[5]);
			dto.setTrader((String) obj[6] == null ? " ":(String) obj[6]);
			dto.setPhoneNum((String) obj[7] + " " + (String) obj[8]);

			dtos.add(dto);
		}

		map.put("dtos", dtos);

		return map;
	}
	@Override
	public PageBean getRechargeList(int currentPage, String userPhone, String lowerAmount, String upperAmount,
			String startTime, String endTime, String transferType) throws ParseException {
		logger.info("currentPage={},startTime={},endTime={},transferType={}", currentPage, startTime, endTime,
				transferType);

		List<Object> values = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(
				"from Transfer t, User u where t.userTo = u.userId and t.transferStatus = ? and t.transferType = ? ");
		values.add(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
		values.add(Integer.parseInt(transferType));

		if (StringUtils.isNotBlank(userPhone)) {
			hql.append(" and u.userPhone =  ?");
			values.add(userPhone);
		}
		if (StringUtils.isNotBlank(lowerAmount)) {
			hql.append(" and t.transferAmount >=  ?");
			values.add(new BigDecimal(lowerAmount));
		}
		if (StringUtils.isNotBlank(upperAmount)) {
			hql.append(" and t.transferAmount <=  ?");
			values.add(new BigDecimal(upperAmount));
		}
		if (StringUtils.isNotBlank(startTime)) {
			hql.append(" and t.finishTime >=  ?");
			values.add(DateFormatUtils.getStartTime(startTime));
		}
		if (StringUtils.isNotBlank(endTime)) {
			hql.append(" and t.finishTime <= ?");
			values.add(DateFormatUtils.getEndTime(endTime));
		}
		hql.append(" order by t.finishTime desc");
		return transferDAO.searchTransfersByPage(hql.toString(), values, currentPage, 10);
	}
}
