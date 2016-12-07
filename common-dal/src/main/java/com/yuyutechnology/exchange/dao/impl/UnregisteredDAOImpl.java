package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.UnregisteredDAO;
import com.yuyutechnology.exchange.pojo.Unregistered;

@Repository
public class UnregisteredDAOImpl implements UnregisteredDAO {
	@Resource
	HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<Unregistered> getUnregisteredByUserPhone(String areaCode, String userPhone) {
		List<?> list = hibernateTemplate.find("from Unregistered where areaCode = ? and userPhone = ?", areaCode,
				userPhone);
		return (List<Unregistered>) list;
	}

	@Override
	public void addUnregistered(Unregistered unregistered) {
		hibernateTemplate.save(unregistered);
	}
}
