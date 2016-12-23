/**
 * 
 */
package com.yuyutechnology.exchange.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.dao.AppVersionDAO;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dto.CurrencyInfo;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.pojo.AppVersion;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.utils.JsonBinder;
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
	RedisDAO redisDAO;
	
	@Override
	@Scheduled(cron = "0 1/10 * * * ?")
	public void refreshConfig() {
		ResourceUtils.clearCache();
	}
	
	@Override
	public AppVersion getAppVersion(String platformType, String updateWay) {
		return appVersionDAO.getAppVersionInfo(platformType, updateWay);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CurrencyInfo> getCurrency() {
		List<Currency> currencies;
		String currencyString = redisDAO.getValueByKey("getCurrency");
		if (StringUtils.isBlank(currencyString)) {
			logger.info("getCurrency from db");
			currencies = currencyDAO.getCurrencys();
			redisDAO.saveData("getCurrency", JsonBinder.getInstance().toJson(currencies), 30);
		} else {
			logger.info("getCurrency from redis: " +currencyString);
			currencies = (List<Currency>) JsonBinder.getInstance().fromJsonToList(currencyString, Currency.class);
		}
		List<CurrencyInfo> list = new ArrayList<>();
		for (Currency currency : currencies) {
			list.add(new CurrencyInfo(currency.getCurrency(), currency.getNameEn(), currency.getNameCn(),
					currency.getNameHk(), currency.getCurrencyStatus()));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Currency> getCurrentCurrency() {
		List<Currency> currencies;
		String currencyString = redisDAO.getValueByKey("getCurrentCurrency");
		if (StringUtils.isBlank(currencyString)) {
			logger.info("getCurrentCurrency from db");
			currencies = currencyDAO.getCurrentCurrency();
			logger.info("currency={}", currencies);
			redisDAO.saveData("getCurrentCurrency", JsonBinder.getInstance().toJson(currencies), 30);
		} else {
			logger.info("getCurrentCurrency from redis: "+currencyString);
			currencies = (List<Currency>) JsonBinder.getInstance()
					.fromJsonToList(currencyString, Currency.class);
		}
		return currencies;
	}
	
	@Override
	public boolean verifyCurrency(String currency) {
		List<Currency> currencyList = getCurrentCurrency();
		for (Currency currency2 : currencyList) {
			if(currency2.getCurrency().equals(currency)){
				return true;
			}
		}
		return false;
	}
}
