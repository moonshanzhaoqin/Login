package com.yuyutechnology.exchange.dao;

import java.math.BigDecimal;
import java.util.List;

import com.yuyutechnology.exchange.pojo.Wallet;

public interface WalletDAO {
	
	/**
	 * @Descrition : TODO
	 * @author : nicholas.chi
	 * @time : 2016年12月2日 下午5:15:20
	 * @param userId
	 * @return
	 */
	public List<Wallet> getWalletsByUserId(int userId);
	
	public Wallet getWalletByUserIdAndCurrency(int userId,String currency);
	
	public void updateWalletByUserIdAndCurrency(int userId,String currency,BigDecimal amount,String capitalFlows);

}
