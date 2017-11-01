/**
 * 
 */
package com.yuyutechnology.exchange.dao.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.InviterDAO;
import com.yuyutechnology.exchange.pojo.Inviter;

/**
 * @author suzan.wu
 *
 */
@Repository
public class InviterDAOImpl implements InviterDAO {
	public static Logger logger = LogManager.getLogger(InviterDAOImpl.class);
	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public Inviter getInviter(Integer userId) {
		return hibernateTemplate.get(Inviter.class, userId);
	}

	@Override
	public Integer updateInviter(final Integer userId, final int inviteQuantityIncrement,
			final BigDecimal inviteBonusIncrement ) {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(
							"update Inviter set inviteQuantity = inviteQuantity+"+inviteQuantityIncrement +"  , inviteBonus =inviteBonus+"+inviteBonusIncrement+" where userId = :userId ");
				query.setInteger("userId", userId);
				return query.executeUpdate();
			}
		});
		
	}

	@Override
	public void updateInviter(Inviter inviter) {
		hibernateTemplate.saveOrUpdate(inviter);
	}

}
