package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.HashMap;

import com.yuyutechnology.exchange.goldpay.trans4merge.GoldpayUserDTO;

public interface GoldpayTrans4MergeManager {

	GoldpayUserDTO getGoldpayUserInfo(Integer exUserId);

	String getGoldpayOrderId();

	Integer goldpayTransaction(String fromAccountNum, String toAccountNum, 
			BigDecimal balance, String payOrderId,String comment);
	
	HashMap<String, String> updateWalletByUserIdAndCurrency(
			Integer payerId,String currencyOut,
			Integer payeeId,String currencyIn, BigDecimal amount,
			int transferType, String transactionId,boolean isUpdateWallet,String goldpayOrderId);





}
