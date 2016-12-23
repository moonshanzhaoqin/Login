/**
 * 
 */
package com.yuyutechnology.exchange.manager;

import java.util.List;

import com.yuyutechnology.exchange.dto.CurrencyInfo;
import com.yuyutechnology.exchange.dto.MsgFlagInfo;
import com.yuyutechnology.exchange.pojo.AppVersion;
import com.yuyutechnology.exchange.pojo.Currency;

/**
 * @author silent.sun
 *
 */
public interface CommonManager {
	
	public void refreshConfig();
	
	public AppVersion getAppVersion(String platformType, String updateWay);
	
	public List<CurrencyInfo> getCurrency();
	
	public List<Currency> getCurrentCurrency();
	
	public boolean verifyCurrency(String currency);
	
	/**
	 * @param userId
	 * @param type 0=转账提示, 1=请求消息提示
	 */
	public void addMsgFlag(int userId, int type);
	
	/**
	 * @param userId
	 * @param type 0=转账提示, 1=请求消息提示
	 */
	public void readMsgFlag(int userId, int type);
	
	/**
	 * @param userId
	 * @return
	 */
	public MsgFlagInfo getMsgFlag(int userId);
}
