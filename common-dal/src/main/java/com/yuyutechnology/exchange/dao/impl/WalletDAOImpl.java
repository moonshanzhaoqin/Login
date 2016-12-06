package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.pojo.Wallet;

@Repository
public class WalletDAOImpl implements WalletDAO {

	@Resource
	HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<Wallet> getWalletsByUserId(int userId) {
		List<?> list = hibernateTemplate.find("from Wallet where userId = ?", userId);
		return (List<Wallet>) list;
	}

	@Override
	public Wallet getWalletByUserIdAndCurrency(int userId, String currency) {
		Wallet wallet = null;
		List<?> list = hibernateTemplate.find("from Wallet where userId = ? and currency = ?", userId,currency);
		if(!list.isEmpty()){
			wallet = (Wallet) list.get(0);
		}
		return wallet;
	}

	@Override
	public void addwallet(Wallet wallet) {
		// TODO Auto-generated method stub
		
	}
}
