package com.yuyutechnology.exchange.dao;

public interface RedisDAO {

	public void saveData(String key,Object value, long timeout);
	
	public String getValueByKey(String key);
	
	public void deleteKey(String key);
	
}
