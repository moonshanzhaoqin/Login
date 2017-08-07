/**
 * 
 */
package com.yuyutechnology.exchange.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.manager.AccountingManager;
import com.yuyutechnology.exchange.manager.CrmAlarmManager;

/**
 * @author silent.sun
 *
 */
@Component
public class AutoRegistrationTask {

@Autowired
CrmAlarmManager crmAlarmManager;
	public static Logger logger = LogManager.getLogger(AutoRegistrationTask.class);
	
	public void registrationAlarm(){
		logger.info("=============registrationAlarm Start==================");
		crmAlarmManager.registrationAlarm();
		logger.info("=============registrationAlarm End==================");
	}
}
