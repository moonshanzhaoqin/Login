package com.yuyutechnology.exchange.manager;

import java.util.List;

import com.yuyutechnology.exchange.ConfigKeyEnum;
import com.yuyutechnology.exchange.pojo.Config;

public interface ConfigManager {

	public void refreshConfig();

	public String getConfigStringValue(ConfigKeyEnum key, String defaultValue);

	public Long getConfigLongValue(ConfigKeyEnum key, Long defaultValue);

	public Boolean getConfigBooleanValue(ConfigKeyEnum key);

	public Double getConfigDoubleValue(ConfigKeyEnum key, Double defaultValue);

	public String updateConfig(String configKey, String configValue);

	public List<Config> getConfigList();

}
