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

	@SuppressWarnings("unchecked")
	@Override
	public List<Bind> getBindByUserId(Integer userId) {
		List<?> list = hibernateTemplate.find("from Bind where userId = ?", userId);
		return (List<Bind>) list;
	}

	@Override
	public void saveBind(Bind bind) {
		hibernateTemplate.saveOrUpdate(bind);
	}
}
