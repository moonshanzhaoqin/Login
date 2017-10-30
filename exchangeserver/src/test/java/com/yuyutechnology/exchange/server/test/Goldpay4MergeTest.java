package com.yuyutechnology.exchange.server.test;

import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;

public class Goldpay4MergeTest  extends BaseSpringJunit4 {

	@Autowired
	GoldpayTrans4MergeManager goldpayTrans4MergeManager;
	
	@Test
	public void test(){
//		
//		HashMap<String , String> result = goldpayTrans4MergeManager.
//				updateWalletByUserIdAndCurrency(
//				1, ServerConsts.CURRENCY_OF_GOLDPAY, 
//				2, ServerConsts.CURRENCY_OF_GOLDPAY, 
//				BigDecimal.TEN, ServerConsts.TRANSFER_TYPE_EXCHANGE, 
//				"2017042109410T002143", true, null);
//		System.out.println("goldpayOrderId : "+ result.get("goldpayOrderId"));

	}
	
}
