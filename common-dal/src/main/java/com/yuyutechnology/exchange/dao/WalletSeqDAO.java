package com.yuyutechnology.exchange.dao;

import java.math.BigDecimal;

import com.yuyutechnology.exchange.pojo.WalletSeq;

public interface WalletSeqDAO {
	
	public void addWalletSeq(WalletSeq walletSeq);
	
	/**
	 * @Descrition : 添加兑换交易中一方的进出两条交易记录
	 * @author : nicholas.chi
	 * @time : 2016年12月6日 下午1:18:17
	 * @param userId
	 * @param transferType
	 * @param transactionId
	 * @param currencyOut
	 * @param amountOut
	 * @param currencyIn
	 * @param amountIn
	 */
	void addWalletSeq(int userId, int transferType, String transactionId, 
			String currencyOut, BigDecimal amountOut,
			String currencyIn, BigDecimal amountIn);

}
