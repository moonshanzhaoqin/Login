package com.yuyutechnology.exchange.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.yuyutechnology.exchange.util.page.PageUtils;

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

	@Override
	public Integer addUser(User user) {
		Integer userId = (Integer) hibernateTemplate.save(user);
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
		List<?> list = hibernateTemplate.find("from User where pushId = ? ", pushId);
		return list.isEmpty() ? null : (List<User>) list;
	}

	@Override
	public void updateHQL(String hql, Object[] values) {
		hibernateTemplate.bulkUpdate(hql, values);
	}

	@Override
	public long get24HRegistration() {
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		calendar.add(Calendar.DATE, -1);
		
		String hql = "from CrmUserInfo where createTime > ? and createTime < ?";
		List<Object> values = new ArrayList<Object>();
		values.add(calendar.getTime());
		values.add(now);
		return PageUtils.getTotal(hibernateTemplate, hql, values);
	}

	
}
