package com.yuyutechnology.exchange.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.NotificationDAO;
import com.yuyutechnology.exchange.pojo.TransactionNotification;
import com.yuyutechnology.exchange.utils.page.PageUtils;

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
	
	@Override
	public HashMap<String, Object> getNotificationRecordsByPage(String sql,String countSql,
			List<Object> values,int currentPage, int pageSize) {
		
		int firstResult = (currentPage -1)*pageSize;
		int masResult = pageSize;
		
		List<?> list = PageUtils.getListByPage4MySql(hibernateTemplate, sql, values, firstResult, masResult);
		
		long total = PageUtils.getTotal4MySql(hibernateTemplate, countSql, values);
		int pageTotal = PageUtils.getPageTotal(total, pageSize);
		
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("currentPage",currentPage);
		map.put("pageSize",pageSize);
		map.put("total",total);
		map.put("pageTotal",pageTotal);
		map.put("list",list);
		
		return map;
		
	}

}
