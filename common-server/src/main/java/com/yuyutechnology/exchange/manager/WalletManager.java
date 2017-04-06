package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.HashMap;

import com.yuyutechnology.exchange.utils.page.PageBean;

public interface WalletManager {
	
	/**
	 * @Descrition : 根据用户id获取账号资产对应的黄金总量
	 * @author : nicholas.chi
	 * @time : 2016年12月17日 上午10:36:38
	 * @param userId
	 * @return
	 */
	public HashMap<String, BigDecimal> getTotalAmoutGold(int userId);
	
	public void getUserTotalBalance(int systemUserId);

	public PageBean getBadAccountByPage(int parseInt);
	
}
