package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.Config;

public interface ConfigDAO {
	/**
	 * 获取配置值
	 * 
	 * @param configKey
	 * @return configValue
	 */
	public String getConfigValue(String configKey);

	/**
	 * 添加或更改配置
	 * 
	 * @param configKey
	 * @param configValue
	 */
	public void saveOrUpdateConfig(String configKey, String configValue);
	
	public List<Config> getCongifValues();

}
