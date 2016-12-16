package com.yuyutechnology.exchange.manager;

import java.util.HashMap;

public interface ExchangeRateManager {
	
	/**
	 * @Descrition : TODO
	 * @author : nicholas.chi
	 * @time : 2016年12月9日 下午3:15:20
	 */
	public void updateExchangeRateNoGoldq();
	
	/**
	 * @Descrition : TODO
	 * @author : nicholas.chi
	 * @time : 2016年12月9日 下午3:15:23
	 */
	public void updateGoldpayExchangeRate();

	/**
	 * @Descrition : TODO
	 * @author : nicholas.chi
	 * @time : 2016年12月9日 下午3:15:27
	 * @param base
	 * @param outCurrency
	 * @return
	 */
	public double getExchangeRate(String base,String outCurrency);
	
	
	public HashMap<String, Double> getExchangeRate(String base);

}
