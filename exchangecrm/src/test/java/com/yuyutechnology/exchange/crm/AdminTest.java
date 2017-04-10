package com.yuyutechnology.exchange.crm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.dao.TransferDAO;

/**
 * 
 * @author suzan.wu
 *
 */
public class AdminTest extends BaseSpringJunit4 {
	
	public static Logger logger = LogManager.getLogger(AdminTest.class);
	
//	@Autowired
//	CrmAdminManager crmAdminManager;
	@Autowired
	TransferDAO transferDAO;
	
	
//	@Test
//	public void addAdminTEST() {
//		crmAdminManager.addAdmin("admin");
//	}
//	@Test
//	public void test(){
//		transferDAO.testByPage();
//	}
}
