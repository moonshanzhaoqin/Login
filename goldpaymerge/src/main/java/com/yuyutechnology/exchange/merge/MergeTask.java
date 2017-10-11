/**
 * 
 */
package com.yuyutechnology.exchange.merge;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author silent.sun
 *
 */
@Component
public class MergeTask {
	
	@Autowired
	ExanytimeMergeManager exanytimeMergeManager;
	
	@Autowired
	GoldpayMergeManager goldpayMergeManager;
	
	public static Logger logger = LogManager.getLogger(MergeTask.class);
	
	@PostConstruct
	public void mergeTask(){
		logger.info("start merge users======================================================================");
		
		
		logger.info("end merge users======================================================================");
	}
}
