package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.dao.CrmUserInfoDAO;
import com.yuyutechnology.exchange.manager.CrmUserInfoManager;
import com.yuyutechnology.exchange.manager.ExchangeRateManager;
import com.yuyutechnology.exchange.pojo.CrmUserInfo;
import com.yuyutechnology.exchange.pojo.User;

@Service
public class CrmUserInfoManagerImpl implements CrmUserInfoManager {
	
	@Autowired
	CrmUserInfoDAO crmUserInfoDAO;
	@Autowired
	ExchangeRateManager exchangeRateManager;
	

	public static Logger logger = LoggerFactory.getLogger(CrmUserInfoManagerImpl.class);
	
	@Override
	public void updateUserInfo(User user) {
		CrmUserInfo crmUserInfo = new CrmUserInfo(user);
		BigDecimal totalBalance = exchangeRateManager.getTotalBalance(user.getUserId());
		logger.info("current time : {} , user Id : {} ,totalBalance : {}",new Object[]{new Date(),user.getUserId(),totalBalance});
		crmUserInfo.setUserTotalAssets(totalBalance);
		crmUserInfo.setUpdateAt(new Date());
		crmUserInfoDAO.updateUserInfo(crmUserInfo);
	}
	
	
	

}
