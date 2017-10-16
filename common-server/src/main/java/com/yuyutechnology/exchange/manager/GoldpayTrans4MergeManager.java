package com.yuyutechnology.exchange.manager;

import com.yuyutechnology.exchange.goldpay.trans4merge.GoldpayUserDTO;

public interface GoldpayTrans4MergeManager {

	GoldpayUserDTO getGoldpayUserInfo(Integer exUserId);

	String getGoldpayOrderId();

	void goldpayTransaction();

}
