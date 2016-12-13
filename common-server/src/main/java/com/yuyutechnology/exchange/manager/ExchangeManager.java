package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import com.yuyutechnology.exchange.pojo.Wallet;

public interface ExchangeManager {
	
	/**
	 * @Descrition : 根据UserId获取用户的wallets
	 * @author : nicholas.chi
	 * @time : 2016年12月5日 下午4:17:36
	 * @param userId
	 * @return
	 */
	public List<Wallet> getWalletsByUserId(int userId);
	
	/**
	 * @Descrition : 兑换金额计算
	 * @author : nicholas.chi
	 * @time : 2016年12月5日 下午4:17:40
	 * @param userId
	 * @param currencyOut
	 * @param currencyIn
	 * @param amountOut
	 * @return
	 */
	public HashMap<String, String> exchangeCalculation(int userId,String currencyOut,
			String currencyIn,BigDecimal amountOut);
	
	/**
	 * @Descrition : 兑换交易确认
	 * @author : nicholas.chi
	 * @time : 2016年12月5日 下午5:20:43
	 * @param userId
	 * @param currencyOut
	 * @param currencyIn
	 * @param amountOut
	 * @param amountIn
	 * @return
	 */
	public String exchangeConfirm(int userId,String currencyOut,
			String currencyIn,BigDecimal amountOut,BigDecimal amountIn);

	/**
	 * @Descrition : TODO
	 * @author : nicholas.chi
	 * @time : 2016年12月9日 下午4:35:28
	 * @param currencyOut
	 * @param currencyIn
	 * @param outAmount
	 * @param capitalFlows
	 * @return
	 */
	HashMap<String, BigDecimal> exchangeCalculation(String currencyOut, String currencyIn, BigDecimal outAmount,
			int capitalFlows);



}
