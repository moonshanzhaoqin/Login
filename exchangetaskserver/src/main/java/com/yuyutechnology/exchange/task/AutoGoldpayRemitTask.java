package com.yuyutechnology.exchange.task;

import java.util.Date;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.manager.GoldpayTransManager;

@Component
public class AutoGoldpayRemitTask {

	@Autowired
	GoldpayTransManager goldpayTransManager;

	public static Logger logger = LogManager.getLogger(AutoGoldpayRemitTask.class);

	public void autoGoldpayRemitTask() {
		logger.info("=============autoGoldpayRemitTask Start=================={}", new Date());
		goldpayTransManager.goldpayRemitAll();
		logger.info("=============autoGoldpayRemitTask End=================={}", new Date());
	}

}
