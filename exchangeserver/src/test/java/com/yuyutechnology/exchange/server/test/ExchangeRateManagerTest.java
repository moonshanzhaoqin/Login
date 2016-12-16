/**
 * 
 */
package com.yuyutechnology.exchange.server.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.dao.UnregisteredDAO;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.ExchangeRateManager;
import com.yuyutechnology.exchange.manager.TransferManager;
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

	public static Logger logger = LoggerFactory.getLogger(TransferController.class);

	@Test
	public void testUpdateRate() {

		// exchangeRateManager.updateExchangeRateNoGoldq();
		// exchangeRateManager.updateGoldpayExchangeRate();

		// String result = transferManager.transferInitiate(2,
		// "+86","12312312336",
		// "CNY", new BigDecimal(100),"test",0);
		// logger.info("testResult : {}",result);
		// String result = transferManager.payPwdConfirm(2, "201612140T000002",
		// "123456");
		// logger.info("testResult : {}",result);
		// transferManager.systemRefundBatch();

//		transferManager.getTransactionRecordByPage("today", 2, 1, 10);
		
		exchangeManager.getExchangeRecordsByPage(2, "lastMonth", 1, 10);
	}

}
