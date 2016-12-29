package com.yuyutechnology.exchange.dao;

import java.util.HashMap;
import java.util.List;

import com.yuyutechnology.exchange.pojo.CrmUserInfo;

public interface CrmUserInfoDAO {

	public void updateUserInfo(CrmUserInfo crmUserInfo);
	
	public CrmUserInfo getCrmUserInfoByUserId(int userId);
	
	public HashMap<String, Object> getUserAccountInfoListByPage(
			String sql,List<Object> values,int currentPage, int pageSize);

}
