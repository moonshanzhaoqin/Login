/**
 * 
 */
package com.yuyutechnology.exchange.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.dao.ConfigDAO;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.pojo.Config;

/**
 * @author silent.sun
 *
 */
@Component
public class ConfigManagerImpl implements ConfigManager {
	private static Logger logger = LoggerFactory.getLogger(ConfigManagerImpl.class);

	@Autowired
	ConfigDAO configDAO;

	Map<String, String> configMap = new HashMap<String, String>();

	@Override
	@PostConstruct
	@Scheduled(cron = "0 1/10 * * * ?")
	public void refreshConfig() {
		initConfig();
	}

	private void initConfig() {
		List<Config> configs = configDAO.getConfigValues();
		for (Config config : configs) {
			configMap.put(config.getConfigKey(), config.getConfigValue());
		}
	}

	public String getConfigStringValue(ConfigKeyEnum key, String defaultValue) {
		String value = configMap.get(key.getKey());
		if (StringUtils.isNotBlank(value)) {
			return value;
		}
		return defaultValue;
	}

	public Long getConfigLongValue(ConfigKeyEnum key, Long defaultValue) {
		String value = configMap.get(key.getKey());
		if (StringUtils.isNotBlank(value)) {
			try {
				return Long.valueOf(value);
			} catch (Exception e) {
			}
		}
		return defaultValue;
	}

	public Boolean getConfigBooleanValue(ConfigKeyEnum key) {
		String value = configMap.get(key.getKey());
		if (StringUtils.isNotBlank(value)) {
			try {
				return Boolean.valueOf(value);
			} catch (Exception e) {
			}
		}
		return false;
	}

	public Double getConfigDoubleValue(ConfigKeyEnum key, Double defaultValue) {
		String value = configMap.get(key.getKey());
		if (StringUtils.isNotBlank(value)) {
			try {
				return Double.valueOf(value);
			} catch (Exception e) {
			}
		}
		return defaultValue;
	}

	@Override
	public String updateConfig(String configKey, String configValue) {
		logger.info("{},{}", configKey, configValue);
		Config config = configDAO.getConfig(configKey);
		if (config == null) {
			return RetCodeConsts.RET_CODE_FAILUE;
		} else {
			config.setConfigValue(configValue);
			configDAO.saveOrUpdateConfig(config);
			return RetCodeConsts.RET_CODE_SUCCESS;
		}
	}

	@Override
	public List<Config> getConfigList() {
		List<Config> configs = configDAO.getConfigValues();
		return configs;
	}
}
