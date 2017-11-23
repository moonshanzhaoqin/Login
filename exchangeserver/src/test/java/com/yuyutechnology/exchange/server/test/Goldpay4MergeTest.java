package com.yuyutechnology.exchange.server.test;

import java.util.HashMap;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.yuyutechnology.exchange.dao.BindDAO;
import com.yuyutechnology.exchange.goldpay.msg.GoldpayUserDTO;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.pojo.Bind;
import com.yuyutechnology.exchange.server.controller.request.TransConfirmRequest;

public class Goldpay4MergeTest  extends BaseSpringJunit4 {

	@Autowired
	ExchangeManager exchangeManager;
	@Autowired
	TransferManager transferManager;
	@Autowired
	GoldpayTrans4MergeManager goldpayTrans4MergeManager;
	@Autowired
	BindDAO bindDAO;
	
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
		
		
//		HashMap<String, String> request = transferManager.transferInitiate(16, "+999", "12345678963", ServerConsts.CURRENCY_OF_GOLDPAY, BigDecimal.TEN, null, 0);
//		String result = transferManager.transferConfirm(16,request.get("transferId") );
//		System.out.println("result is : "+result);
		
//		transferManager.systemRefundBatch();
		
//		HashMap<String, String> result = exchangeManager.exchangeConfirm(16,ServerConsts.CURRENCY_OF_GOLDPAY, ServerConsts.CURRENCY_OF_USD,BigDecimal.ONE);
//		HashMap<String, String> result1 = exchangeManager.exchangeConfirm(16,ServerConsts.CURRENCY_OF_USD, ServerConsts.CURRENCY_OF_CNY,BigDecimal.TEN);
//		
//		TransInitRequest reqMsg = new TransInitRequest();
//		reqMsg.setPayerId(2);
//		reqMsg.setPayeeId(12);
//		reqMsg.setCurrency(ServerConsts.CURRENCY_OF_GOLDPAY);
//		reqMsg.setAmount(872d);
//		reqMsg.setTransferComment("test");
//		
//		reqMsg.setFeeDeduction(true);
//		reqMsg.setFee(new BigDecimal("72"));
//		reqMsg.setFeepayerId(12);
//		reqMsg.setRestricted(true);
//		
//
//		HashMap<String, String> map = transferManager.transInit4ThirdParty(reqMsg.getRestricted(),reqMsg.getPayerId(), 
//				reqMsg.getPayeeId(), reqMsg.getCurrency(),new BigDecimal(reqMsg.getAmount()+""), 
//				reqMsg.getTransferComment(), reqMsg.getFeeDeduction(), reqMsg.getFee(), reqMsg.getFeepayerId());
//		
//		System.out.println(map.toString());
		
//		TransConfirmRequest rqMsg = new TransConfirmRequest();
//		rqMsg.setRestricted(true);
//		rqMsg.setUserId(2);
//		rqMsg.setTransferId("2017112210500T000042");
//		rqMsg.setUserPayPwd("");
//		
//		HashMap<String, String> map = transferManager.transConfirm4ThirdParty(rqMsg.getRestricted(),rqMsg.getUserId(), 
//				rqMsg.getTransferId());
//		
//		System.out.println(map.toString());
		
		goldpayTrans4MergeManager.createGoldpay("+86", "9999999", "system", false);
		
	}
	
}
