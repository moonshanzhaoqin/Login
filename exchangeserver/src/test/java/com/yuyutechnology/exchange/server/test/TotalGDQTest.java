package com.yuyutechnology.exchange.server.test;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.manager.TransDetailsManager;
import com.yuyutechnology.exchange.manager.TransferManager;

public class TotalGDQTest extends BaseSpringJunit4{
	
	@Autowired
	TransferManager transferManager;
	@Autowired
	TransDetailsManager transDetailsManager;
	
	
	@Test
	public void test(){
		
//		HashMap<String, Object> result = transferManager.getTransRecordbyPage("all", "expenses", 2, 1,100);
//		
//		List<TransferDTO> dtos = (List<TransferDTO>) result.get("dtos");
//		
//		for (TransferDTO transferDTO : dtos) {
//			System.out.println(transferDTO.toString());
//			transferManager.getTransDetails(transferDTO.getTransferId(), 2);
//		}
		
		transDetailsManager.addTransDetails("xxxxxxxxxxxxxxxxx", 1, 2, "", "", "", ServerConsts.CURRENCY_OF_GOLDPAY, BigDecimal.TEN,
				BigDecimal.ZERO, null, "", ServerConsts.TRANSFER_TYPE_IN_WITHDRAW);
		
	}

}
