package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.List;

import com.yuyutechnology.exchange.pojo.CrmAlarm;
import com.yuyutechnology.exchange.pojo.CrmSupervisor;

public interface CrmAlarmManager {
	
	public void addAlarmConfig(String alarmGrade,BigDecimal lowerLimit,
			BigDecimal upperLimit,int alarmMode,int editorid,String adminIdArr);
	
	public void delAlarmConfig(int alarmId);
	
	public void autoAlarm(BigDecimal Difference);
	
	public List<CrmAlarm> getCrmAlarmConfigList();
	
	public List<CrmSupervisor> getCrmSupervisorList();
	

}