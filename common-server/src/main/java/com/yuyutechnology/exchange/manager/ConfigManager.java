package com.yuyutechnology.exchange.manager;

import com.yuyutechnology.exchange.ConfigKeyEnum;

public interface ConfigManager {
	
	public void refreshConfig();
	
	public String getConfigStringValue (ConfigKeyEnum key, String defaultValue);
	
	public Long getConfigLongValue (ConfigKeyEnum key, Long defaultValue);
	
	public Boolean getConfigBooleanValue (ConfigKeyEnum key);
	
	public Double getConfigDoubleValue (ConfigKeyEnum key, Double defaultValue);

}
