/**
 * 
 */
package com.yuyutechnology.exchange.manager;

import java.util.List;

import com.yuyutechnology.exchange.dto.CurrencyInfo;
import com.yuyutechnology.exchange.pojo.AppVersion;
import com.yuyutechnology.exchange.pojo.Currency;

/**
 * @author silent.sun
 *
 */
public interface CommonManager {
	
	public AppVersion getAppVersion(String platformType, String updateWay);
	
	public List<CurrencyInfo> getCurrency();
	
	List<Currency> getCurrentCurrency();
	
	public boolean verifyCurrency(String currency);
}
