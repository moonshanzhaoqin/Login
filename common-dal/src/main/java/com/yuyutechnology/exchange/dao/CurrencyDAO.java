package com.yuyutechnology.exchange.dao;

import com.yuyutechnology.exchange.pojo.Currency;

import java.util.List;

public interface CurrencyDAO {
	
	/**
	 * @Descrition : TODO
	 * @author : nicholas.chi
	 * @time : 2016年12月2日 下午5:09:37
	 * @param currency
	 * @return
	 */
	public Currency getCurrency(String currency);
	
	/**
	 * @Descrition : TODO
	 * @author : nicholas.chi
	 * @time : 2016年12月2日 下午5:09:43
	 * @return
	 */
	public List<Currency> getCurrencys();

	public List<Currency> getCurrentCurrency();

	public void updateCurrency(Currency currency);

}
