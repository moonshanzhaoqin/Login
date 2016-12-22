package com.yuyutechnology.exchange.dao;

public interface RedisDAO {

	public void saveData(String key,Object value, long timeout);
	
	public void saveData(String key, Object value);
	
	public String getValueByKey(String key);
	
	public void deleteKey(String key);
	
}
