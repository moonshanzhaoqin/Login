package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.CrmSupervisorDAO;
import com.yuyutechnology.exchange.pojo.CrmSupervisor;

@Repository
public class CrmSupervisorDAOImpl implements CrmSupervisorDAO {
	
	@Resource
	HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<CrmSupervisor> getCrmSupervisorList() {
		List<?> list = hibernateTemplate.find("from CrmSupervisor");
		if(list.isEmpty()){
			return null;
		}
		return (List<CrmSupervisor>) list;
	}

	@Override
	public CrmSupervisor getCrmSupervisorById(Integer supervisorId) {
		return hibernateTemplate.get(CrmSupervisor.class, supervisorId);
	}

}
