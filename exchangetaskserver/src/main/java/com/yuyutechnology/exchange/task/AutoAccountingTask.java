///**
// * 
// */
//package com.yuyutechnology.exchange.task;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.yuyutechnology.exchange.manager.AccountingManager;
//
///**
// * @author silent.sun
// *
// */
//@Component
//public class AutoAccountingTask {
//
//	@Autowired
//	AccountingManager accountManager;
//	
//	public static Logger logger = LogManager.getLogger(AutoAccountingTask.class);
//	
//	public void autoAccounting(){
//		logger.info("=============autoAccounting Start==================");
//		int badAccountSize = accountManager.accountingAll();
//		if (badAccountSize != 0) {
//			accountManager.freezeUsers();
//		}
//		logger.info("=============autoAccounting End==================");
//	}
//}
