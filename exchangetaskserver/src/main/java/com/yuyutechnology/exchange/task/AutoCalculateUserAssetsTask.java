package com.yuyutechnology.exchange.task;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.manager.CrmUserInfoManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.User;

@Component
public class AutoCalculateUserAssetsTask {
	
	@Autowired
	UserManager userManager;
	@Autowired
	CrmUserInfoManager crmUserInfoManager;
	
	public static Logger logger = LoggerFactory.getLogger(AutoCalculateUserAssetsTask.class);
	
	public void autoUpdateExchangeRateTask(){
		logger.info("=============autoUpdateExchangeRateTask Start==================");
		List<User> list = userManager.getUserList();
		if(list.isEmpty()){
			return ;
		}
		
		for (User user : list) {
			crmUserInfoManager.updateUserInfo(user);
		}
		logger.info("=============End at {}==================",new Date());
	}

}
