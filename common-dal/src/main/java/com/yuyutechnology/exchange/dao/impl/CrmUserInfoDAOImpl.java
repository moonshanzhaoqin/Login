package com.yuyutechnology.exchange.dao.impl;

import javax.annotation.Resource;

import org.hibernate.ReplicationMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.CrmUserInfoDAO;
import com.yuyutechnology.exchange.pojo.CrmUserInfo;

@Repository
public class CrmUserInfoDAOImpl implements CrmUserInfoDAO {
	
	@Resource
	HibernateTemplate hibernateTemplate;
	
	public static Logger logger = LoggerFactory.getLogger(CrmUserInfoDAOImpl.class);
	
	@Override
	public void updateUserInfo(CrmUserInfo crmUserInfo){
		
		hibernateTemplate.replicate(crmUserInfo, ReplicationMode.LATEST_VERSION);
		
	}

}
