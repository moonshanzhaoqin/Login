package com.yuyutechnology.exchange.manager.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.manager.CurrencyManager;
import com.yuyutechnology.exchange.pojo.Currency;

@Service
public class CurrencyManagerImpl implements CurrencyManager {

	private static Logger logger = LoggerFactory.getLogger(CurrencyManagerImpl.class);

	@Autowired
	CurrencyDAO currencyDAO;

	@Override
	public int enableCurrency(String currencyId) {
		Currency currency = currencyDAO.getCurrency(currencyId);
		if (currency == null) {
			logger.info("currency not exist");
			return RetCodeConsts.CURRENCY_NOT_EXIST;
		} else if (currency.getCurrencyStatus() == ServerConsts.CURRENCY_AVAILABLE) {
			logger.info("currency has been AVAILABLE");
			return RetCodeConsts.CURRENCY_HAS_BEEN_AVAILABLE;
		} else {
			currency.setCurrencyStatus(ServerConsts.CURRENCY_AVAILABLE);
			currencyDAO.updateCurrency(currency);
			return RetCodeConsts.SUCCESS;
		}

	}

	@Override
	public int disableCurrency(String currencyId) {
		Currency currency = currencyDAO.getCurrency(currencyId);
		if (currency == null) {
			logger.info("currency not exist");
			return RetCodeConsts.CURRENCY_NOT_EXIST;
		} else if (currency.getCurrencyStatus() == ServerConsts.CURRENCY_UNAVAILABLE) {
			logger.info("currency has been UNAVAILABLE");
			return RetCodeConsts.CURRENCY_HAS_BEEN_UNAVAILABLE;
		} else {
			currency.setCurrencyStatus(ServerConsts.CURRENCY_UNAVAILABLE);
			currencyDAO.updateCurrency(currency);
			return RetCodeConsts.SUCCESS;
		}

	}

	@Override
	public List<Currency> getCurrencyList() {
		logger.info("get currencies");
		return currencyDAO.getCurrencys();
	}

	@Override
	public void updateCurrency(Currency currency) {
		// TODO Auto-generated method stub
		currencyDAO.updateCurrency(currency);
	}

}
