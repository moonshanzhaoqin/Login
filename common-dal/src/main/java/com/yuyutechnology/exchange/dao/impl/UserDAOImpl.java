package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.pojo.User;

@Repository
public class UserDAOImpl implements UserDAO {
	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public User getUserByUserPhone(String userPhone) {
		List<?> list = hibernateTemplate.find("from User where userPhone = ?", userPhone);
		if (!list.isEmpty()) {
			return (User) list.get(0);
		}
		return null;
	}

	@Override
	public User getUser(Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer addUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
