package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.UnregisteredDAO;
import com.yuyutechnology.exchange.pojo.Unregistered;

@Repository
public class UnregisteredDAOImpl implements UnregisteredDAO {
	
	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public void addUnregistered(Unregistered unregistered) {
		hibernateTemplate.save(unregistered);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Unregistered> getUnregisteredByUserPhone(String areaCode, String userPhone) {
		List<?> list = hibernateTemplate.find(
				"from Unregistered where areaCode = ? and userPhone = ? and unregisteredStatus = ?", areaCode,
				userPhone, ServerConsts.UNREGISTERED_STATUS_OF_PENDING);
		return (List<Unregistered>) list;
	}

	@Override
	public void updateUnregistered(Unregistered unregistered) {
		hibernateTemplate.saveOrUpdate(unregistered);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Unregistered> getAllUnfinishedTransaction() {
		List<?> list = hibernateTemplate.find("from Unregistered where unregisteredStatus = ?", 
				ServerConsts.UNREGISTERED_STATUS_OF_PENDING);
		return (List<Unregistered>) list;
	}

	@Override
	public Unregistered getUnregisteredByTransId(String transId) {
		List<?> list = hibernateTemplate.find("from Unregistered where transferId = ?",transId);
		if(!list.isEmpty()){
			return (Unregistered) list.get(0);
		}
		return null;
	}
	
	@Override
	public List<Unregistered> getUnregisteredByPhoneAllStatus(String areaCode, String userPhone){
		List<?> list = hibernateTemplate.find(
				"from Unregistered where areaCode = ? and userPhone = ? ", areaCode,
				userPhone);
		return (List<Unregistered>) list;
	}
	
	
}
