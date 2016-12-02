package com.yuyutechnology.exchange.server.dao.Impl;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.server.dao.TestDao;

@Repository
public class TestDaoImpl implements TestDao {
	
	@Resource
	HibernateTemplate hibernateTemplate;
}
