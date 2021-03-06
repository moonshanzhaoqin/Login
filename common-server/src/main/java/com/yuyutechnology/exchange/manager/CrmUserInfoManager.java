package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.HashMap;

import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.util.page.PageBean;

public interface CrmUserInfoManager {

	public void updateUserInfo(User user);

	public HashMap<String, BigDecimal> getSystemAccountTotalAssets();

	public HashMap<String, BigDecimal> getUserAccountTotalAssets();

	public HashMap<String, Object> getUserAccountInfoListByPage(String userPhone, String userName, int userAvailable,
			int loginAvailable, int payAvailable, BigDecimal upperLimit, BigDecimal lowerLimit, int currentPage,
			int pageSize);

	public void updateImmediately();

	public int getUpdateFlag();

	/**
	 * @param currentPage
	 * @param userPhone
	 * @param userName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	PageBean getUserInfoByPage(int currentPage, String userPhone, String userName, String startTime, String endTime);

}
