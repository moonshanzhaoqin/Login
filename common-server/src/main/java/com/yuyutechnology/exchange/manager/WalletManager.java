package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import com.yuyutechnology.exchange.util.page.PageBean;

public interface WalletManager {

	/**
	 * @Descrition : 根据用户id获取账号资产对应的黄金总量
	 * @author : nicholas.chi
	 * @time : 2016年12月17日 上午10:36:38
	 * @param userId
	 * @return
	 */
	HashMap<String, BigDecimal> getTotalAmoutGold(int userId);

	void getUserTotalBalance(int systemUserId);

	PageBean getBadAccountByPage(int parseInt);

	List<?> getDetailSeq(Integer badAccountId);

	List<?> getDetailSeqByTransferId(String transferId);

}
