package com.yuyutechnology.exchange.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.manager.GoldpayTransManager;

public class AutoTaskTest extends BaseSpringJunit4 {

	@Autowired
	AutoGoldpayRemitTask autoGoldpayRemitTask;
	@Autowired
	GoldpayTransManager goldpayTransManager;
	@Autowired 
	AutoWithdrawReviewTask autoWithdrawReviewTask;
	
//	@Test
	public void test () {
		autoWithdrawReviewTask.autoWithdrawReviewTask();
	}
	
//	@Test
//	public void test () {
//		List<Transfer> transfers= goldpayTransManager.getNeedGoldpayRemitWithdraws();
//		for (Transfer transfer : transfers) {
//			System.out.println(transfer);
//		}
//	}
	
}
