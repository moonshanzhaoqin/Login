package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.HashMap;

import com.yuyutechnology.exchange.pojo.User;

public interface CrmUserInfoManager {
	
	public void updateUserInfo(User user);
	
	public HashMap<String, BigDecimal> getSystemAccountTotalAssets();

	public HashMap<String, BigDecimal> getUserAccountTotalAssets();

	public HashMap<String, Object> getUserAccountInfoListByPage(String userPhone, String userName, int userAvailable,
			int loginAvailable, int payAvailable, BigDecimal upperLimit, BigDecimal lowerLimit, int currentPage,
			int pageSize);
	
	public void userFreeze(Integer userId,int userAvailable);
	
	public void updateImmediately();
	
	public int getUpdateFlag();

}
