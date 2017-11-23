package com.yuyutechnology.exchange.server.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;

public class TotalGDQTest extends BaseSpringJunit4{
	

	@Autowired
	CrmAlarmManager crmAlarmManager;
	@Autowired
	ExchangeManager exchangeManager;
	@Autowired
	GoldpayTrans4MergeManager goldpayTrans4MergeManager;
	
	@Test
	public void test(){
	}

}
