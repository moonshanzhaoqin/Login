package com.yuyutechnology.exchange.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.yuyutechnology.exchange.pojo.WalletSeq;

@Repository
public class WalletDAOImpl implements WalletDAO {

	private int systemUserId;

	@Resource
	HibernateTemplate hibernateTemplate;

	public static Logger logger = LogManager.getLogger(WalletDAOImpl.class);

	@PostConstruct
	public void updateSystemUserId() {
		List<?> list = hibernateTemplate.find("from User where userType = ?", ServerConsts.USER_TYPE_OF_SYSTEM);
		if (!list.isEmpty()) {
			systemUserId = ((User) list.get(0)).getUserId();
		}
	}

	@Override
	public Integer emptyWallet(final int userId, final String currency) {
			return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
				@Override
				public Integer doInHibernate(Session session) throws HibernateException {
					Query query = session.createQuery(
								"update Wallet set updateTime = :updateTime  , balance =:balance where userId = :userId and currency.currency = :currency");
					query.setTimestamp("updateTime", new Date());
					query.setBigDecimal("balance", BigDecimal.ZERO);
					query.setInteger("userId", userId);
					query.setString("currency", currency);
					return query.executeUpdate();
				}
			});
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

	private Integer updateWalletByUserIdAndCurrency(final int userId, final String currency, final BigDecimal amount,
			final String capitalFlows, final long walletSeqId) {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query = null;
				if (capitalFlows.equals("+")) {
					query = session.createQuery(
							"update Wallet set updateTime = :updateTime ,updateSeqId = :updateSeqId , balance = balance+"
									+ amount + " where userId = :userId and currency.currency = :currency");
				} else {
					if (userId != systemUserId) {
						query = session.createQuery(
								"update Wallet set updateTime = :updateTime , updateSeqId = :updateSeqId , balance = balance-"
										+ amount
										+ " where userId = :userId and currency.currency = :currency and balance-"
										+ amount + ">=0");

					} else {
						query = session.createQuery(
								"update Wallet set updateTime = :updateTime , updateSeqId = :updateSeqId , balance = balance-"
										+ amount + " where userId = :userId and currency.currency = :currency");
					}

				}
				query.setTimestamp("updateTime", new Date());
				query.setLong("updateSeqId", walletSeqId);
				query.setInteger("userId", userId);
				query.setString("currency", currency);
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
		return map;

	}

	@Override
	public Integer updateWalletByUserIdAndCurrency(int userId, String currency, BigDecimal amount, String capitalFlows,
			int transferType, String transactionId) {
		amount = amount.abs();
		if (capitalFlows.equals("-")) {
			amount = amount.negate();
		}
		WalletSeq walletSeq = new WalletSeq(userId, transferType, currency, amount, transactionId, new Date());
		hibernateTemplate.save(walletSeq);
		int update = updateWalletByUserIdAndCurrency(userId, currency, amount.abs(), capitalFlows,
				walletSeq.getSeqId());
		if (update == 0) {
			hibernateTemplate.delete(walletSeq);
		}
		return update;
	}
}
