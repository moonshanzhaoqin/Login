/**
 * 
 */
package com.yuyutechnology.exchange.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.BadAccountDAO;
import com.yuyutechnology.exchange.pojo.BadAccount;
import com.yuyutechnology.exchange.utils.page.PageBean;
import com.yuyutechnology.exchange.utils.page.PageUtils;

/**
 * @author silent.sun
 *
 */
@Repository
public class BadAccountDAOImpl implements BadAccountDAO {

	@Resource
	HibernateTemplate hibernateTemplate;
	
	@Override
	public BadAccount getBadAccount(Integer badAccountId){
		return hibernateTemplate.get(BadAccount.class, badAccountId);
	}
	
	@Override
	public void saveBadAccount(BadAccount badAccount) {
		hibernateTemplate.saveOrUpdate(badAccount);
	}

	@Override
	public  List<BadAccount> findBadAccountList(int badAccountStatus) {
		List<?> list = hibernateTemplate.find("from BadAccount where badAccountStatus = ?", badAccountStatus);
		return (List<BadAccount>) list;
	}

	@Override
	public PageBean getBadAccountByPage(int currentPage, int pageSize) {
		List<Object> values = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from BadAccount b,User u where u.userId=b.userId");
		PageBean pageBean = PageUtils.getPageContent(hibernateTemplate, hql.toString(), values, currentPage, pageSize);
		return pageBean;
	}

}
