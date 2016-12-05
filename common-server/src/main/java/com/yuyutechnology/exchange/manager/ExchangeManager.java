package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.List;

import com.yuyutechnology.exchange.pojo.Wallet;

public interface ExchangeManager {
	
	public List<Wallet> getWalletsByUserId(int userId);
	
	public void exchangeCalculation(int userId,String currencyOut,String currencyIn,BigDecimal amountOut);
	
	

}
