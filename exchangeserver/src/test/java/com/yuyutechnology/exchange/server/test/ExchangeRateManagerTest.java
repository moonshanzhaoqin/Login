/**
 * 
 */
package com.yuyutechnology.exchange.server.test;

import java.math.BigDecimal;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UnregisteredDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.ExchangeRateManager;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.manager.WalletManager;
import com.yuyutechnology.exchange.server.controller.TransferController;

/**
 * @author silent.sun
 *
 */
public class ExchangeRateManagerTest extends BaseSpringJunit4 {

	@Autowired
	UnregisteredDAO unregisteredDAO;
	@Autowired
	ExchangeManager exchangeManager;
	@Autowired
	ExchangeRateManager exchangeRateManager;
	@Autowired
	TransferManager transferManager;
	@Autowired
	WalletManager walletManager;
	
	@Autowired
	TransferDAO transferDAO;
	
	
	@Autowired
	UserDAO userDAO;

	public static Logger logger = LoggerFactory.getLogger(TransferController.class);

	@Test
	public void testUpdateRate() {

		BigDecimal sum = transferDAO.sumGoldpayTransAmount(5);
		logger.info("sum : {}",sum);
		 
	}

}
