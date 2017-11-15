package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.TransDetails;

public interface TransDetailsDAO {

	void addTransDetails(TransDetails transDetails);
	
	void updateTransDetails(TransDetails transDetails);

	List<?> getTransDetailsByTransIdAndUserId(Integer userId, String transId);

	TransDetails getTransDetails(Integer userId, String transId);

	Integer updateTransDetails(String receiverName, String areaCode, String phone);
	

}
