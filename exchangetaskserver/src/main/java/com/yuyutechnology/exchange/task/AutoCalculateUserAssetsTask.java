package com.yuyutechnology.exchange.task;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	
	public static Logger logger = LogManager.getLogger(AutoCalculateUserAssetsTask.class);
	
	public void autoCalculateUserAssetsTask(){
		logger.info("=============autoCalculateUserAssetsTask Start==================");
		List<User> list = userManager.getUserList();
		if(list.isEmpty()){
			return ;
		}
		
		for (User user : list) {
			try {
				crmUserInfoManager.updateUserInfo(user);
			} catch (Exception e) {
			}
		}
		logger.info("=============End at {}==================",new Date());
	}

}
