/**
 * 
 */
package com.yuyutechnology.exchange.dao.impl;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.hibernate4.HibernateTemplate;

import com.yuyutechnology.exchange.dao.InviterDAO;
import com.yuyutechnology.exchange.pojo.Inviter;

/**
 * @author suzan.wu
 *
 */
public class InviterDAOImpl implements InviterDAO {
	public static Logger logger = LogManager.getLogger(InviterDAOImpl.class);
	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public Inviter getInviter(Integer userId) {
		return hibernateTemplate.get(Inviter.class, userId);
	}

	@Override
	public void updateInviter(Inviter inviter) {
	hibernateTemplate.saveOrUpdate(inviter);
		
	}

}
