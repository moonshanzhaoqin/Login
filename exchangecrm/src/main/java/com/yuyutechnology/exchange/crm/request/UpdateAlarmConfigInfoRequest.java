package com.yuyutechnology.exchange.crm.request;

import java.math.BigDecimal;

public class UpdateAlarmConfigInfoRequest {

	private Integer alarmId;
	private String alarmGrade;
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
	public String getAlarmGrade() {
		return alarmGrade;
	}
	public void setAlarmGrade(String alarmGrade) {
		this.alarmGrade = alarmGrade;
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
