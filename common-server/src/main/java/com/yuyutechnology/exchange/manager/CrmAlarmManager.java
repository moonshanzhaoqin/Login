package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import com.yuyutechnology.exchange.pojo.CrmAlarm;
import com.yuyutechnology.exchange.pojo.CrmSupervisor;

public interface CrmAlarmManager {

	public void addAlarmConfig(int alarmTypeString, BigDecimal lowerLimit, BigDecimal upperLimit, int alarmMode,
			int editorid, String adminIdArr);

	public void updateAlarmConfig(Integer alarmId, int alarmType, BigDecimal lowerLimit, BigDecimal upperLimit,
			int alarmMode, int editorid, String adminIdArr);

	public int updateAlarmAvailable(Integer alarmId, int alarmAvailable);

	public int delAlarmConfig(int alarmId);

	public CrmAlarm getAlarmConfigById(int alarmId);

	public List<CrmAlarm> getCrmAlarmConfigList();

	public List<CrmSupervisor> getCrmSupervisorList();

	public void delSupervisorById(Integer supervisorId);

	public String saveSupervisor(String supervisorName, String supervisorMobile, String supervisorEmail);

//	public void autoAlarm(BigDecimal userHoldingTotalAssets);

//	public HashMap<String, BigDecimal> getAccountInfo(BigDecimal userHoldingTotalAssets);

	public HashMap<String, BigDecimal> getLargeTransLimit();

	/**
	 * 
	 * @param supervisorIdArr
	 * @param features
	 * @param earlyWarningMode
	 * @param params
	 */
	public void alarmNotice(String supervisorIdArr, String features, int earlyWarningMode,
			HashMap<String, Object> params);

	void reachtotalGDQLimitAlarm(BigDecimal totalGDQCanBeSold, BigDecimal percent);

	/**
	 * 
	 */
	void registrationAlarm();

}
