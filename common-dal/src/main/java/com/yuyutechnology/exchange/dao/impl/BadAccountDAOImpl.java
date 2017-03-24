/**
 * 
 */
package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.BadAccountDAO;
import com.yuyutechnology.exchange.pojo.BadAccount;

/**
 * @author silent.sun
 *
 */
@Repository
public class BadAccountDAOImpl implements BadAccountDAO {

	@Resource
	HibernateTemplate hibernateTemplate;
	
	@Override
	public void saveBadAccount(BadAccount badAccount) {
		hibernateTemplate.saveOrUpdate(badAccount);
	}

	@Override
	public  List<BadAccount> findBadAccountList(int badAccountStatus) {
		List<?> list = hibernateTemplate.find("from BadAccount where badAccountStatus = ? and userPhone = ?", badAccountStatus);
		return (List<BadAccount>) list;
	}

}
