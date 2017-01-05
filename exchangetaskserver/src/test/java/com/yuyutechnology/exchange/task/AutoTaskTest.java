package com.yuyutechnology.exchange.task;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AutoTaskTest extends BaseSpringJunit4 {

	@Autowired
	AutoTransferRefundTask autoTransferRefundTask;
	
//	@Test
	public void test () {
		autoTransferRefundTask.autoWithdrawRefund();
	}
}
