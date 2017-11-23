package com.yuyutechnology.exchange.manager;

import org.springframework.scheduling.annotation.Async;

import com.yuyutechnology.exchange.goldpay.msg.GoldpayUserDTO;
import com.yuyutechnology.exchange.pojo.Transfer;

public interface GoldpayTrans4MergeManager {

	GoldpayUserDTO getGoldpayUserInfo(Integer exUserId);

	String getGoldpayOrderId();

	void updateWallet4GoldpayTrans(String transferId);
	
	@Async
	void updateWallet4GoldpayExchange(String exchangeId,Integer systemUserId);

	GoldpayUserDTO createGoldpay(String areaCode, String userPhone, String userName, boolean newUser);

}
