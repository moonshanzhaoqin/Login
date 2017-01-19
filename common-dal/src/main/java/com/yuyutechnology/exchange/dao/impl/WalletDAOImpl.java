package com.yuyutechnology.exchange.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	public static Logger logger = LoggerFactory.getLogger(WalletDAOImpl.class);

	@PostConstruct
	public void updateSystemUserId() {
		List<?> list = hibernateTemplate.find("from User where userType = ?", ServerConsts.USER_TYPE_OF_SYSTEM);
		if (!list.isEmpty()) {
			systemUserId = ((User) list.get(0)).getUserId();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Wallet> getWalletsByUserId(int userId) {
		List<?> list = hibernateTemplate.find("from Wallet where userId = ? " + "order by currency.currencyOrder",
				userId);
		return (List<Wallet>) list;
	}

	@Override
	public Wallet getWalletByUserIdAndCurrency(int userId, String currency) {
		Wallet wallet = null;
		List<?> list = hibernateTemplate.find("from Wallet where userId = ? " + "and currency.currency = ?", userId,
				currency);
		if (!list.isEmpty()) {
			wallet = (Wallet) list.get(0);
		}
		return wallet;
	}

	@Override
	public Integer updateWalletByUserIdAndCurrency(final int userId, final String currency, final BigDecimal amount,
			final String capitalFlows) {

		logger.info(
				"updateWalletByUserIdAndCurrency , userId : {} , currency " + ": {}, amount : {}, capitalFlows : {}",
				new Object[] { userId, currency, amount.toString(), capitalFlows });

		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query = null;
				if (capitalFlows.equals("+")) {
//					query = session.createSQLQuery("update e_wallet set update_time = ? ,balance = balance+" + amount.abs()
//					+ " where user_id = ? and currency = ?");
					query = session.createQuery("update Wallet set updateTime = ? ,balance = balance+" + amount.abs()
							+ " where userId = ? and currency.currency = ?");
				} else {
					if (userId != systemUserId) {
//						query = session.createSQLQuery("update e_wallet set update_time = ? ,balance = balance-"
//								+ amount.abs() + " where user_id = ? and currency = ? and balance-"
//								+ amount.abs() + ">=0");
						query = session.createQuery("update Wallet set updateTime = ? ,balance = balance-"
								+ amount.abs() + " where userId = ? and currency.currency = ? and balance-"
								+ amount.abs() + ">=0");
						
					} else {
//						query = session.createSQLQuery("update e_wallet set update_time = ? ,balance = balance-"
//								+ amount.abs() + " where user_id = ? and currency = ?");
						query = session.createQuery("update Wallet set updateTime = ? ,balance = balance-"
								+ amount.abs() + " where userId = ? and currency.currency = ?");
					}

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

	@Override
	public HashMap<String, BigDecimal> getUserAccountTotalAssets(final int systemUserId) {

		HashMap<String, BigDecimal> map = new HashMap<String, BigDecimal>();

		List<?> list = hibernateTemplate.executeWithNativeSession(new HibernateCallback<List<?>>() {
			@Override
			public List<?> doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery(
						"SELECT currency,SUM(balance) " + "FROM `e_wallet` WHERE user_id <> ? GROUP BY currency ");
				query.setInteger(0, systemUserId);
				return query.list();
			}
		});

		if (!list.isEmpty()) {
			for (Object object : list) {
				Object[] obj = (Object[]) object;
				map.put(new String((String) obj[0]), new BigDecimal(obj[1] + ""));
			}
		}
		// logger.info("Map content : {}",map.toString());

		return map;

	}
}
