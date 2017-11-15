package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.hql.internal.ast.HqlASTFactory;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.UserConfig;
import com.yuyutechnology.exchange.util.DateFormatUtils;

@Repository
public class UserDAOImpl implements UserDAO {
	public static Logger logger = LogManager.getLogger(UserDAOImpl.class);
	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public Integer addUser(User user) {
		Integer userId = (Integer) hibernateTemplate.save(user);
		return userId;
	}

	@Override
	public User getSystemUser() {
		List<?> list = hibernateTemplate.find("from User where userType = ?", ServerConsts.USER_TYPE_OF_SYSTEM);
		return list.isEmpty() ? null : (User) list.get(0);
	}

	@Override
	public User getUser(Integer userId) {
		return hibernateTemplate.get(User.class, userId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserByPushId(String pushId) {
		List<?> list = hibernateTemplate.find("from User where pushId = ? ", pushId);
		return list.isEmpty() ? null : (List<User>) list;
	}

	@Override
	public User getUserByUserPhone(String areaCode, String userPhone) {
		List<?> list = hibernateTemplate.find("from User where areaCode = ? and userPhone = ?", areaCode, userPhone);
		return list.isEmpty() ? null : (User) list.get(0);
	}

	@Override
	public UserConfig getUserConfig(Integer userId) {
		return hibernateTemplate.get(UserConfig.class, userId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> listAllUser() {
		List<?> list = hibernateTemplate.find("from User");
		return list.isEmpty() ? null : (List<User>) list;
	}

	@Override
	public void saveUserConfig(UserConfig userConfig) {
		hibernateTemplate.saveOrUpdate(userConfig);
	}

	@Override
	public void updateSQL(final String sql, final Object[] values) {
		logger.info(sql);
		logger.info(values);
		hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				StringBuilder sqlsb = new StringBuilder(sql);
				Query query = session.createSQLQuery(sqlsb.toString());
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.executeUpdate();
			}
		});
	}

	@Override
	public void updateUser(User user) {
		hibernateTemplate.update(user);
	}

}
