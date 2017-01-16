package com.yuyutechnology.exchange.crm;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.manager.CrmAdminManager;
import com.yuyutechnology.exchange.manager.ExchangeRateManager;

/**
 * 
 * @author suzan.wu
 *
 */
public class AdminTest extends BaseSpringJunit4 {
	public static Logger logger = LoggerFactory.getLogger(AdminTest.class);
	@Autowired
	CrmAdminManager adminManager;
	@Autowired
	ExchangeRateManager exchangeRateManager;

	 @Test
	 public void addAdminTEST() {
//	 adminManager.addAdmin("admin");
	 }

}
