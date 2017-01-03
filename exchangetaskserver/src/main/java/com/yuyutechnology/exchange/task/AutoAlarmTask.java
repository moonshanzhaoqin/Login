package com.yuyutechnology.exchange.task;

import java.math.BigDecimal;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.manager.CrmUserInfoManager;

public class AutoAlarmTask {
	
	@Autowired
	CrmUserInfoManager crmUserInfoManager;
	
	public static Logger logger = LoggerFactory.getLogger(AutoAlarmTask.class);
	
	public void autoAlarmTask(){
		
		HashMap<String, BigDecimal> systemTotalAssets = crmUserInfoManager.getSystemAccountTotalAssets();
		HashMap<String, BigDecimal> userTotalAssets = crmUserInfoManager.getUserAccountTotalAssets();
		
		BigDecimal Difference = systemTotalAssets.get("totalAssets").subtract(userTotalAssets.get("totalAssets"));
		
	}

}
