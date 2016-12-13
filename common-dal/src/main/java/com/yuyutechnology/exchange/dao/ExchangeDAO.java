package com.yuyutechnology.exchange.dao;

import com.yuyutechnology.exchange.pojo.Exchange;

public interface ExchangeDAO {

	public String createExchangeId(int transferType);
	
	public void addExchange(Exchange exchange);

}
