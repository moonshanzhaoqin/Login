package com.yuyutechnology.exchange.dao;

import java.util.HashMap;
import java.util.List;

import com.yuyutechnology.exchange.pojo.Exchange;

public interface ExchangeDAO {

	public String createExchangeId(int transferType);
	
	public void addExchange(Exchange exchange);
	
	public HashMap<String, Object> getExchangeRecordsByPage(
			String sql,List<Object> values,int currentPage, int pageSize);
	
	public Integer getTotalNumOfDailyExchange();

}
