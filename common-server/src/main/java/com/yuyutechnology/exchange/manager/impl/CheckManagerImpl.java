package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.goldpay.msg.GoldpayUserDTO;
import com.yuyutechnology.exchange.manager.CheckManager;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.TransactionNotification;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.util.DateFormatUtils;

@Service
public class CheckManagerImpl implements CheckManager {
	
	@Autowired
	UserDAO userDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	TransferDAO transferDAO;
	
	
	@Autowired
	CommonManager commonManager;
	@Autowired
	ConfigManager configManager;
	@Autowired
	OandaRatesManager oandaRatesManager;
	@Autowired
	GoldpayTrans4MergeManager goldpayTrans4MergeManager;
	
	public static Logger logger = LogManager.getLogger(CheckManagerImpl.class);
	
	@Override
	public boolean isInsufficientBalance(Integer userId,String currency,BigDecimal amount){
		
		if (currency.equals(ServerConsts.CURRENCY_OF_GOLDPAY)) {
			GoldpayUserDTO goldpayUser = goldpayTrans4MergeManager.getGoldpayUserInfo(userId);
			if (new BigDecimal(goldpayUser.getBalance() + "").compareTo(amount) == -1) {
				logger.warn("Current balance is insufficient");
				return false;
			}

		} else {
			Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, currency);
			if (wallet == null || wallet.getBalance().compareTo(amount) == -1) {
				logger.warn("Current balance is insufficient");
				return false;
			}
		}
		
		return true;
	}

	@Override
	public HashMap<String, String> checkTransferLimit(String currency, BigDecimal amount, int userId) {
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
	public HashMap<String, String> checkNotificationStatus(TransactionNotification notification,Integer userId,
			String currency,BigDecimal amount){
		
		HashMap<String, String> result = new HashMap<>();
		
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
		
		result.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		return result;
	}
	
	@Override
	public HashMap<String, String> checkRecevierStatus(Integer sponsorId,Integer userId,
			String areaCode,String userPhone){
		
		HashMap<String, String> result = new HashMap<>();
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
		
		result.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		return result;
		
	}
	
	@Override
	public HashMap<String, String> checkPayerAndTrasStatus(User payer,Transfer transfer,int userId){
		HashMap<String, String> result = new HashMap<>();
		
		if (payer == null || payer.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE) {
			logger.warn("The user does not exist or the account is blocked");
			result.put("msg", "The user does not exist or the account is blocked");
			result.put("retCode", RetCodeConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED);
			return result;
		}

		if (transfer == null) {
			logger.warn("The transaction order does not exist");
			result.put("msg", "The transaction order does not exist");
			result.put("retCode", RetCodeConsts.TRANSFER_TRANS_ORDERID_NOT_EXIST);
			return result;
		}

		if(transfer.getTransferStatus()!= ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION){
			logger.warn("Orders have been paid");
			result.put("msg", "Orders have been paid");
			result.put("retCode", RetCodeConsts.TRANSFER_ORDERS_HAVE_BEEN_PAID);
			return result;
		}
		
		if (userId != transfer.getUserFrom()) {
			logger.warn("userId is different from UserFromId");
			result.put("msg", "userId is different from UserFromId");
			result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
			return result;
		}
		
		result.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		return result;
	}
	
}
