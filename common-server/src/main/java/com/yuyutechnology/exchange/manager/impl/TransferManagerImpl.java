package com.yuyutechnology.exchange.manager.impl;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CrmAlarmDAO;
import com.yuyutechnology.exchange.dao.FriendDAO;
import com.yuyutechnology.exchange.dao.NotificationDAO;
import com.yuyutechnology.exchange.dao.TransDetailsDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UnregisteredDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dto.TransDetailsDTO;
import com.yuyutechnology.exchange.dto.TransferDTO;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.manager.CheckManager;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.manager.TransDetailsManager;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.pojo.CrmAlarm;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.Friend;
import com.yuyutechnology.exchange.pojo.TransactionNotification;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.Unregistered;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.push.PushManager;
import com.yuyutechnology.exchange.sms.SmsManager;
import com.yuyutechnology.exchange.util.DateFormatUtils;
import com.yuyutechnology.exchange.util.page.PageBean;

@Service
public class TransferManagerImpl implements TransferManager {
	
	@Autowired
	UserDAO userDAO;
	@Autowired
	FriendDAO friendDAO;
	@Autowired
	CrmAlarmDAO crmAlarmDAO;
	@Autowired
	TransferDAO transferDAO;
	@Autowired
	UnregisteredDAO unregisteredDAO;
	@Autowired
	NotificationDAO notificationDAO;
	@Autowired
	TransDetailsDAO transDetailsDAO;
	
	@Autowired
	SmsManager smsManager;
	@Autowired
	PushManager pushManager;
	@Autowired
	CheckManager checkManager;
	@Autowired
	CommonManager commonManager;
	@Autowired
	ConfigManager configManager;
	@Autowired
	CrmAlarmManager crmAlarmManager;
	@Autowired
	OandaRatesManager oandaRatesManager;
	@Autowired
	TransDetailsManager transDetailsManager;
	@Autowired
	GoldpayTrans4MergeManager goldpayTrans4MergeManager;
	
	public static Logger logger = LogManager.getLogger(TransferManagerImpl.class);

	@Override
	public HashMap<String, String> transferInitiate(Integer userId, String areaCode, String userPhone, String currency,
			BigDecimal amount, String transferComment, int noticeId) {
		
		HashMap<String, String> map = new HashMap<>();
		
		if (!commonManager.verifyCurrency(currency)) {
			logger.warn("This currency is not a tradable currency");
			map.put("retCode", RetCodeConsts.TRANSFER_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY);
			map.put("msg", "This currency is not a tradable currency");
			return map;
		}
		
		User payer = userDAO.getUser(userId);
		if (payer == null || payer.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE) {
			logger.warn("The user does not exist or the account is blocked");
			map.put("retCode", RetCodeConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED);
			map.put("msg", "The user does not exist or the account is blocked");
			return map;
		}
		
		if(checkManager.isInsufficientBalance(userId, currency, amount)){
			map.put("retCode", RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
			map.put("msg", "Current balance is insufficient");
			return map;
		}
		
		//判断 每次支付金额限制、 每天累计金额限制、每天累计给付次数限制
		map = checkManager.checkTransferLimit(currency, amount, userId);
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

		if (ServerConsts.CURRENCY_OF_GOLDPAY.equals(currency)) {
			goldpayOrderId = goldpayTrans4MergeManager.getGoldpayOrderId();
			if (!StringUtils.isNotBlank(goldpayOrderId)) {
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

			transDetailsManager.addTransDetails(transferId, userId, receiver.getUserId(),
					receiver.getUserName(),areaCode, userPhone, currency, 
					amount,BigDecimal.ZERO,null,transferComment, ServerConsts.TRANSFER_TYPE_TRANSACTION);

		} else {
			User systemUser = userDAO.getSystemUser();
			transfer.setUserTo(systemUser.getUserId());
			transfer.setTransferType(ServerConsts.TRANSFER_TYPE_OUT_INVITE);

			transDetailsManager.addTransDetails(transferId, userId, null, null, 
					areaCode, userPhone, currency, amount,BigDecimal.ZERO,null,
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
	public HashMap<String, String> whenPayPwdConfirmed(Integer userId, String transferId, String userPayPwd) {
		HashMap<String, String> result = new HashMap<>();
		
		User user = userDAO.getUser(userId);
		if (user == null || user.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE) {
			logger.warn("The user does not exist or the account is blocked");
			result.put("msg", "The user does not exist or the account is blocked");
			result.put("retCode", RetCodeConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED);
			return result;
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

	@Override
	public String transferConfirm(Integer userId, String transferId) {
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

		//判断 每次支付金额限制、 每天累计金额限制、每天累计给付次数限制
//		map = checkManager.checkTransferLimit(currency, amount, userId);
//		if (!map.isEmpty()) {
//			return map;
//		}
		
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
//
//		Integer dayTradubgVolume = transferDAO.getCumulativeNumofTimes("transfer_" + userId);
//		logger.warn("transferLimitNumOfPayPerDay : {},dayTradubgVolume : {} ", transferLimitNumOfPayPerDay,
//				dayTradubgVolume);
//		if (transferLimitNumOfPayPerDay <= new Double(dayTradubgVolume)) {
//			logger.warn("Exceeds the maximum number of transactions per day");
//			return RetCodeConsts.TRANSFER_LIMIT_NUM_OF_PAY_PER_DAY;
//		}

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

	@Override
	public HashMap<String, Object> makeRequest(Integer userId, String payerAreaCode, String payerPhone, String currency,
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
	public HashMap<String, String> respond2Request(Integer userId, String areaCode, String userPhone, String currency,
			BigDecimal amount, String transferComment, int noticeId) {
		
		HashMap<String, String> map = new HashMap<>();

		TransactionNotification notification = notificationDAO.getNotificationById(noticeId);
		if (notification == null) {
			logger.warn("Can not find the corresponding notification information");
			map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
			map.put("msg", "Can not find the corresponding notification information");
			return map;
		}
		
		if (!commonManager.verifyCurrency(currency)) {
			logger.warn("This currency is not a tradable currency");
			map.put("retCode", RetCodeConsts.TRANSFER_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY);
			map.put("msg", "This currency is not a tradable currency");
			return map;
		}
		
		User payer = userDAO.getUser(userId);
		if (payer == null || payer.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE) {
			logger.warn("The user does not exist or the account is blocked");
			map.put("retCode", RetCodeConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED);
			map.put("msg", "The user does not exist or the account is blocked");
			return map;
		}
		
		if(checkManager.isInsufficientBalance(userId, currency, amount)){
			map.put("retCode", RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
			map.put("msg", "Current balance is insufficient");
			return map;
		}
		
		
		map = checkManager.checkNotificationStatus(notification, userId, currency, amount);
		if (!map.isEmpty()) {
			return map;
		}

		map = checkManager.checkRecevierStatus(notification.getSponsorId(), userId, areaCode, userPhone);
		if (!map.isEmpty()) {
			return map;
		}
		//判断 每次支付金额限制、 每天累计金额限制、每天累计给付次数限制
		map = checkManager.checkTransferLimit(currency, amount, userId);
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
		
		if(ServerConsts.CURRENCY_OF_GOLDPAY.equals(currency)){
			String goldpayOrderId = goldpayTrans4MergeManager.getGoldpayOrderId();
			transfer.setGoldpayOrderId(goldpayOrderId);
		}
		
		// 保存
		transferDAO.addTransfer(transfer);

		User Sponsor = userDAO.getUser(notification.getSponsorId());
		transDetailsManager.addTransDetails(transferId, userId, notification.getSponsorId(),
				Sponsor.getUserName(),areaCode, userPhone, currency, 
				amount,BigDecimal.ZERO,null, transferComment, ServerConsts.TRANSFER_TYPE_TRANSACTION);

		map.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		map.put("msg", "ok");
		map.put("transferId", transferId);

		return map;
	}

	@Override
	public HashMap<String, Object> getTransRecordbyPage(String period, String type, Integer userId, int currentPage,
			int pageSize) {
		HashMap<String, Object> map = getTransactionRecordByPage(period, type, userId, currentPage, pageSize);

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
			dto.setTrader((String) obj[6] == null ? " " : (String) obj[6]);
			dto.setPhoneNum((String) obj[7] + " " + (String) obj[8]);
			dto.setFee(((BigDecimal) obj[9]).doubleValue());

			dtos.add(dto);
		}

		map.put("dtos", dtos);

		return map;
	}

	@Override
	public HashMap<String, Object> getNotificationRecordsByPage(Integer userId, int currentPage, int pageSize) {
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

	@Override
	public TransDetailsDTO getTransDetails(String transferId, Integer userId) {
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
		dto.setTransRemarks(((String) obj[5] == null ? "" : (String) obj[5]));
		dto.setTransType((Integer) obj[6]);
		dto.setCreateTime((Date) obj[7]);
		dto.setFinishTime((Date) obj[8]);
		dto.setTraderName((String) obj[9]);
		dto.setTraderAreaCode((String) obj[10]);
		dto.setTraderPhone((String) obj[11]);
		dto.setGoldpayName((String) obj[12]);
		dto.setPaypalCurrency((String) obj[13]);
		dto.setPaypalExchange((BigDecimal) obj[14]);
		dto.setTransFee((BigDecimal) obj[15]);
		

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
			}
		}

		return dto;
	}
	
	@Override
	public HashMap<String, String> transInit4ThirdParty(Boolean isRestricted,int payerId, int payeeId,String currency,
			BigDecimal amount,String transferComment,Boolean isFeeDeduction,
			BigDecimal fee,int feepayerId) {
		
		HashMap<String, String> map = new HashMap<>();
		
		if (!commonManager.verifyCurrency(currency)) {
			logger.warn("This currency is not a tradable currency");
			map.put("retCode", RetCodeConsts.TRANSFER_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY);
			map.put("msg", "This currency is not a tradable currency");
			return map;
		}
		
		User payer = userDAO.getUser(payerId);
		if (payer == null || payer.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE) {
			logger.warn("The user does not exist or the account is blocked");
			map.put("retCode", RetCodeConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED);
			map.put("msg", "The user does not exist or the account is blocked");
			return map;
		}
		
		if(checkManager.isInsufficientBalance(payerId, currency, amount)){
			map.put("retCode", RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
			map.put("msg", "Current balance is insufficient");
			return map;
		}
		
		//判断 每次支付金额限制、 每天累计金额限制、每天累计给付次数限制
		
		map = checkManager.checkTransferLimit(currency, amount, payerId);
		if (!map.isEmpty()) {
			return map;
		}

		// 不用给自己转账
		User receiver = userDAO.getUser(payeeId);
		if (receiver != null && payerId == receiver.getUserId()) {
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
		transfer.setUserFrom(payerId);
		transfer.setAreaCode(receiver.getAreaCode());
		transfer.setPhone(receiver.getUserPhone());
		transfer.setGoldpayOrderId(goldpayOrderId);
		transfer.setUserTo(receiver.getUserId());
		transfer.setTransferType(ServerConsts.TRANSFER_TYPE_TRANSACTION);
		transferDAO.addTransfer(transfer);
		// 判断对方是否有该种货币
		commonManager.checkAndUpdateWallet(receiver.getUserId(), currency);

		transDetailsManager.addTransDetails(transferId, payerId, receiver.getUserId(),
				receiver.getUserName(),receiver.getAreaCode(), receiver.getUserPhone(),
				currency, amount,BigDecimal.ZERO,null, transferComment,
				ServerConsts.TRANSFER_TYPE_TRANSACTION);
		
		//手续费订单生成
		if(isFeeDeduction){
			
			//获取手续费账户
			User feeUser = new User();
			
			String goldpayFeeOrderId = goldpayTrans4MergeManager.getGoldpayOrderId();
			if(!StringUtils.isNotBlank(goldpayOrderId)){
				map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
				map.put("msg", "Failed to create goldpay fee order");
				return map;
			}
			
			// 生成TransId
			String transferId4Fee = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_TRANSACTION);
			Transfer transfer4Fee = new Transfer();
			transfer4Fee.setTransferId(transferId4Fee);
			transfer4Fee.setCreateTime(new Date());
			transfer4Fee.setCurrency(ServerConsts.CURRENCY_OF_GOLDPAY);
			transfer4Fee.setTransferAmount(fee);
			//主单TransId
			transfer4Fee.setTransferComment(transferId);
			transfer4Fee.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
			
			transfer4Fee.setUserFrom(feepayerId);
			transfer4Fee.setAreaCode(feeUser.getAreaCode());
			transfer4Fee.setPhone(feeUser.getUserPhone());
			transfer4Fee.setGoldpayOrderId(goldpayFeeOrderId);
			transfer4Fee.setUserTo(feeUser.getUserId());
			transfer4Fee.setTransferType(ServerConsts.TRANSFER_TYPE_IN_FEE);
			transferDAO.addTransfer(transfer4Fee);

			transDetailsManager.addTransDetails(transferId4Fee, feepayerId, feeUser.getUserId(),
					feeUser.getUserName(),feeUser.getAreaCode(), feeUser.getUserPhone(),
					ServerConsts.CURRENCY_OF_GOLDPAY, fee,BigDecimal.ZERO,null, transferId+"订单产生的手续费",
					ServerConsts.TRANSFER_TYPE_IN_FEE);
			
			map.put("transferId4Fee", transferId4Fee);
			
		}
		
		map.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		map.put("msg", "ok");
		map.put("transferId", transferId);
		
		return map;

	}
	
	@Override
	public HashMap<String, String> transConfirm4ThirdParty(Boolean isRestricted,int userId, String transferId, String userPayPwd){
		
		HashMap<String, String> result = new HashMap<>();
		
		User payer = userDAO.getUser(userId);
		Transfer transfer = transferDAO.getTransferById(transferId);
		
		//判断Payer和Transfer的状态
		result = checkManager.checkPayerAndTrasStatus(payer,transfer,userId);
		if(!result.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)){
			return result;
		}
		
		//判断 每次支付金额限制、 每天累计金额限制、每天累计给付次数限制
		result = checkManager.checkTransferLimit(transfer.getCurrency(), transfer.getTransferAmount(), userId);
		if (!result.isEmpty()) {
			return result;
		}
		//根据transferId更新账户		
		goldpayTrans4MergeManager.updateWallet4GoldpayTrans(transferId);

		// 推送到账通知
		User payee = userDAO.getUser(transfer.getUserTo());
		pushManager.push4Transfer(transfer.getTransferId(), payer, payee, transfer.getCurrency(),
				amountFormatByCurrency(transfer.getCurrency(), transfer.getTransferAmount()));
		
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
		
		//扣除手续费
		
		
		result.put("msg", "success");
		result.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public BigDecimal getAccumulatedAmount(String key) {
		BigDecimal accumulatedAmount = transferDAO.getAccumulatedAmount(key);
		return accumulatedAmount;
	}
	
	@Override
	public Object getTransfer(String transferId) {
		return transferDAO.getTransferByIdJoinUser(transferId);
	}
	
	@Override
	public PageBean getRechargeList(int currentPage, String userPhone, String lowerAmount, String upperAmount,
			String startTime, String endTime, String transferType){
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

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public String systemRefundStep1(Unregistered unregistered) {

		Transfer transfer = transferDAO.getTransferById(unregistered.getTransferId());
		if (transfer == null || transfer.getTransferStatus() != ServerConsts.TRANSFER_STATUS_OF_COMPLETED) {
			logger.warn("Did not find the corresponding transfer information");
			return null;
		}

		String transferId2 = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_TRANSACTION);
		User systemUser = userDAO.getSystemUser();

		String goldpayOrderId = null;
		if (ServerConsts.CURRENCY_OF_GOLDPAY.equals(transfer.getCurrency())) {
			goldpayOrderId = goldpayTrans4MergeManager.getGoldpayOrderId();
		}

		// 生成transfer系统退款订单
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

		return transferId2;
	}
	
	@Override
	public void systemRefundStep2(String transferId,Unregistered unregistered) {
		
		Transfer transfer = transferDAO.getTransferById(unregistered.getTransferId());
		
		goldpayTrans4MergeManager.updateWallet4GoldpayTrans(transferId);

		transDetailsManager.addTransDetails(transferId, transfer.getUserFrom(),
				null, null, unregistered.getAreaCode(),unregistered.getUserPhone(), 
				transfer.getCurrency(), transfer.getTransferAmount(),BigDecimal.ZERO,null,
				unregistered.getUserPhone() + "对方逾期未注册,系统退款", ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND);

		// 修改gift记录
		unregistered.setUnregisteredStatus(ServerConsts.UNREGISTERED_STATUS_OF_BACK);
		unregistered.setRefundTransId(transferId);
		unregisteredDAO.updateUnregistered(unregistered);

		// 发送推送
		User payee = userDAO.getUser(transfer.getUserFrom());
		pushManager.push4Refund(payee, transfer.getAreaCode(), transfer.getPhone(), transfer.getCurrency(),
				amountFormatByCurrency(transfer.getCurrency(), transfer.getTransferAmount()));

	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	private BigDecimal amountFormatByCurrency(String currency, BigDecimal amoumt) {
		DecimalFormat df1;
		if (StringUtils.isNotBlank(currency) && currency.equals(ServerConsts.CURRENCY_OF_GOLDPAY)) {
			df1 = new DecimalFormat("0");
		} else {
			df1 = new DecimalFormat("0.00");
		}

		return new BigDecimal(df1.format(amoumt));
	}
	
	@SuppressWarnings("serial")
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
	
	private HashMap<String, Object> getTransactionRecordByPage(String period, String type, int userId,
			int currentPage, int pageSize) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		StringBuffer sql = new StringBuffer("select t1.transfer_id,t1.trans_currency,t1.trans_amount, ");
		sql.append("t3.currency_unit,t2.transfer_type,t2.finish_time, ");
		sql.append("t1.trader_name,t1.trader_area_code,t1.trader_phone,t1.trans_fee ");

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
			case "withdraw":// 提现
				sb.append("and t2.transfer_type = ? ");
				values.add(ServerConsts.TRANSFER_TYPE_IN_WITHDRAW + "");//原来的提现核现在的提取金条合并。suzan-2017/11/15
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

}
