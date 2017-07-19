package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.TransDetailsDAO;
import com.yuyutechnology.exchange.pojo.TransDetails;

@Repository
public class TransDetailsDAOImpl implements TransDetailsDAO {
	
	private static Logger logger = LogManager.getLogger(TransDetailsDAOImpl.class);
	
	@Resource
	HibernateTemplate hibernateTemplate;
	
	@Override
	public void addTransDetails(TransDetails transDetails){
		hibernateTemplate.save(transDetails);
	}
	
	@Override
	public TransDetails getTransDetails(Integer userId,String transId) {
		
		List<?> list = hibernateTemplate.find("from TransDetails where transferId = ? and userId = ?", transId,userId);
		
		if(list.isEmpty() || list.size() > 1){
			return null;
		}
		
		return (TransDetails) list.get(0);
		
	}
	
	@Override
	public List<?> getTransDetailsByTransIdAndUserId(final Integer userId,final String transId){
		
		StringBuffer sb = new StringBuffer("SELECT ");
		sb.append("t1.user_id, ");
		sb.append("t1.transfer_id, ");
		sb.append("t1.trans_currency, ");
		sb.append("t1.trans_amount, ");
		sb.append("t3.currency_unit, ");
		sb.append("t1.trans_remarks, ");
		sb.append("t2.transfer_type, ");
		sb.append("t2.create_time, ");
		sb.append("t2.finish_time, ");
		sb.append("t1.trader_name, ");
		sb.append("t1.trader_area_code, ");
		sb.append("t1.trader_phone, ");
		sb.append("t2.goldpay_name, ");
		sb.append("t2.paypal_currency, ");
		sb.append("t2.paypal_exchange ");
		sb.append("FROM e_trans_details t1 ");
		sb.append("LEFT JOIN e_transfer t2 ON t1.transfer_id = t2.transfer_id ");
		sb.append("LEFT JOIN e_currency t3 ON t1.trans_currency = t3.currency ");
		sb.append("where t1.user_id = ? AND t1.transfer_id = ? ");
		
		final String sql = sb.toString();
		logger.info("sql  : {}",sql);
		
		List<?> list = hibernateTemplate.executeWithNativeSession(new HibernateCallback<List<?>>() {
			@Override
			public List<?> doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery(sql);
				query.setInteger(0, userId);
				query.setString(1, transId);
				return query.list();
			}
		});
		
		return list;
	}

	@Override
	public void updateTransDetails(TransDetails transDetails) {
		// TODO Auto-generated method stub
		hibernateTemplate.saveOrUpdate(transDetails);
	}
	
	

}
