package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.pojo.User;

@Repository
public class UserDAOImpl implements UserDAO {
	@Resource
	HibernateTemplate hibernateTemplate;
	
	@Override
	public User getSystemUser() {
		List<?> list = hibernateTemplate.find("from User where userType = ?", ServerConsts.USER_TYPE_OF_SYSTEM);
		if (!list.isEmpty()) {
			return (User) list.get(0);
		}
		return null;
	}

	@Override
	public User getUser(Integer userId) {
		return hibernateTemplate.get(User.class, userId);
	}

	@Override
	public User getUserByUserPhone(String areaCode,String userPhone) {
		List<?> list = hibernateTemplate.find("from User where areaCode = ? and userPhone = ?",areaCode, userPhone);
		if (!list.isEmpty()) {
			return (User) list.get(0);
		}
		return null;
	}

	@Override
	public Integer addUser(User user) {
		Integer userId=	(Integer) hibernateTemplate.save(user);
		return userId;
	}

	@Override
	public void updateUserPassword(User user) {
		hibernateTemplate.saveOrUpdate(user);
		
	}


}
