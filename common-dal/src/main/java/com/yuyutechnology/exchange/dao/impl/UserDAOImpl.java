package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.UserConfig;

@Repository
public class UserDAOImpl implements UserDAO {
	public static Logger logger = LogManager.getLogger(UserDAOImpl.class);
	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public User getSystemUser() {
		List<?> list = hibernateTemplate.find("from User where userType = ?", ServerConsts.USER_TYPE_OF_SYSTEM);
		return list.isEmpty() ? null : (User) list.get(0);
	}

	@Override
	public User getUser(Integer userId) {
		return hibernateTemplate.get(User.class, userId);
	}

	@Override
	public User getUserByUserPhone(String areaCode, String userPhone) {
		List<?> list = hibernateTemplate.find("from User where areaCode = ? and userPhone = ?", areaCode, userPhone);
		return list.isEmpty() ? null : (User) list.get(0);
	}
	
	@Override
	public User getUserByPhone(String userPhone) {
		List<?> list = hibernateTemplate.find("from User where and userPhone = ?", userPhone);
		return list.isEmpty() ? null : (User) list.get(0);
	}

	// @SuppressWarnings("unchecked")
	@Override
	public Integer addUser(User user) {
		Integer userId = (Integer) hibernateTemplate.save(user);
		// List<Currency> currencies = (List<Currency>)
		// hibernateTemplate.find("from Currency");
		// for (Currency currency : currencies) {
		// hibernateTemplate.saveOrUpdate(new Wallet(userId,
		// currency.getCurrency(), new BigDecimal(0), new Date()));
		// }
		return userId;
	}

	@Override
	public void updateUser(User user) {
		hibernateTemplate.saveOrUpdate(user);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserList() {
		List<?> list = hibernateTemplate.find("from User");
		return list.isEmpty() ? null : (List<User>) list;
	}

	@Override
	public UserConfig getUserConfig(Integer userId) {
		return hibernateTemplate.get(UserConfig.class, userId);
	}

	@Override
	public void saveUserConfig(UserConfig userConfig) {
		hibernateTemplate.saveOrUpdate(userConfig);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserByPushId(String pushId) {
		List<?> list = hibernateTemplate.find("from User where pushId = ? ",pushId);
		return list.isEmpty() ? null : (List<User>) list;
		
	}

}
