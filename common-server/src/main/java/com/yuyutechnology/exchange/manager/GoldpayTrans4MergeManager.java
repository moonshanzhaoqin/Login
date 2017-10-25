package com.yuyutechnology.exchange.manager;

import org.springframework.scheduling.annotation.Async;

import com.yuyutechnology.exchange.goldpay.msg.GoldpayUserDTO;

public interface GoldpayTrans4MergeManager {

	GoldpayUserDTO getGoldpayUserInfo(Integer exUserId);

	String getGoldpayOrderId();

	@Async
	void updateWallet4GoldpayTrans(String transferId);
	
	@Async
	void updateWallet4GoldpayExchange(String exchangeId,Integer systemUserId);

	GoldpayUserDTO createGoldpay(String areaCode, String userPhone, boolean newUser);
}
