package com.yuyutechnology.exchange.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.GoldpayDAO;

@Repository
public class GoldpayDAOImpl implements GoldpayDAO {

	public static Logger logger = LogManager.getLogger(GoldpayDAOImpl.class);
	
	@Resource
	JdbcTemplate goldpayJdbcTemplate;
	
	@Resource
	HibernateTemplate hibernateTemplate;
	
	@Override
	public int getGoldpayAccountTotalCount() {
		return goldpayJdbcTemplate.queryForObject("SELECT COUNT(0) FROM `goldq_account` WHERE `balance` > 0", Integer.class);
	}

	@Override
	public List<Map<String, Object>> getGoldpayAccountList(int start, int size) {
		return goldpayJdbcTemplate.queryForList("SELECT `balance`,`user_id`,`account_id` FROM `goldq_account` WHERE `balance` > 0 LIMIT "+start+", "+size);
	}
	
	@Override
	public void replaceGAccount(final List<Map<String, Object>> lists) {
		hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				StringBuilder sb = new StringBuilder();
				sb.append("REPLACE INTO `g_account` (`balance`, `user_id`, `account_id`) VALUES ");
				for (Map<String, Object> map : lists) {
					sb.append("(")
					.append("'").append(map.get("balance")).append("'").append(",")
					.append("'").append(map.get("user_id")).append("'").append(",")
					.append("'").append(map.get("account_id")).append("'")
					.append("),");
				}
				String sql = sb.toString();
				sql = sql.substring(0, sql.length()-1);
				logger.info(sql);
				Query query = session.createSQLQuery(sql);
				return query.executeUpdate();
			}
		});
	}

}
//INSERT INTO `anytime_exchange`.`g_account` (`balance`, `user_id`, `account_id`) VALUES ('111', '11111', '1111');