/**
 * 
 */
package com.yuyutechnology.exchange.server.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
		exchangeRateManager.updateExchangeRate();
		exchangeRateManager.getExchangeRate("CNY", "USD");
	}
}
