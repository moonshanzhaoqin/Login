package com.yuyutechnology.exchange.manager;

import java.util.HashMap;
import java.util.List;

import org.springframework.scheduling.annotation.Async;

import com.yuyutechnology.exchange.goldpay.msg.GoldpayUserDTO;

public interface GoldpayTrans4MergeManager {

	GoldpayUserDTO getGoldpayUserAccount(Integer exUserId);

	String getGoldpayOrderId();

	void updateWallet4GoldpayTrans(String transferId);
	
	@Async
	void updateWallet4GoldpayExchange(String exchangeId,Integer systemUserId);

	GoldpayUserDTO createGoldpay(String areaCode, String userPhone, String userName, boolean newUser);

	void updateWallet4GoldpayTransList(List<String> transferIds);

	HashMap<String, String> updateWallet4FeeTrans(String transferId, String feeTransferId);

}
