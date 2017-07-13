package com.yuyutechnology.exchange.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "e_crm_supervisor")
public class CrmSupervisor {

	private Integer supervisorId;
	private String supervisorName;
	private String supervisorMobile;
	private String supervisorEmail;
	private Date updateAt;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "supervisor_id", unique = true, nullable = false)
	public Integer getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(Integer supervisorId) {
		this.supervisorId = supervisorId;
	}

	@Column(name = "supervisor_name")
	public String getSupervisorName() {
		return supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}

	@Column(name = "supervisor_mobile")
	public String getSupervisorMobile() {
		return supervisorMobile;
	}

	public void setSupervisorMobile(String supervisorMobile) {
		this.supervisorMobile = supervisorMobile;
	}

	@Column(name = "supervisor_email")
	public String getSupervisorEmail() {
		return supervisorEmail;
	}

	public void setSupervisorEmail(String supervisorEmail) {
		this.supervisorEmail = supervisorEmail;
	}

	@Column(name = "update_at")
	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
}
