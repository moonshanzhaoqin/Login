/**
 * 
 */
package com.yuyutechnology.exchange.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.BadAccountDAO;
import com.yuyutechnology.exchange.pojo.BadAccount;
import com.yuyutechnology.exchange.util.page.PageBean;
import com.yuyutechnology.exchange.util.page.PageUtils;

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

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> findBadAccountList(int badAccountStatus) {
		List<?> list = hibernateTemplate.find("select distinct(userId) from BadAccount where badAccountStatus = ?", badAccountStatus);
		return (List<Integer>) list;
	}
	
	@Override
	public void updateBadAccountStatus(final int badAccountStatus, final int userId) {
		
		hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery("update e_bad_account set bad_account_status = ? where user_id = ?");
				query.setInteger(0, badAccountStatus);
				query.setInteger(1, userId);
				return query.executeUpdate();
			}
		});
	}

	@Override
	public PageBean getBadAccountByPage(int currentPage, int pageSize) {
		List<Object> values = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from BadAccount b,User u where u.userId=b.userId order by b.startTime desc");
		PageBean pageBean = PageUtils.getPageContent(hibernateTemplate, hql.toString(), values, currentPage, pageSize);
		return pageBean;
	}

	@Override
	public BadAccount getBadAccountByTransferId(String transferId) {
		List<?> list = hibernateTemplate.find("from BadAccount where transferId = ? ", transferId);
		return list.isEmpty() ? null : (BadAccount) list.get(0);
	}

}
