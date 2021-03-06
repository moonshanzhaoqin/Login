package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.pojo.Currency;

@Repository
public class CurrencyDAOImpl implements CurrencyDAO {

	@Resource
	HibernateTemplate hibernateTemplate;
	
	public static Logger logger = LogManager.getLogger(CurrencyDAOImpl.class);

	@Override
	public Currency getCurrency(String currency) {
		return hibernateTemplate.get(Currency.class, currency);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Currency> getCurrencys() {
		List<?> list = hibernateTemplate.find("from Currency order by currencyOrder");
		return (List<Currency>) list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Currency> getCurrentCurrency() {
		List<?> list = hibernateTemplate.find("from Currency where currencyStatus = ?",
				ServerConsts.CURRENCY_AVAILABLE);
		return (List<Currency>) list;
	}

	@Override
	public void updateCurrency(Currency currency) {
		hibernateTemplate.saveOrUpdate(currency);
	}
}
