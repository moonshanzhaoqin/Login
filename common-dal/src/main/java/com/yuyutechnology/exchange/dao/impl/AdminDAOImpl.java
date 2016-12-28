package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.AdminDAO;
import com.yuyutechnology.exchange.pojo.Admin;

@Repository
public class AdminDAOImpl implements AdminDAO {
	public static Logger logger = LoggerFactory.getLogger(AdminDAOImpl.class);
	
	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public Admin getAdminByName(String adminName) {
		List<?> list = hibernateTemplate.find("from Admin where adminName = ?", adminName);
		if (!list.isEmpty()) {
			return (Admin) list.get(0);
		}
		return null;
	}
}
