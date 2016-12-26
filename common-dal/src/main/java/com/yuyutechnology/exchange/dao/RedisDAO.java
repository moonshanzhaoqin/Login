package com.yuyutechnology.exchange.dao;

public interface RedisDAO {

	public void saveData(String key,Object value, long timeout);
	
	public void saveData(String key, Object value);
	
	public String getValueByKey(String key);
	
	public void deleteKey(String key);
	
	public void saveData4Hash(String key, String hashKey, String value);
	
	public String getData4Hash(String key, String hashKey);
	
	public void delData4Hash(String key, String ... hashKeys);
	
}
