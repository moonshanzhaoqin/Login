package com.yuyutechnology.exchange.dao;

import java.util.HashMap;
import java.util.List;

import com.yuyutechnology.exchange.pojo.CrmUserInfo;
import com.yuyutechnology.exchange.util.page.PageBean;

public interface CrmUserInfoDAO {

	void updateUserInfo(CrmUserInfo crmUserInfo);

	CrmUserInfo getCrmUserInfoByUserId(int userId);

	HashMap<String, Object> getUserAccountInfoListByPage(String sql, List<Object> values, int currentPage,
			int pageSize);

	void userFreeze(Integer userId, int userAvailable);

	/**
	 * @param userPhone
	 * @param userName
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	PageBean getUserInfoByPage(String userPhone, String userName, int currentPage, int pageSize);

}
