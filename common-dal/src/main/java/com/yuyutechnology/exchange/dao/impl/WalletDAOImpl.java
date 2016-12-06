package com.yuyutechnology.exchange.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
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
	public void updateWalletByUserIdAndCurrency(final int userId, final String currency, final BigDecimal amount, final String capitalFlows) {
		hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query;
				if(capitalFlows.equals("+")){
					query = session.createQuery("update Wallet set updateTime = ? ,balance = balance+"
							+amount+"where userId = ? and currency = ?");
				}else{
					query = session.createQuery("update Wallet set updateTime = ? ,balance = balance-"
							+amount+"where userId = ? and currency = ?");
				}
				query.setTimestamp(0, new Date());
				query.setInteger(1, userId);
				query.setString(2, currency);
				
				return query.executeUpdate();
			}
		});
		
	}
}
