package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.NotificationDAO;
import com.yuyutechnology.exchange.pojo.TransactionNotification;

@Repository
public class NotificationDAOImpl implements NotificationDAO {
	
	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public void addNotification(TransactionNotification transactionNotification) {
		hibernateTemplate.save(transactionNotification);
	}

	@Override
	public TransactionNotification getNotificationById(int noticeId) {
		return hibernateTemplate.get(TransactionNotification.class,noticeId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TransactionNotification> getNotificationListByUserId(int userId) {
		List<?> list = hibernateTemplate.find("from TransactionNotification where payerId = ?", userId);
		if(!list.isEmpty()){
			return (List<TransactionNotification>) list;
		}
		return null;
	}

	@Override
	public void updateNotification(TransactionNotification transactionNotification) {
		hibernateTemplate.saveOrUpdate(transactionNotification);
	}

}
