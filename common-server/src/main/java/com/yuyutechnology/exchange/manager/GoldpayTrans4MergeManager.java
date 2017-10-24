package com.yuyutechnology.exchange.manager;

import org.springframework.scheduling.annotation.Async;

import com.yuyutechnology.exchange.goldpay.trans4merge.GoldpayUserDTO;

public interface GoldpayTrans4MergeManager {

	GoldpayUserDTO getGoldpayUserInfo(Integer exUserId);

	String getGoldpayOrderId();

	@Async
	void updateWallet4GoldpayTrans(String transferId);
	
	@Async
	void updateWallet4GoldpayExchange(String exchangeId,Integer systemUserId);
}