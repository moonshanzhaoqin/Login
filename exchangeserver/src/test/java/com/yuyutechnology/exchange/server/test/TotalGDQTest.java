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
		
//		GoldpayUserDTO goldpayUser = goldpayTrans4MergeManager.getGoldpayUserInfo(16);
//		if(goldpayUser!=null){
//			System.out.println("goldpay amount : "+goldpayUser.getBalance());
//		}
		//1528
//		List<WalletInfo> wallets = exchangeManager.getWalletsByUserId(16);
//		for (WalletInfo walletInfo : wallets) {
//			if(walletInfo.getCurrency().equals(ServerConsts.CURRENCY_OF_GOLDPAY)){
//				System.out.println("sssssssssssssssssss"+walletInfo.getBalance());
//			}
//		}

//		crmAlarmManager.alarmNotice("[25]","reachTotalGQDLimtWarning",1,new HashMap<String,Object>(){
//			private static final long serialVersionUID = 1L;
//
//			{
//				put("totalGDQCanBeSold", "3000");
//				put("percent", "30");
//			}
//		});
		
	}

}
