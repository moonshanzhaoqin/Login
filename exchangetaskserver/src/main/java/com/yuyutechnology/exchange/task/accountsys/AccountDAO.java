/**
 * 
 */
package com.yuyutechnology.exchange.task.accountsys;

import java.math.BigDecimal;
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

import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.utils.DateFormatUtils;

/**
 * @author silent.sun
 *
 */
@Repository
public class AccountDAO {

	private static Logger logger = LogManager.getLogger(AccountManager.class);
	
	private final String ACCOUNTINGTIMEKEY = "accountingTimeKey";

	@Resource
	HibernateTemplate hibernateTemplate;
	
	@Autowired
	RedisDAO redisDAO;

	public Integer snapshotWalletToNow(final Date stareDate, final Date endDate) {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery(
						"replace into e_wallet_now (user_id,currency,balance,update_time,update_seq_id) "
								+ "select user_id,currency,balance,update_time,update_seq_id from e_wallet "
								+ "where update_time > ? and update_time <= ? and balance != 0");
				query.setString(0, DateFormatUtils.formatDate(stareDate));
				query.setString(1, DateFormatUtils.formatDate(endDate));
				return query.executeUpdate();
			}
		});
	}

	public Integer accountingWalletSeq(final int seqIdStart, final int seqIdEnd, final Date stareDate,
			final Date endDate) {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				StringBuilder sql = new StringBuilder();
				sql.append("insert into e_bad_account (user_id,currency,sum_amount,balance_before,balance_now,start_time,end_time) ")
						.append("select user_id,currency,sum_amount,balance_before,balance_now,?,? ")
						.append("from (select ws.user_id as user_id,ws.currency as currency,SUM(ws.amount) as sum_amount,w.balance as balance_before,w2.balance as balance_now ")
						.append("from e_wallet_seq ws left join e_wallet_before w on ws.user_id = w.user_id and ws.currency = w.currency left join e_wallet_now w2 on ws.user_id = w2.user_id and ws.currency = w2.currency ")
						.append("where ws.seq_id > ? and ws.seq_id <= ? group by ws.user_id, ws.currency")
						.append(") tmp where sum_amount + balance_before != balance_now");
				Query query = session.createSQLQuery(sql.toString());
				query.setString(0, DateFormatUtils.formatDate(stareDate));
				query.setString(1, DateFormatUtils.formatDate(endDate));
				query.setInteger(2, seqIdStart);
				query.setInteger(3, seqIdEnd);
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

	public Integer getMAXSeqId4WalletNow() {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery("select max(update_seq_id) from e_wallet_now");
				return (Integer) query.list().get(0);
			}
		});
	}

	public Integer getMAXSeqId4WalletBefore() {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery("select max(update_seq_id) from e_wallet_before");
				return (Integer) query.list().get(0);
			}
		});
	}
	
	public Object[] calculatorWalletSeqByUserIdAndCurrency(final int seqIdEnd, final int userId, final String currency) {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Object[]>() {
			@Override
			public Object[] doInHibernate(Session session) throws HibernateException {
				StringBuilder sql = new StringBuilder();
				sql.append("select SUM(ws.amount) as sum_amount, w.balance as balance_before, w.update_time as update_time from e_wallet_seq ws ")
						.append("left join e_wallet_before w on ws.user_id = w.user_id and ws.currency = w.currency ")
						.append("where ws.user_id = ? and ws.currency = ? and ws.seq_id > w.update_seq_id and ws.seq_id <= ?");
				Query query = session.createSQLQuery(sql.toString());
				query.setInteger(0, userId);
				query.setString(1, currency);
				query.setInteger(2, seqIdEnd);
				List r = query.list();
				if (r == null || r.isEmpty()) {
					return null;
				}else{
					return (Object[]) r.get(0);
				}
			}
		});
	}
	
	public Integer saveBadAccount(final int userId, final String currency, final BigDecimal sumAmount, final BigDecimal balanceBefore, final BigDecimal balanceNow, final Date stareDate,
			final Date endDate) {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				StringBuilder sql = new StringBuilder("insert into e_bad_account (user_id,currency,sum_amount,balance_before,balance_now,start_time,end_time) values (?,?,?,?,?,?,?) ");
				Query query = session.createSQLQuery(sql.toString());
				query.setInteger(0, userId);
				query.setString(1, currency);
				query.setBigDecimal(2, sumAmount);
				query.setBigDecimal(3, balanceBefore);
				query.setBigDecimal(4, balanceNow);
				query.setString(5, DateFormatUtils.formatDate(endDate));
				query.setString(6, DateFormatUtils.formatDate(endDate));
				return query.executeUpdate();
			}
		});
	}
	
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
