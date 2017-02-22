package com.yuyutechnology.exchange.task;

import java.util.Date;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.manager.ExchangeRateManager;
import com.yuyutechnology.exchange.manager.TransferManager;

@Component
public class AutoUpdateExchangeRateTask {

	@Autowired
	ExchangeRateManager exchangeRateManager;
	@Autowired
	TransferManager transferManager;
	
	public static Logger logger = LogManager.getLogger(AutoUpdateExchangeRateTask.class);
	
	public void autoUpdateExchangeRateTask(){
		logger.info("=============autoUpdateExchangeRateTask Start==================");
		exchangeRateManager.updateExchangeRate(false);
		logger.info("=============End at {}==================",new Date());
	}
	
	public void autoSystemRefundBatch(){
		logger.info("=============autoSystemRefundBatch Start==================");
		transferManager.systemRefundBatch();
		logger.info("=============End at {}==================",new Date());
	}
	
}
