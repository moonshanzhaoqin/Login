package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

public interface SpareExchangeRateManager {
	
	/**
	 * 
	 */
	public void updateExchangeRate(boolean refresh);

	/**
	 * @Descrition : 
	 * @author : nicholas.chi
	 * @time : 2016年12月9日 下午3:15:27
	 * @param base
	 * @param outCurrency
	 * @return
	 */
	public double getExchangeRate(String base,String outCurrency);
	
	/**
	 * @return
	 */
	public Date getExchangeRateUpdateDate();
	
	/**
	 * @Descrition : 
	 * @author : nicholas.chi
	 * @time : 2016年12月17日 上午10:47:56
	 * @param base
	 * @return
	 */
	public HashMap<String, Double> getExchangeRate(String base);
	
	/**
	 * @Descrition : 将交易金额兑换为默认币种
	 * @author : nicholas.chi
	 * @time : 2016年12月17日 上午10:47:59
	 * @param transCurrency
	 * @param transAmount
	 * @return
	 */
	public BigDecimal getExchangeResult(String transCurrency,BigDecimal transAmount);

	/**
	 * @Descrition : 
	 * @author : nicholas.chi
	 * @time : 2016年12月17日 下午5:21:10
	 * @param userId
	 * @return
	 */
	public BigDecimal getTotalBalance(int userId);

}
