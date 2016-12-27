/**
 * 
 */
package com.yuyutechnology.exchange;

/**
 * @author silent.sun
 *
 */
public enum ConfigKeyEnum {
	
	DAILYTRANSFERTHRESHOLD("daily_transfer_threshold"),EACHTRANSFERTHRESHOLD("each_transfer_threshold"),TOTALBALANCETHRESHOLD("total_balance_threshold");
	
	private String key;
	private ConfigKeyEnum(String key)
	{
		this.key = key;
	}
	public String getKey()
	{
		return key;
	}
}
