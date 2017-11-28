package com.yuyutechnology.exchange.crm;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;

import com.yuyutechnology.exchange.crm.tpps.pojo.GoldqPayClient;
import com.yuyutechnology.exchange.manager.CrmUserInfoManager;

public class AccountInfoManagerTest extends BaseSpringJunit4 {
	
	@Autowired
	CrmUserInfoManager crmUserInfoManager;
	
	@Resource
	HibernateTemplate hibernateTemplateTPPS;
	
	@Test
	public void testTPPS(){
		GoldqPayClient client = hibernateTemplateTPPS.get(GoldqPayClient.class, 1L);
		System.out.println(client.getName());
	}

}
