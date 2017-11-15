/**
 * 
 */
package com.yuyutechnology.exchange.dao.impl;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.CrmLogDAO;
import com.yuyutechnology.exchange.pojo.CrmLog;

/**
 * @author suzan.wu
 *
 */
@Repository
public class CrmLogDAOImpl implements CrmLogDAO {
	public static Logger logger = LogManager.getLogger(CrmLogDAOImpl.class);
	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public void saveCrmLog(CrmLog crmLog) {
		hibernateTemplate.saveOrUpdate(crmLog);
	}
}
