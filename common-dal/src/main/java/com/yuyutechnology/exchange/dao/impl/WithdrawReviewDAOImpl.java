package com.yuyutechnology.exchange.dao.impl;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.hibernate4.HibernateTemplate;

import com.yuyutechnology.exchange.dao.WithdrawReviewDAO;
import com.yuyutechnology.exchange.pojo.WithdrawReview;

public class WithdrawReviewDAOImpl implements WithdrawReviewDAO {
	public static Logger logger = LogManager.getLogger(WithdrawReviewDAOImpl.class);
	
	@Resource
	HibernateTemplate hibernateTemplate;

	

	@Override
	public void addWithdraw(WithdrawReview withdrawReview) {
		hibernateTemplate.saveOrUpdate(withdrawReview);
	}
	
	
}
