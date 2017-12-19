package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.HashMap;

import com.yuyutechnology.exchange.pojo.TransactionNotification;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.User;

public interface CheckManager {

	boolean isInsufficientBalance(Integer userId, String currency, BigDecimal amount);
	
	boolean isPaymentVerification(Integer userId, String currency, BigDecimal transAmount);
	
	HashMap<String, String> checkTransferLimit(String currency, BigDecimal amount, int userId);

	HashMap<String, String> checkNotificationStatus(TransactionNotification notification, Integer userId,
			String currency, BigDecimal amount);

	HashMap<String, String> checkRecevierStatus(Integer sponsorId, Integer userId, String areaCode, String userPhone);

	HashMap<String, String> checkPayerAndTrasStatus(User payer, Transfer transfer, int userId);

}
