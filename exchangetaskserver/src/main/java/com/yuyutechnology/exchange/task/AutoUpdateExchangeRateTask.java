package com.yuyutechnology.exchange.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.manager.TransferManager;

@Component
public class AutoUpdateExchangeRateTask {

	@Autowired
	OandaRatesManager oandaRatesManager;
	@Autowired
	TransferManager transferManager;
	
	public static Logger logger = LogManager.getLogger(AutoUpdateExchangeRateTask.class);
	
	public void autoUpdateExchangeRateTask(){
		logger.info("=============autoUpdateExchangeRateTask Start=================={}",new SimpleDateFormat("HH:mm:ss").format(new Date()));
		oandaRatesManager.updateExchangeRates();
		logger.info("=============End at {}==================",new SimpleDateFormat("HH:mm:ss").format(new Date()));
	}
	
	public void autoSystemRefundBatch(){
		logger.info("=============autoSystemRefundBatch Start=============={}",new SimpleDateFormat("HH:mm:ss").format(new Date()) );
		try {
			transferManager.systemRefundBatch();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("error : {}",e.toString());
		}
		logger.info("=============End at {}==================",new SimpleDateFormat("HH:mm:ss").format(new Date()));
	}
	
}
