/**
 * 
 */
package com.yuyutechnology.exchange.server.test;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.UnregisteredDAO;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.ExchangeRateManager;

/**
 * @author silent.sun
 *
 */
public class ExchangeRateManagerTest extends BaseSpringJunit4{

	
	@Autowired
	UnregisteredDAO unregisteredDAO;
	@Autowired
	ExchangeManager exchangeManager;
	@Autowired
	ExchangeRateManager exchangeRateManager;
	
	
	@Test	
	public void testUpdateRate(){
		
		exchangeRateManager.updateExchangeRateNoGoldq();
		exchangeRateManager.updateGoldpayExchangeRate();
//
//		exchangeManager.exchangeCalculation("USD","CNY",new BigDecimal(1),0);
//		
//		exchangeManager.exchangeCalculation("USD","CNY",new BigDecimal(2),0);
		
		exchangeManager.exchangeCalculation(ServerConsts.CURRENCY_OF_GOLDPAY,"USD",new BigDecimal(324),0);
		
		exchangeManager.exchangeCalculation("USD",ServerConsts.CURRENCY_OF_GOLDPAY,new BigDecimal(324),0);
		
	}
}
