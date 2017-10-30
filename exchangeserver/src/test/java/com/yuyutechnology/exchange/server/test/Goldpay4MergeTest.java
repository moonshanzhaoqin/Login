package com.yuyutechnology.exchange.server.test;

import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;
import com.yuyutechnology.exchange.manager.TransferManager;

public class Goldpay4MergeTest  extends BaseSpringJunit4 {

	@Autowired
	ExchangeManager exchangeManager;
	@Autowired
	TransferManager transferManager;
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
		
		
//		HashMap<String, String> request = transferManager.transferInitiate(16, "+090", "12345678963", ServerConsts.CURRENCY_OF_GOLDPAY, BigDecimal.TEN, null, 0);
//		String result = transferManager.transferConfirm(16,request.get("transferId") );
//		System.out.println("result is : "+result);
		
//		transferManager.systemRefundBatch();
		
		HashMap<String, String> result = exchangeManager.exchangeConfirm(16,ServerConsts.CURRENCY_OF_GOLDPAY, ServerConsts.CURRENCY_OF_USD,BigDecimal.ONE);
		HashMap<String, String> result1 = exchangeManager.exchangeConfirm(16,ServerConsts.CURRENCY_OF_USD, ServerConsts.CURRENCY_OF_CNY,BigDecimal.TEN);
		


	}
	
}
