package com.yuyutechnology.exchange.dao;

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

}
