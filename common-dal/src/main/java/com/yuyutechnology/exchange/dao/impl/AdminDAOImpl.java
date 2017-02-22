package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.AdminDAO;
import com.yuyutechnology.exchange.pojo.Admin;

@Repository
public class AdminDAOImpl implements AdminDAO {
	public static Logger logger = LogManager.getLogger(AdminDAOImpl.class);

	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public Admin getAdminByName(String adminName) {
		List<?> list = hibernateTemplate.find("from Admin where adminName = ?", adminName);
		if (!list.isEmpty()) {
			return (Admin) list.get(0);
		}
		return null;
	}

	@Override
	public void updateAdmin(Admin admin) {
		hibernateTemplate.saveOrUpdate(admin);
	}

	@Override
	public Admin getAdmin(Integer adminId) {
		return	hibernateTemplate.get(Admin.class, adminId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Admin> getAdminList(){
		List<?> list = hibernateTemplate.find("from Admin");
		if(list.isEmpty()){
			return null;
		}
		return (List<Admin>) list;
	}
}
