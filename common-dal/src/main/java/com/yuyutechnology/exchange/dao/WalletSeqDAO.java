package com.yuyutechnology.exchange.dao;

import java.math.BigDecimal;
import java.util.List;

import com.yuyutechnology.exchange.pojo.WalletSeq;

public interface WalletSeqDAO {
	
	/**
	 * @Descrition : 
	 * @author : nicholas.chi
	 * @time : 2016年12月7日 上午11:49:38
	 * @param walletSeq
	 */
	public void addWalletSeq(WalletSeq walletSeq);

	public List<?> getWalletSeq(int userId, String currency, long startSeqId, long endSeqId);
	
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
//	public void addWalletSeq4Exchange(int userId, int transferType, String transactionId, 
//			String currencyOut, BigDecimal amountOut,
//			String currencyIn, BigDecimal amountIn);
	
	/**
	 * @Descrition : 交易过程中产生的两条交易记录
	 * @author : nicholas.chi
	 * @time : 2016年12月7日 上午11:53:41
	 * @param payerId 付款人Id
	 * @param payeeId 收款人Id
	 * @param transferType
	 * @param transactionId
	 * @param currency
	 * @param amount
	 */
//	public void addWalletSeq4Transaction(int payerId,int payeeId,int transferType, 
//			String transactionId, String currency,BigDecimal amount );

}
