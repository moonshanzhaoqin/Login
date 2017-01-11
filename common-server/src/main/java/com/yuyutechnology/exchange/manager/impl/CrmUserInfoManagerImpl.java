package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CrmUserInfoDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.manager.CrmUserInfoManager;
import com.yuyutechnology.exchange.manager.ExchangeRateManager;
import com.yuyutechnology.exchange.pojo.CrmUserInfo;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;

@Service
public class CrmUserInfoManagerImpl implements CrmUserInfoManager {
	
	@Autowired
	UserDAO userDAO;
	@Autowired
	WalletDAO walletDAO;
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

	@Override
	public HashMap<String, BigDecimal> getSystemAccountTotalAssets() {
		
		HashMap<String, BigDecimal> map = new HashMap<>();
		User systemUser = userDAO.getSystemUser();
		
		if(systemUser == null){
			return map;
		}
		
		CrmUserInfo crmUserInfo = crmUserInfoDAO.getCrmUserInfoByUserId(systemUser.getUserId());
		
		if(crmUserInfo == null){
			return map;
		}
		
		BigDecimal totalAssets = crmUserInfo.getUserTotalAssets();
		map.put("totalAssets", totalAssets);
		
		List<Wallet> list = walletDAO.getWalletsByUserId(systemUser.getUserId());
		if(list.isEmpty()){
			return map;
		}
		for (Wallet wallet : list) {
			map.put(wallet.getCurrency().getCurrency(), wallet.getBalance());
		}
		
		logger.info("System Account Total Assets : {}",map.toString());
		
		return map;
	}
	
	@Override
	public HashMap<String, BigDecimal> getUserAccountTotalAssets(){
		
		HashMap<String, BigDecimal> map = null;
		
		User systemUser = userDAO.getSystemUser();
		
		if(systemUser == null){
			return map;
		}
		
		map = walletDAO.getUserAccountTotalAssets(systemUser.getUserId());
		
		BigDecimal totalAssets = new BigDecimal(0);
		
		if(map == null){
			return map;
		}
		
		for (Entry<String, BigDecimal> entry : map.entrySet()) {
			if(entry.getKey().equals(ServerConsts.STANDARD_CURRENCY)){
				totalAssets = totalAssets.add(entry.getValue());
			}else{
				totalAssets = totalAssets.add(exchangeRateManager.getExchangeResult(entry.getKey(), entry.getValue()));
			}
		}
		
		map.put("totalAssets", totalAssets.setScale(4, RoundingMode.CEILING));
		
		return map;
		
	}

	@Override
	public HashMap<String, Object> getUserAccountInfoListByPage(String userPhone,String userName,
			int isFrozen,BigDecimal upperLimit,BigDecimal lowerLimit,int currentPage, int pageSize) {
		
		List<Object> values = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder("from CrmUserInfo where userType = 0 ");
		
		if(!StringUtils.isEmpty(userPhone)){
			sb.append("and userPhone= ? ");
			values.add(userPhone);
		}
		if(isFrozen != 3){
			sb.append("and userAvailable = ? ");
			values.add(isFrozen);
		}
		if(upperLimit != null && upperLimit.compareTo(new BigDecimal(0)) == 1){
			sb.append("and userTotalAssets <= ? ");
			values.add(upperLimit);
		}
		if(lowerLimit != null && lowerLimit.compareTo(new BigDecimal(0)) == 1){
			sb.append("and userTotalAssets >= ? ");
			values.add(lowerLimit);
		}
		if(!StringUtils.isEmpty(userName)){
			sb.append("and userName like '%"+userName+"%'");

		}
		
		sb.append(" order by userTotalAssets desc");
		
		HashMap<String, Object> result = crmUserInfoDAO.getUserAccountInfoListByPage(
				sb.toString(), values, currentPage, pageSize);
		
		
		return result;
	}

	@Override
	public void userFreeze(Integer userId, int userAvailable) {
		crmUserInfoDAO.userFreeze(userId, userAvailable);
	}
	
	
	

}
