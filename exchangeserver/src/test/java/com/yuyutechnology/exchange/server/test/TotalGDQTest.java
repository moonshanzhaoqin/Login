package com.yuyutechnology.exchange.server.test;

import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.WalletManager;

public class TotalGDQTest extends BaseSpringJunit4{
	

	@Autowired
	CrmAlarmManager crmAlarmManager;
	@Autowired
	WalletManager walletManager;

	
	@Test
	public void test(){

//		crmAlarmManager.alarmNotice("[25]","reachTotalGQDLimtWarning",1,new HashMap<String,Object>(){
//			private static final long serialVersionUID = 1L;
//
//			{
//				put("totalGDQCanBeSold", "3000");
//				put("percent", "30");
//			}
//		});
		
		HashMap<String, BigDecimal> result = walletManager.getTotalAmoutGold(16);
		System.out.println(result.toString());
		
	}

}
