/**
 * 
 */
package com.yuyutechnology.exchange.manager.impl;

import java.util.ArrayList;
import java.util.List;

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
	@Scheduled(cron = "0 1/5 * * * ?")
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
		if (redisDAO.getValueByKey("getCurrency") == null) {
			logger.info("getCurrency from db");
			currencies = currencyDAO.getCurrencys();
			redisDAO.saveData("getCurrency", currencies, 30);
		} else {
			logger.info("getCurrency from redis:");
			currencies = (List<Currency>) JsonBinder.getInstance().fromJsonToList(redisDAO.getValueByKey("getCurrency"),
					Currency.class);
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
		if (redisDAO.getValueByKey("getCurrentCurrency") == null) {
			logger.info("getCurrentCurrency from db");
			currencies = currencyDAO.getCurrentCurrency();
			logger.info("currency={}", currencies);
			redisDAO.saveData("getCurrentCurrency", currencies, 30);
		} else {
			logger.info("getCurrentCurrency from redis:");
			currencies = (List<Currency>) JsonBinder.getInstance()
					.fromJsonToList(redisDAO.getValueByKey("getCurrentCurrency"), Currency.class);
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
