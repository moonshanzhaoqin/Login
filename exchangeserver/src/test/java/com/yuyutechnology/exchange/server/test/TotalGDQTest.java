package com.yuyutechnology.exchange.server.test;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.dto.TransferDTO;
import com.yuyutechnology.exchange.manager.TransferManager;

public class TotalGDQTest extends BaseSpringJunit4{
	
	@Autowired
	TransferManager transferManager;
	
	
	@Test
	public void test(){
		
		HashMap<String, Object> result = transferManager.getTransRecordbyPage("all", "expenses", 2, 1,100);
		
		List<TransferDTO> dtos = (List<TransferDTO>) result.get("dtos");
		
		for (TransferDTO transferDTO : dtos) {
			System.out.println(transferDTO.toString());
			transferManager.getTransDetails(transferDTO.getTransferId(), 2);
		}
		
	}

}
