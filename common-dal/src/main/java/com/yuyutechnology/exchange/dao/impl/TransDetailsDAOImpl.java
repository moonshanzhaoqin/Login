package com.yuyutechnology.exchange.dao.impl;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	
	

}
