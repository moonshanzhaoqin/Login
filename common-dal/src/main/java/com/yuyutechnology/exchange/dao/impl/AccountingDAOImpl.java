/**
 * 
 */
package com.yuyutechnology.exchange.dao.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.AccountingDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.util.DateFormatUtils;

/**
 * @author silent.sun
 *
 */
@Repository
public class AccountingDAOImpl implements AccountingDAO{

	private static Logger logger = LogManager.getLogger(AccountingDAOImpl.class);
	
	private final String ACCOUNTINGTIMEKEY = "accountingTimeKey";

	@Resource
	HibernateTemplate hibernateTemplate;
	
	@Autowired
	RedisDAO redisDAO;
	
	public Integer snapshotWalletToBeforeByUser(final int userId) {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery(
						"replace into e_wallet_before (user_id,currency,balance,update_time,update_seq_id) "
								+ "select user_id,currency,balance,update_time,update_seq_id from e_wallet where user_id = ?");
				query.setInteger(0, userId);
				return query.executeUpdate();
			}
		});
	}

	public Integer snapshotWalletToNow() {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery(
						"replace into e_wallet_now (user_id,currency,balance,update_time,update_seq_id) "
								+ "select user_id,currency,balance,update_time,update_seq_id from e_wallet ");
//				Query query = session.createSQLQuery(
//						"replace into e_wallet_now (user_id,currency,balance,update_time,update_seq_id) "
//								+ "select user_id,currency,balance,update_time,update_seq_id from e_wallet "
//								+ "where update_time > ? and update_time <= ?");
//				query.setString(0, DateFormatUtils.formatDate(stareDate));
//				query.setString(1, DateFormatUtils.formatDate(endDate));
				return query.executeUpdate();
			}
		});
	}

	public Integer accountingWalletSeq(final long seqIdStart, final long seqIdEnd, final Date startDate,
			final Date endDate) {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				StringBuilder sql = new StringBuilder();
				sql.append("insert into e_bad_account (user_id,currency,sum_amount,balance_history,balance_now,start_time,end_time,start_seq_id,end_seq_id) ")
				.append("select w.user_id,w.currency,coalesce(ws.sum_amount, 0),coalesce(w2.balance,0), w.balance,?,?,?,? from e_wallet_now w ")
				.append("left join (select user_id,currency, SUM(amount) as sum_amount from e_wallet_seq ")
				.append("where seq_id > ? and seq_id <= ? group by user_id, currency ")
				.append(") ws on w.user_id = ws.user_id and ws.currency = w.currency left join e_wallet_before w2 on w.user_id = w2.user_id and w.currency = w2.currency ")
				.append("where coalesce(ws.sum_amount, 0) + coalesce(w2.balance,0) != coalesce(w.balance,0)");
//				sql.append("insert into e_bad_account (user_id,currency,sum_amount,balance_history,balance_now,start_time,end_time,start_seq_id,end_seq_id) ")
//						.append("select user_id,currency,sum_amount,coalesce(balance_before,0),coalesce(balance_now,0),?,?,?,? ")
//						.append("from (select ws.user_id as user_id,ws.currency as currency,SUM(ws.amount) as sum_amount,w.balance as balance_before,w2.balance as balance_now ")
//						.append("from e_wallet_seq ws left join e_wallet_before w on ws.user_id = w.user_id and ws.currency = w.currency left join e_wallet_now w2 on ws.user_id = w2.user_id and ws.currency = w2.currency ")
//						.append("where ws.seq_id > ? and ws.seq_id <= ? group by ws.user_id, ws.currency")
//						.append(") tmp where sum_amount + coalesce(balance_before,0) != coalesce(balance_now,0)");
				Query query = session.createSQLQuery(sql.toString());
				query.setString(0, DateFormatUtils.formatDate(startDate));
				query.setString(1, DateFormatUtils.formatDate(endDate));
				query.setLong(2, seqIdStart);
				query.setLong(3, seqIdEnd);
				query.setLong(4, seqIdStart);
				query.setLong(5, seqIdEnd);
				return query.executeUpdate();
			}
		});
	}

	public Integer snapshotWalletNowToHistory() {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery(
						"replace into e_wallet_before (user_id,currency,balance,update_time,update_seq_id) "
								+ "select user_id,currency,balance,update_time,update_seq_id from e_wallet_now");
				return query.executeUpdate();
			}
		});
	}

	public void cleanSnapshotWalletNow() {
		hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery("truncate table e_wallet_now");
				return query.executeUpdate();
			}
		});
	}

	public Long getMAXSeqId4WalletNow() {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery("select max(update_seq_id) from e_wallet_now");
				List list = query.list();
				if (list == null || list.isEmpty() || list.get(0) == null) {
					return 0l;
				}
				return ((BigInteger) list.get(0)).longValue();
			}
		});
	}

	public Long getMAXSeqId4WalletBefore() {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery("select max(update_seq_id) from e_wallet_before");
				List list = query.list();
				if (list == null || list.isEmpty() ||  list.get(0) == null) {
					return 0l;
				}
				return ((BigInteger) list.get(0)).longValue();
			}
		});
	}
	
	public Long getMAXSeqId4WalletBeforeByUserId(final int userId) {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery("select max(update_seq_id) from e_wallet_before where user_id = ?");
				query.setInteger(0, userId);
				List list = query.list();
				if (list == null || list.isEmpty() || list.get(0) == null) {
					return 0l;
				}
				return ((BigInteger) list.get(0)).longValue();
			}
		});
	}
	
	public Long getMAXSeqId4WalletUserId(final int userId) {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery("select max(update_seq_id) from e_wallet where user_id = ?");
				query.setInteger(0, userId);
				List list = query.list();
				if (list == null || list.isEmpty() || list.get(0) == null) {
					return 0l;
				}
				return ((BigInteger) list.get(0)).longValue();
			}
		});
	}
	
	public Integer calculatorWalletSeqByUserId(final long seqIdStart, final long seqIdEnd, final Date startDate,
			final Date endDate, final int userId, final String transferId) {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				StringBuilder sql = new StringBuilder();
//				sql.append("insert into e_bad_account (user_id,currency,sum_amount,balance_history,balance_now,start_time,end_time,start_seq_id,end_seq_id,bad_account_status) ")
//						.append("select user_id,currency,sum_amount,coalesce(balance_before,0),coalesce(balance_now,0),?,?,?,?,1 ")
//						.append("from (select ws.user_id as user_id,ws.currency as currency,SUM(ws.amount) as sum_amount,w.balance as balance_before,w2.balance as balance_now ")
//						.append("from e_wallet_seq ws left join e_wallet_before w on ws.user_id = w.user_id and ws.currency = w.currency left join e_wallet w2 on ws.user_id = w2.user_id and ws.currency = w2.currency ")
//						.append("where ws.seq_id > ? and ws.seq_id <= ? and ws.user_id = ? group by ws.currency")
//						.append(") tmp where sum_amount + coalesce(balance_before,0) != coalesce(balance_now,0)");

				sql.append("insert into e_bad_account (user_id,currency,sum_amount,balance_history,balance_now,start_time,end_time,start_seq_id,end_seq_id,bad_account_status,transfer_id) ")
				.append("select w.user_id,w.currency,coalesce(ws.sum_amount, 0),coalesce(w2.balance,0), w.balance,?,?,?,?,1,? from e_wallet w ")
				.append("left join (select user_id,currency, SUM(amount) as sum_amount from e_wallet_seq ")
				.append("where seq_id > ? and seq_id <= ? and user_id = ? group by currency ")
				.append(") ws on w.user_id = ws.user_id and ws.currency = w.currency left join e_wallet_before w2 on w.user_id = w2.user_id and w.currency = w2.currency ")
				.append("where w.user_id = ? and coalesce(ws.sum_amount, 0) + coalesce(w2.balance,0) != coalesce(w.balance,0)");
				
				Query query = session.createSQLQuery(sql.toString());
				query.setString(0, DateFormatUtils.formatDate(startDate));
				query.setString(1, DateFormatUtils.formatDate(endDate));
				query.setLong(2, seqIdStart);
				query.setLong(3, seqIdEnd);
				query.setString(4, transferId);
				query.setLong(5, seqIdStart);
				query.setLong(6, seqIdEnd);
				query.setLong(7, userId);
				query.setLong(8, userId);
				return query.executeUpdate();
			}
		});
	}
	
//	public Object[] calculatorWalletSeqByUserId(final long seqIdStart, final long seqIdEnd, final int userId) {
//		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Object[]>() {
//			@Override
//			public Object[] doInHibernate(Session session) throws HibernateException {
//				StringBuilder sql = new StringBuilder();
//				sql.append("select SUM(ws.amount) as sum_amount, w.balance as balance_before, w.update_time as update_time from e_wallet_seq ws ")
//						.append("left join e_wallet_before w on ws.user_id = w.user_id and ws.currency = w.currency ")
//						.append("where ws.user_id = ? and ws.currency = ? and ws.seq_id > ? and ws.seq_id <= ?");
//				Query query = session.createSQLQuery(sql.toString());
//				query.setInteger(0, userId);
//				query.setString(1, currency);
//				query.setLong(2, seqIdStart);
//				query.setLong(3, seqIdEnd);
//				List r = query.list();
//				if (r == null || r.isEmpty()) {
//					return null;
//				}else{
//					return (Object[]) r.get(0);
//				}
//			}
//		});
//	}

	public Date getLastAccountingTime() {
		String timeString = redisDAO.getValueByKey(ACCOUNTINGTIMEKEY);
		if (StringUtils.isBlank(timeString)) {
			return DateFormatUtils.fromString("2017-01-01 00:00:00");
		}else{
			return DateFormatUtils.fromString(timeString);
		}
	}
	
	public void saveLastAccountingTime(Date time) {
		redisDAO.saveData(ACCOUNTINGTIMEKEY, DateFormatUtils.formatDate(time));
	}

}
