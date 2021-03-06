package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.CrmAlarmDAO;
import com.yuyutechnology.exchange.pojo.CrmAlarm;

@Repository
public class CrmAlarmDAOImpl implements CrmAlarmDAO {
	
	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public void addCrmAlarmConfig(CrmAlarm crmAlarm) {
		hibernateTemplate.saveOrUpdate(crmAlarm);
	}

	@Override
	public int delCrmAlarmConfig(int alarmId) {
		CrmAlarm crmAlarm = hibernateTemplate.get(CrmAlarm.class, alarmId);
		hibernateTemplate.delete(crmAlarm);
		return crmAlarm.getAlarmType();
	}
	

	@Override
	public CrmAlarm getCrmAlarmConfig(int alarmId) {
		CrmAlarm crmAlarm = hibernateTemplate.get(CrmAlarm.class, alarmId);
		return crmAlarm;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrmAlarm> getCrmAlarmConfigList() {
		List<?> list = hibernateTemplate.find("from CrmAlarm");
		if(list.isEmpty()){
			return null;
		}
		return (List<CrmAlarm>) list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CrmAlarm> getConfigListByTypeAndStatus(int alarmType,int alarmAvailable){
		List<?> list = hibernateTemplate.find("from CrmAlarm where alarmType = ? and alarmAvailable = ?",alarmType,alarmAvailable);
		if(list.isEmpty()){
			return null;
		}
		return (List<CrmAlarm>) list;
	}

}
