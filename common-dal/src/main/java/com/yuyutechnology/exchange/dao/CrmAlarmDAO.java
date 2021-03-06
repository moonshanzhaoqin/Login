package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.CrmAlarm;

public interface CrmAlarmDAO {
	
	public void addCrmAlarmConfig(CrmAlarm crmAlarm);
	
	public int delCrmAlarmConfig(int alarmId);
	
	public CrmAlarm getCrmAlarmConfig(int alarmId);
	
	public List<CrmAlarm> getCrmAlarmConfigList();

	public List<CrmAlarm> getConfigListByTypeAndStatus(int alarmType,int alarmAvailable);

}
