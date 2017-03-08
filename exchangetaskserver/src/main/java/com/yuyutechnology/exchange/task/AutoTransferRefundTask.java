package com.yuyutechnology.exchange.task;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.manager.GoldpayTransManager;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.utils.DateFormatUtils;

@Component
public class AutoTransferRefundTask {
	
	@Autowired
	GoldpayTransManager goldpayTransManager;
	
	public static Logger logger = LogManager.getLogger(AutoTransferRefundTask.class);
	
	public void autoWithdrawRefund(){
		logger.info("=============autoWithdrawRefund Start=================={}",new Date());
		List<Transfer> trans = goldpayTransManager.findGoldpayWithdrawByTimeBefore(DateFormatUtils.getIntervalHour(new Date(), -2));
		if (trans != null && !trans.isEmpty()) {
			for (Transfer transfer : trans) {
				goldpayTransManager.withdrawRefund(transfer.getUserFrom(), transfer.getTransferId(), transfer.getCurrency(), transfer.getTransferAmount());
			}
		}
		logger.info("=============autoWithdrawRefund end==================difference : {}",new Date());
		
	}
}
