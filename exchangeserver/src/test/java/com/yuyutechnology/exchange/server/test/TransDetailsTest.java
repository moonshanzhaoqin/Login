package com.yuyutechnology.exchange.server.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dto.TransDetailsDTO;
import com.yuyutechnology.exchange.manager.TransDetailsManager;
import com.yuyutechnology.exchange.manager.TransferManager;

public class TransDetailsTest extends BaseSpringJunit4 {

	
	@Autowired
	TransferDAO transferDAO;
	@Autowired
	TransferManager transferManager;
	@Autowired
	TransDetailsManager transDetailsManager;
	
	@Test
	public void test() throws InterruptedException {
		
		TransDetailsDTO dto = transferManager.getTransDetails("2017042503120T000064",8);
		System.out.println(dto.getTraderName());
		
		
	}
	
	
}
