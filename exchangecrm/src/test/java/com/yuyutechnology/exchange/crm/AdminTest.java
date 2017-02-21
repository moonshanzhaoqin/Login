package com.yuyutechnology.exchange.crm;

import org.junit.Test;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.manager.CrmAdminManager;

/**
 * 
 * @author suzan.wu
 *
 */
public class AdminTest extends BaseSpringJunit4 {
	
	public static Logger logger = LogManager.getLogger(AdminTest.class);
	
	@Autowired
	CrmAdminManager crmAdminManager;

	@Test
	public void addAdminTEST() {
		crmAdminManager.addAdmin("admin");
	}

}
