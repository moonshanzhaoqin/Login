package com.yuyutechnology.exchange.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import com.yuyutechnology.exchange.pojo.Wallet;

public interface WalletDAO {

	/**
	 * @Descrition :
	 * @author : nicholas.chi
	 * @time : 2016年12月2日 下午5:15:20
	 * @param userId
	 * @return
	 */
	public List<Wallet> getWalletsByUserId(int userId);

	public Wallet getWalletByUserIdAndCurrency(int userId, String currency);

	public Integer updateWalletByUserIdAndCurrency(int userId, String currency, BigDecimal amount, String capitalFlows,
			int transferType, String transactionId);

	public void addwallet(Wallet wallet);

	/**
	 * 获取各个币种的用户资产总和
	 * 
	 * @param systemUserId
	 * @return
	 */
	public HashMap<String, BigDecimal> getUserAccountTotalAssets(int systemUserId);

	Integer emptyWallet(int userId, String currency);

}
