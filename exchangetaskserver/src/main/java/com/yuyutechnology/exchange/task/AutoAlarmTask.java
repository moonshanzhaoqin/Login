package com.yuyutechnology.exchange.task;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	public static Logger logger = LoggerFactory.getLogger(AutoAlarmTask.class);
	
	public void autoAlarmTask(){
		
		logger.info("=============autoAlarmTask Start=================={}",new Date());
		
		HashMap<String, BigDecimal> systemTotalAssets = crmUserInfoManager.getSystemAccountTotalAssets();
		HashMap<String, BigDecimal> userTotalAssets = crmUserInfoManager.getUserAccountTotalAssets();
		
		if(systemTotalAssets != null && userTotalAssets != null){
			BigDecimal difference = systemTotalAssets.get("totalAssets").subtract(userTotalAssets.get("totalAssets"));	
			crmAlarmManager.autoAlarm(difference);
			logger.info("=============autoAlarmTask difference : {}",difference);
		}
		
		logger.info("=============autoAlarmTask END=================={}",new Date());	
	}

}
