package com.yuyutechnology.exchange.task;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.manager.CrmUserInfoManager;
import com.yuyutechnology.exchange.manager.GoldpayManager;
import com.yuyutechnology.exchange.pojo.User;

@Component
public class AutoCalculateUserAssetsTask {
	
	@Autowired
	UserDAO userDAO;
	@Autowired
	CrmUserInfoManager crmUserInfoManager;
	@Autowired
	GoldpayManager goldpayManager;
	
	public static Logger logger = LogManager.getLogger(AutoCalculateUserAssetsTask.class);
	
	public void autoCalculateUserAssetsTask(){
		logger.info("=============autoCalculateUserAssetsTask Start==================");
		goldpayManager.startCopyGoldpayAccount();
		List<User> list = userDAO.listAllUser();
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
