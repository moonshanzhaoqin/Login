package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.pojo.User;

@Repository
public class UserDAOImpl implements UserDAO {
	public static Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
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
	public User getUserByUserPhone(String areaCode, String userPhone) {
		List<?> list = hibernateTemplate.find("from User where areaCode = ? and userPhone = ?", areaCode, userPhone);
//		logger.info("{}",list);
		if (!list.isEmpty()) {
			return (User) list.get(0);
		}
		return null;
	}

//	@SuppressWarnings("unchecked")
	@Override
	public Integer addUser(User user) {
		Integer userId = (Integer) hibernateTemplate.save(user);
//		List<Currency> currencies = (List<Currency>) hibernateTemplate.find("from Currency");
//		for (Currency currency : currencies) {
//			hibernateTemplate.saveOrUpdate(new Wallet(userId, currency.getCurrency(), new BigDecimal(0), new Date()));
//		}
		return userId;
	}

	@Override
	public void updateUser(User user) {
		hibernateTemplate.saveOrUpdate(user);

	}

}
