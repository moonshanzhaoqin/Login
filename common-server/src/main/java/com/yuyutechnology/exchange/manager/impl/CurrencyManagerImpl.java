package com.yuyutechnology.exchange.manager.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.CurrencyManager;
import com.yuyutechnology.exchange.pojo.Currency;

@Service
public class CurrencyManagerImpl implements CurrencyManager {

	private static Logger logger = LoggerFactory.getLogger(CurrencyManagerImpl.class);

	@Autowired
	CurrencyDAO currencyDAO;
	@Autowired
	CommonManager commonManager;

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

	@Override
	public String addCurrency(String currencyId) {
		Currency currency = currencyDAO.getCurrency(currencyId);
		if (currency == null) {
			currencyDAO.updateCurrency(new Currency(currencyId,currencyId,currencyId,currencyId,currencyId, ServerConsts.CURRENCY_UNAVAILABLE, "0"));
			return ServerConsts.RET_CODE_SUCCESS;
		} else {
			return ServerConsts.RET_CODE_FAILUE;
		}
	}

}
