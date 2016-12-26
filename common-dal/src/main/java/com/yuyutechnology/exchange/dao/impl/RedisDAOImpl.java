package com.yuyutechnology.exchange.dao.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.utils.JsonBinder;

@Repository
public class RedisDAOImpl implements RedisDAO {

	@Resource
	RedisTemplate<String, String> commonRedisTemplate;

	public void saveData(String key, Object value, long timeout) {
		String json = null;

		if (value instanceof String) {
			json = (String) value;
		} else {
			json = JsonBinder.getInstance().toJson(value);
		}
		commonRedisTemplate.opsForValue().set(key, json);
		commonRedisTemplate.expire(key, timeout, TimeUnit.MINUTES);
	}
	
	public void saveData(String key, Object value) {
		String json = null;

		if (value instanceof String) {
			json = (String) value;
		} else {
			json = JsonBinder.getInstance().toJson(value);
		}
		commonRedisTemplate.opsForValue().set(key, json);
	}

	public String getValueByKey(String key) {
		String jsonContent = commonRedisTemplate.opsForValue().get(key);
		if (StringUtils.isEmpty(jsonContent)) {
			return null;
		} else {
			return jsonContent;
		}
	}

	public void deleteKey(String key) {
		commonRedisTemplate.delete(key);
	}
	
	public String getData4Hash(String key, String hashKey) {
		return (String) commonRedisTemplate.opsForHash().get(key, hashKey);
	}
	
	public void saveData4Hash(String key, String hashKey, String value) {
		commonRedisTemplate.opsForHash().put(key, hashKey, value);
	}
	
	public void delData4Hash(String key, String ... hashKeys) {
		commonRedisTemplate.opsForHash().delete(key, hashKeys);
	}

}
