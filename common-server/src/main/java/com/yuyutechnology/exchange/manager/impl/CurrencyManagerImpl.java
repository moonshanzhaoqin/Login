package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.CurrencyManager;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.Wallet;

@Service
public class CurrencyManagerImpl implements CurrencyManager {

	private static Logger logger = LogManager.getLogger(CurrencyManagerImpl.class);

	@Autowired
	CurrencyDAO currencyDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CommonManager commonManager;
	@Autowired
	OandaRatesManager oandaRatesManager;

	@Override
	public List<Currency> getCurrencyList() {
		logger.info("get currencies");
		return currencyDAO.getCurrencys();
	}

	@Override
	public void updateCurrency(Currency currency) {
		currencyDAO.updateCurrency(currency);
	}

	@Override
	public String addCurrency(String currencyId) {
		Currency currency = currencyDAO.getCurrency(currencyId);
		if (currency == null) {
			currency = new Currency(currencyId, currencyId, currencyId, currencyId, currencyId,
					ServerConsts.CURRENCY_UNAVAILABLE, 0);
			currencyDAO.updateCurrency(currency);
			// 为系统用户添加新钱包
			walletDAO.addwallet(new Wallet(currency, userDAO.getSystemUser().getUserId(), BigDecimal.ZERO, new Date(),0));
			// 强制刷新汇率缓存
//			oandaRatesManager.updateExchangeRates();
			return RetCodeConsts.RET_CODE_SUCCESS;
		} else {
			logger.info("Currency {} is exist", currencyId);
			return RetCodeConsts.CURRENCY_IS_EXIST;
		}
	}

	@Override
	public String changeCurrencyStatus(String currencyId, int status) {
		Currency currency = currencyDAO.getCurrency(currencyId);
		if (currency == null) {
			logger.warn("Currency {} is not exist", currencyId);
			return RetCodeConsts.RET_CODE_FAILUE;
		} else {
			currency.setCurrencyStatus(status);
			currencyDAO.updateCurrency(currency);
			return RetCodeConsts.RET_CODE_SUCCESS;
		}
	}
}
