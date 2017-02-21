/**
 * 
 */
package com.yuyutechnology.exchange.server.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	public static Logger logger = LoggerFactory.getLogger(TransferController.class);

	@Test
	public void testUpdateRate() {
		
	}

}
