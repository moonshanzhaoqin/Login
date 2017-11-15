package com.yuyutechnology.exchange.crm.request;

import java.math.BigDecimal;
import java.util.Arrays;

public class SaveAlarmConfigRequest {

	// private String alarmGrade;
	private int alarmType;
	private BigDecimal criticalThresholdLowerLimit;
	private BigDecimal criticalThresholdUpperLimit;
	private int alarmMode;
	private int[] supervisorId;

	// public String getAlarmGrade() {
	// return alarmGrade;
	// }
	// public void setAlarmGrade(String alarmGrade) {
	// this.alarmGrade = alarmGrade;
	// }

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

	@Override
	public String toString() {
		return "SaveAlarmConfigRequest [alarmType=" + alarmType + ", criticalThresholdLowerLimit="
				+ criticalThresholdLowerLimit + ", criticalThresholdUpperLimit=" + criticalThresholdUpperLimit
				+ ", alarmMode=" + alarmMode + ", supervisorId=" + Arrays.toString(supervisorId) + "]";
	}

}
