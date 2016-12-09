/**
 * 
 */
package com.yuyutechnology.exchange.server.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.dao.UnregisteredDAO;

/**
 * @author silent.sun
 *
 */
public class ExchangeRateManagerTest extends BaseSpringJunit4{

	
	@Autowired
	UnregisteredDAO unregisteredDAO;
	
	@Test	
	public void testUpdateRate(){

		
		unregisteredDAO.getUnregisteredByUserPhone("+86", "18818218259");
	}
}
