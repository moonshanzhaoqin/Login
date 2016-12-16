package com.yuyutechnology.exchange.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;

@Repository
public class WalletDAOImpl implements WalletDAO {
	
	private int systemUserId;

	@Resource
	HibernateTemplate hibernateTemplate;
	
	@PostConstruct
	public void updateSystemUserId(){
		List<?> list = hibernateTemplate.find("from User where userType = ?", ServerConsts.USER_TYPE_OF_SYSTEM);
		if (!list.isEmpty()) {
			systemUserId = ((User) list.get(0)).getUserId();
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Wallet> getWalletsByUserId(int userId) {
		List<?> list = hibernateTemplate.find("from Wallet where userId = ?", userId);
		return (List<Wallet>) list;
	}

	@Override
	public Wallet getWalletByUserIdAndCurrency(int userId, String currency) {
		Wallet wallet = null;
		List<?> list = hibernateTemplate.find("from Wallet where userId = ? and currency.currency = ?", userId,currency);
		if(!list.isEmpty()){
			wallet = (Wallet) list.get(0);
		}
		return wallet;
	}

	@Override
	public Integer updateWalletByUserIdAndCurrency(final int userId,final String currency, final BigDecimal amount, final String capitalFlows) {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query;
				if(capitalFlows.equals("+")){
					query = session.createQuery("update Wallet set updateTime = ? ,balance = balance+"
							+amount+" where userId = ? and currency.currency = ?");
				}else{
					query = session.createQuery("update Wallet set updateTime = ? ,balance = balance-"
							+amount+" where userId = ? and currency.currency = ? and userId != ? and balance-"+amount+">0");
					query.setInteger(3, systemUserId);
				}
				query.setTimestamp(0, new Date());
				query.setInteger(1, userId);
				query.setString(2, currency);
				
				return query.executeUpdate();
			}
		});
	}

	@Override
	public void addwallet(Wallet wallet) {
		hibernateTemplate.saveOrUpdate(wallet);
	}
}
