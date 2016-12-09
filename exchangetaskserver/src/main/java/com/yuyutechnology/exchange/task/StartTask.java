package com.yuyutechnology.exchange.task;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.yuyutechnology.exchange.manager.ExchangeRateManager;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.startup.Bootstrap;
/**
 * Hello world!
 * 
 */
public class StartTask{
	
	@Autowired
	ExchangeRateManager exchangeRateManager;
	@Autowired
	TransferManager transferManager;
	
	public static Logger logger = LoggerFactory.getLogger(StartTask.class);

	public void autoUpdateExchangeRateTask(){
		logger.info("=============autoUpdateExchangeRateTask Start==================");
		exchangeRateManager.updateExchangeRateNoGoldq();
		exchangeRateManager.updateGoldpayExchangeRate();
		logger.info("=============End at {}==================",new Date());
	}
	
	@Async
	public void autoSystemRefundBatch(){
		logger.info("=============autoSystemRefundBatch Start==================");
		transferManager.systemRefundBatch();
		logger.info("=============End at {}==================",new Date());
	}
	
	public static void main(String[] args){
		String home = System.getProperty("task.home");
		if (StringUtils.isEmpty(home))
		{
			home = System.getenv().get("task.home");
		}
		if (home == null)
		{
			System.out
					.println("[task.home] is not set,please set like this :java -Dtask.home=D:/kevin/taskhome"
							+ new Date());
		}
		else
		{
			String logCfgPath = home + "/conf/log4j.properties";
			String contextCfgPath = home + "/conf/task-context.xml";
			System.out.println("home=" + home);
			System.out.println("contextCfgPath=" + contextCfgPath);
			Bootstrap bootstrap = new Bootstrap(contextCfgPath, logCfgPath);
			bootstrap.startup();
		}
	}
}
