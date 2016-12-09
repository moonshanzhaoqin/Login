package com.yuyutechnology.exchange.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.manager.ExchangeRateManager;
import com.yuyutechnology.exchange.manager.TransferManager;

@Component
public class AutoUpdateExchangeRateTask {

	@Autowired
	ExchangeRateManager exchangeRateManager;
	@Autowired
	TransferManager transferManager;
	
	public static Logger logger = LoggerFactory.getLogger(AutoUpdateExchangeRateTask.class);
	
	public void autoUpdateExchangeRateTask(){
		logger.info("=============autoUpdateExchangeRateTask Start==================");
		exchangeRateManager.updateExchangeRateNoGoldq();
		exchangeRateManager.updateGoldpayExchangeRate();
		logger.info("=============End at {}==================",new Date());
	}
	
	@Async
	public void autoSystemRefundBatch(){
		logger.info("=============autoSystemRefundBatch Start==================");
		transferManager.systemRefundBatch();
		logger.info("=============End at {}==================",new Date());
	}
	
}
