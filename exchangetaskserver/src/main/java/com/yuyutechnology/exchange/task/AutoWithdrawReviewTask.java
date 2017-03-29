package com.yuyutechnology.exchange.task;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.manager.GoldpayTransManager;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.Withdraw;

@Component
public class AutoWithdrawReviewTask {
	
	@Autowired
	GoldpayTransManager goldpayTransManager;
	
	
	public static Logger logger = LogManager.getLogger(AutoWithdrawReviewTask.class);
	
	public void autoWithdrawReviewTask(){
		logger.info("=============AutoWithdrawReviewTask Start=================={}",new Date());
		List<Transfer> transfers = goldpayTransManager.getNeedReviewWithdraws();
		for (Transfer transfer : transfers) {
			goldpayTransManager.withdrawReviewAuto(transfer.getTransferId());
		}
		
		logger.info("=============AutoWithdrawReviewTask End=================={}",new Date());	
	}

}
