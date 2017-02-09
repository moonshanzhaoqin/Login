package com.yuyutechnology.exchange.crm.request;

import java.math.BigDecimal;

public class UpdateAlarmConfigInfoRequest {

	private Integer alarmId;
	private int alarmType;
	private BigDecimal criticalThresholdLowerLimit;
	private BigDecimal criticalThresholdUpperLimit;
	private int alarmMode;
	private int[] supervisorId;
	
	public Integer getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(Integer alarmId) {
		this.alarmId = alarmId;
	}

	public int getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(int alarmType) {
		this.alarmType = alarmType;
	}
	public BigDecimal getCriticalThresholdLowerLimit() {
		return criticalThresholdLowerLimit;
	}
	public void setCriticalThresholdLowerLimit(BigDecimal criticalThresholdLowerLimit) {
		this.criticalThresholdLowerLimit = criticalThresholdLowerLimit;
	}
	public BigDecimal getCriticalThresholdUpperLimit() {
		return criticalThresholdUpperLimit;
	}
	public void setCriticalThresholdUpperLimit(BigDecimal criticalThresholdUpperLimit) {
		this.criticalThresholdUpperLimit = criticalThresholdUpperLimit;
	}
	public int getAlarmMode() {
		return alarmMode;
	}
	public void setAlarmMode(int alarmMode) {
		this.alarmMode = alarmMode;
	}
	public int[] getSupervisorId() {
		return supervisorId;
	}
	public void setSupervisorId(int[] supervisorId) {
		this.supervisorId = supervisorId;
	}
	
	
	
	
}
