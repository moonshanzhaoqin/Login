package com.yuyutechnology.exchange.dao.impl;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.BindDAO;
import com.yuyutechnology.exchange.pojo.Bind;

@Repository
public class BindDAOImpl implements BindDAO {
	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public Bind getBind(Integer userId) {
		return hibernateTemplate.get(Bind.class, userId);
	}

	@Override
	public void updateBind(Bind bind) {
		hibernateTemplate.saveOrUpdate(bind);
	}

	@Override
	public void clearHappyLivesId(final String happyLivesId) {
		hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery("update e_bind set happy_lives_id = '' where happy_lives_id = ?");
				query.setString(0, happyLivesId);
				return query.executeUpdate();
			}
		});
	}
}
