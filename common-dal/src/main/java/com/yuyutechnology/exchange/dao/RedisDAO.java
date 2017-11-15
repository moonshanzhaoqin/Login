package com.yuyutechnology.exchange.dao;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public interface RedisDAO {

	public void saveData(String key,Object value, long expire, TimeUnit timeUnit);
	
	public void saveData(String key,Object value, Date expireAt);
	
	public void saveData(String key, Object value);
	
	public void expireData(String key, long expire, TimeUnit timeUnit);
	
	public void expireAtData(String key, Date expireAt);
	
	public String getValueByKey(String key);
	
	public <T> T getObjectByKey(String key, Class<T> clazz);
	
	public void deleteData(String key);
	
	public void saveData4Hash(String key, String hashKey, String value);
	
	public String getData4Hash(String key, String hashKey);
	
	public void delData4Hash(String key, String ... hashKeys);
	
	public long incrementValue(String key, long number);
	
	public double incrementValue(String key, double number);
}
