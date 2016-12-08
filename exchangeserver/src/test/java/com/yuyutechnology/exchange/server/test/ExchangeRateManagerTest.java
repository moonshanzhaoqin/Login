/**
 * 
 */
package com.yuyutechnology.exchange.server.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.manager.ExchangeRateManager;

/**
 * @author silent.sun
 *
 */
public class ExchangeRateManagerTest extends BaseSpringJunit4{

	
	@Autowired
	ExchangeRateManager exchangeRateManager;
	
	@Test	
	public void testUpdateRate(){
		exchangeRateManager.updateExchangeRateNoGoldq();
		exchangeRateManager.updateGoldpayExchangeRate();
		
		double oneUSD = exchangeRateManager.getExchangeRate("USD", ServerConsts.CURRENCY_OF_GOLDPAY);
		System.out.println("1 usd for "+oneUSD+" gdp");
		double oneCNY = exchangeRateManager.getExchangeRate("CNY", ServerConsts.CURRENCY_OF_GOLDPAY);
		System.out.println("1 cny for "+oneCNY+" gdp");
		
	}
}
