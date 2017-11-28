package com.yuyutechnology.exchange.server.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.dao.BindDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
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
	@Autowired
	BindDAO bindDAO;
	@Autowired 
	TransferDAO transferDAO;
	
	@Test
	public void test(){
		
//		Date time = DateFormatUtils.getpreDays(3);
//		
//		List<Transfer> list = transferDAO.getTransferListByTime(
//				ServerConsts.TRANSFER_STATUS_OF_PROCESSING, ServerConsts.TRANSFER_TYPE_IN_INVITE_CAMPAIGN,time);
//		
//		for (Transfer transfer : list) {
//			System.out.println("sssssssssssssssssssss"+transfer.getTransferId());
//		}
		
	}
	
}
