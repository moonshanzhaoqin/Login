package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.CollectDAO;
import com.yuyutechnology.exchange.pojo.Collect;

@Repository
public class CollectDAOImpl implements CollectDAO {

	@Resource
	HibernateTemplate hibernateTemplate;

	public static Logger logger = LogManager.getLogger(CollectDAOImpl.class);

	@Override
	public Collect getCollect(Integer collectId) {
		return hibernateTemplate.get(Collect.class, collectId);
	}

	@Override
	public void updateCollect(Collect collect) {
		hibernateTemplate.saveOrUpdate(collect);
	}

	@Override
	public Collect findHQL(String hql, Object[] values) {
		List<?> list=hibernateTemplate.find(hql,values);
		return list.isEmpty() ? null : (Collect) list.get(0);
	}
	
	
}
