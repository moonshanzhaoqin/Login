package com.yuyutechnology.exchange.dao;

import java.util.HashMap;
import java.util.List;

import com.yuyutechnology.exchange.pojo.Exchange;

public interface ExchangeDAO {

	String createExchangeId(int transferType);

	void addExchange(Exchange exchange);
	
	void updateExchage(Exchange exchange);

	Exchange getExchangeById(String exchangeId);

	HashMap<String, Object> getExchangeRecordsByPage(String sql, List<Object> values, int currentPage, int pageSize);

	Integer getTotalNumOfDailyExchange();

	Object getExchangeByIdJoinUser(String exchangeId);

}
