package com.yuyutechnology.exchange.task;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.manager.GoldpayTransManager;
import com.yuyutechnology.exchange.pojo.Withdraw;

@Component
public class AutoGoldpayRemitTask {

	@Autowired
	GoldpayTransManager goldpayTransManager;

	public static Logger logger = LogManager.getLogger(AutoGoldpayRemitTask.class);

	public void autoGoldpayRemitTask() {
		logger.info("=============autoGoldpayRemitTask Start=================={}", new Date());
		
		List<Withdraw> withdraws = goldpayTransManager.getNeedGoldpayRemitWithdraws();

		for (Withdraw withdraw : withdraws) {
			goldpayTransManager.goldpayRemit(withdraw.getWithdrawId());
		}
		
		
		logger.info("=============autoGoldpayRemitTask End=================={}", new Date());
	}

}
