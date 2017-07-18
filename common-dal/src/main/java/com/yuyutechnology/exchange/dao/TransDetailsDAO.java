package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.TransDetails;

public interface TransDetailsDAO {

	void addTransDetails(TransDetails transDetails);
	
	void updateTransDetails(TransDetails transDetails);

	List<?> getTransDetailsByTransIdAndUserId(Integer userId, String transId);
	

}
