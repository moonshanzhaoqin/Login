package com.yuyutechnology.exchange.task;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.CrmUserInfoManager;

@Component
public class AutoAlarmTask {
	
	@Autowired
	CrmUserInfoManager crmUserInfoManager;
	@Autowired
	CrmAlarmManager crmAlarmManager;
	
	public static Logger logger = LogManager.getLogger(AutoAlarmTask.class);
	
	public void autoAlarmTask(){
		
		logger.info("=============autoAlarmTask Start=================={}",new Date());
		HashMap<String, BigDecimal> userTotalAssets = crmUserInfoManager.getUserAccountTotalAssets();
		
		if(userTotalAssets != null){
			logger.info("totalAssets : {}",userTotalAssets.get("totalAssets"));
			// TODO  crmAlarmManager.autoAlarm
			//crmAlarmManager.autoAlarm(userTotalAssets.get("totalAssets"));
		}

		logger.info("=============autoAlarmTask END=================={}",new Date());	
	}

}
