/**
 * 
 */
package com.yuyutechnology.exchange.server.test;

import org.junit.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.server.controller.TransferController;

/**
 * @author silent.sun
 *
 */
public class ExchangeRateManagerTest extends BaseSpringJunit4 {

	@Autowired
	UserDAO userDAO;

	public static Logger logger = LogManager.getLogger(TransferController.class);

	@Test
	public void testUpdateRate() {
		
	}

}
