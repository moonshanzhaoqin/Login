package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;

import com.yuyutechnology.exchange.goldpay.trans4merge.GoldpayUserDTO;

public interface GoldpayTrans4MergeManager {

	GoldpayUserDTO getGoldpayUserInfo(Integer exUserId);

	String getGoldpayOrderId();

	Integer goldpayTransaction(BigDecimal balance, String payOrderId, 
			String toAccountNum, String comment, String fromAccountNum);



}
