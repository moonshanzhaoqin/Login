package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.BindDAO;
import com.yuyutechnology.exchange.pojo.Bind;

@Repository
public class BindDAOImpl implements BindDAO {
	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public Bind getBindByUserId(Integer userId) {
		List<?> list = hibernateTemplate.find("from Bind where userId = ?", userId);
		if (!list.isEmpty()) {
			return (Bind) list.get(0);
		}
		return null;
	}

	@Override
	public void updateBind(Bind bind) {
		hibernateTemplate.saveOrUpdate(bind);
	}
}
