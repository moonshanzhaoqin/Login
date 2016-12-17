package com.yuyutechnology.exchange.manager;

public interface WalletManager {
	
	/**
	 * @Descrition : 根据用户id获取账号资产对应的黄金总量
	 * @author : nicholas.chi
	 * @time : 2016年12月17日 上午10:36:38
	 * @param userId
	 * @return
	 */
	public double getTotalAmoutGold(int userId);
	
}
