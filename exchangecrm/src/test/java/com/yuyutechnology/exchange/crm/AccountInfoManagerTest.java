package com.yuyutechnology.exchange.crm;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.manager.CrmUserInfoManager;

public class AccountInfoManagerTest extends BaseSpringJunit4 {
	
	@Autowired
	CrmUserInfoManager crmUserInfoManager;
	
	@Test
	public void crmAccountTest(){
		
		crmUserInfoManager.getUserAccountInfoListByPage(null, null, 3, 
				new BigDecimal(10000), new BigDecimal(1000), 1, 10);

	}

}
