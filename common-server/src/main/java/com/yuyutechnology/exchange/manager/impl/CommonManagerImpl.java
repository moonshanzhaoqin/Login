/**
 * 
 */
package com.yuyutechnology.exchange.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.AppVersionDAO;
import com.yuyutechnology.exchange.dao.ConfigDAO;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dto.MsgFlagInfo;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.pojo.AppVersion;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.utils.ResourceUtils;

/**
 * @author silent.sun
 *
 */
@Service
public class CommonManagerImpl implements CommonManager {

	public static Logger logger = LoggerFactory.getLogger(CommonManagerImpl.class);

	@Autowired
	AppVersionDAO appVersionDAO;
	@Autowired
	CurrencyDAO currencyDAO;
	@Autowired
	ConfigDAO configDAO;
	@Autowired
	RedisDAO redisDAO;

	private Map<String, Currency> allCurrenciesMap = new HashMap<String, Currency>();
	private List<Currency> allCurrencies = new ArrayList<Currency>();
	private Map<String, Currency> currentCurrenciesMap = new HashMap<String, Currency>();
	private List<Currency> currentCurrencies = new ArrayList<Currency>();

	@Override
	@PostConstruct
	@Scheduled(cron = "0 1/10 * * * ?")
	public void refreshConfig() {
		ResourceUtils.clearCache();
		initCurrency();
	}

	private void initCurrency() {
		allCurrencies = currencyDAO.getCurrencys();
		for (Currency currency : allCurrencies) {
			allCurrenciesMap.put(currency.getCurrency(), currency);
			if (currency.getCurrencyStatus() == ServerConsts.CURRENCY_AVAILABLE) {
				currentCurrencies.add(currency);
				currentCurrenciesMap.put(currency.getCurrency(), currency);
			}
		}
	}

	@Override
	public AppVersion getAppVersion(String platformType, String updateWay) {
		return appVersionDAO.getAppVersionInfo(platformType, updateWay);
	}

	@Override
	public List<Currency> getAllCurrencies() {
		return allCurrencies;
	}

	@Override
	public List<Currency> getCurrentCurrencies() {
		return currentCurrencies;
	}

	@Override
	public Currency getCurrentCurreny(String currency) {
		return currentCurrenciesMap.get(currency);
	}

	@Override
	public Currency getCurreny(String currency) {
		return allCurrenciesMap.get(currency);
	}

	@Override
	public boolean verifyCurrency(String currency) {
		return currentCurrenciesMap.get(currency) != null;
	}

	@Override
	public void addMsgFlag(int userId, int type) {
		opsMsgFlag(userId, type, true);
	}

	@Override
	public void readMsgFlag(int userId, int type) {
		opsMsgFlag(userId, type, false);
	}

	public MsgFlagInfo getMsgFlag(int userId) {
		MsgFlagInfo info = new MsgFlagInfo();
		String msgFlagNewTransKey = "msgFlagNewTrans";
		String msgFlagNewRequestTransKey = "msgFlagNewRequestTrans";
		String msgFlagNewTrans = redisDAO.getData4Hash(msgFlagNewTransKey, userId + "");
		String msgFlagNewRequestTrans = redisDAO.getData4Hash(msgFlagNewRequestTransKey, userId + "");
		info.setNewTrans(Boolean.valueOf(msgFlagNewTrans));
		info.setNewRequestTrans(Boolean.valueOf(msgFlagNewRequestTrans));
		return info;
	}

	private void opsMsgFlag(int userId, int type, boolean on) {
		String msgFlagNewTransKey = "msgFlagNewTrans";
		String msgFlagNewRequestTransKey = "msgFlagNewRequestTrans";
		if (type == 1) {
			redisDAO.saveData4Hash(msgFlagNewTransKey, userId + "", Boolean.valueOf(on).toString());
		} else {
			redisDAO.saveData4Hash(msgFlagNewRequestTransKey, userId + "", Boolean.valueOf(on).toString());
		}
	}
}
