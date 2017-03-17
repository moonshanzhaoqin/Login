package com.yuyutechnology.exchange.task;

import java.util.Date;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.manager.GoldpayTransManager;

@Component
public class AutoWithdrawReviewTask {
	
	@Autowired
	GoldpayTransManager goldpayTransManager;
	
	
	public static Logger logger = LogManager.getLogger(AutoWithdrawReviewTask.class);
	
	public void autoWithdrawReviewTask(){
		logger.info("=============AutoWithdrawReviewTask Start=================={}",new Date());
		goldpayTransManager.withdrawReviewAll();
		logger.info("=============AutoWithdrawReviewTask End=================={}",new Date());	
	}

}
