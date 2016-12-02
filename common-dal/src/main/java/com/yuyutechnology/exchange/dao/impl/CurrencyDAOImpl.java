package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.pojo.Currency;

@Repository
public class CurrencyDAOImpl implements CurrencyDAO {
	
	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public Currency getCurrency(String currency) {
		return hibernateTemplate.get(Currency.class, currency);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Currency> getCurrencys() {
		List<?> list = hibernateTemplate.find("from Currency");
		return (List<Currency>) list;
	}

}
