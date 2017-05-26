package com.yuyutechnology.exchange.pojo;
// Generated May 25, 2017 2:13:14 PM by Hibernate Tools 5.2.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ECrmLog generated by hbm2java
 */
@Entity
@Table(name = "e_crm_log")
public class CrmLog implements java.io.Serializable {

	private Integer logId;
	private String adminName;
	private Date operateTime;
	private String operation;
	private String target;

	public CrmLog() {
	}

	public CrmLog(String adminName, Date operateTime, String operation) {
		this.adminName = adminName;
		this.operateTime = operateTime;
		this.operation = operation;
	}

	public CrmLog(String adminName, Date operateTime, String operation, String target) {
		this.adminName = adminName;
		this.operateTime = operateTime;
		this.operation = operation;
		this.target = target;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "log_id", unique = true, nullable = false)
	public Integer getLogId() {
		return this.logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	@Column(name = "admin_name", nullable = false)
	public String getAdminName() {
		return this.adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "operate_time", nullable = false, length = 19)
	public Date getOperateTime() {
		return this.operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	@Column(name = "operation", nullable = false)
	public String getOperation() {
		return this.operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Column(name = "target")
	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
