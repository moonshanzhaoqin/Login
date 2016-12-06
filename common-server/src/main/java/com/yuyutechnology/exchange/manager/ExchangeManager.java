package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.List;

import com.yuyutechnology.exchange.pojo.Wallet;

public interface ExchangeManager {
	
	/**
	 * @Descrition : TODO
	 * @author : nicholas.chi
	 * @time : 2016年12月5日 下午4:17:36
	 * @param userId
	 * @return
	 */
	public List<Wallet> getWalletsByUserId(int userId);
	
	/**
	 * @Descrition : TODO
	 * @author : nicholas.chi
	 * @time : 2016年12月5日 下午4:17:40
	 * @param userId
	 * @param currencyOut
	 * @param currencyIn
	 * @param amountOut
	 * @return
	 */
	public String exchangeCalculation(int userId,String currencyOut,
			String currencyIn,BigDecimal amountOut);
	
	/**
	 * @Descrition : TODO
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

}
