package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.Config;

public interface ConfigDAO {

	public List<Config> getConfigValues();

	void saveOrUpdateConfig(Config config);

	Config getConfig(String configKey);

}
