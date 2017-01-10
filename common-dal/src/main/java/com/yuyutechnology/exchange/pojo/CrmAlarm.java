package com.yuyutechnology.exchange.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "e_crm_alarm")
public class CrmAlarm {
	
	private Integer alarmId;
	private int alarmType;
	private String alarmGrade;
	private BigDecimal lowerLimit;
	private BigDecimal upperLimit;
	private int alarmMode;
	private int alarmAvailable;
	private Date createAt;
	private int editorid;
	private String supervisorIdArr;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "alarm_id", unique = true, nullable = false)
	public Integer getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(Integer alarmId) {
		this.alarmId = alarmId;
	}
	@Column(name = "alarm_type")
	public int getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(int alarmType) {
		this.alarmType = alarmType;
	}
	@Column(name = "alarm_grade")
	public String getAlarmGrade() {
		return alarmGrade;
	}
	public void setAlarmGrade(String alarmGrade) {
		this.alarmGrade = alarmGrade;
	}
	@Column(name = "lower_limit")
	public BigDecimal getLowerLimit() {
		return lowerLimit;
	}
	public void setLowerLimit(BigDecimal lowerLimit) {
		this.lowerLimit = lowerLimit;
	}
	@Column(name = "upper_limit")
	public BigDecimal getUpperLimit() {
		return upperLimit;
	}
	public void setUpperLimit(BigDecimal upperLimit) {
		this.upperLimit = upperLimit;
	}
	@Column(name = "alarm_mode")
	public int getAlarmMode() {
		return alarmMode;
	}
	public void setAlarmMode(int alarmMode) {
		this.alarmMode = alarmMode;
	}
	@Column(name = "alarm_available")
	public int getAlarmAvailable() {
		return alarmAvailable;
	}
	public void setAlarmAvailable(int alarmAvailable) {
		this.alarmAvailable = alarmAvailable;
	}
	@Column(name = "create_at")
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	@Column(name = "editor_id")
	public int getEditorid() {
		return editorid;
	}
	public void setEditorid(int editorid) {
		this.editorid = editorid;
	}
	@Column(name = "supervisor_id_arr")
	public String getSupervisorIdArr() {
		return supervisorIdArr;
	}
	public void setSupervisorIdArr(String supervisorIdArr) {
		this.supervisorIdArr = supervisorIdArr;
	}
}
