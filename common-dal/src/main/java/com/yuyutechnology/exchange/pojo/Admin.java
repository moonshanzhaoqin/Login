package com.yuyutechnology.exchange.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "e_crm_admin")
public class Admin {
	private Integer adminId;
	private String adminName;
	private String adminPassword;
	private String passwordSalt;
	private String adminPower;

	public Admin() {

	}

	public Admin(String adminName, String adminPassword, String passwordSalt) {
		super();
		this.adminName = adminName;
		this.adminPassword = adminPassword;
		this.passwordSalt = passwordSalt;
	}

	public Admin(String adminName, String adminPassword, String passwordSalt, String adminPower) {
		this.adminName = adminName;
		this.adminPassword = adminPassword;
		this.passwordSalt = passwordSalt;
		this.adminPower = adminPower;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "admin_id", unique = true, nullable = false)
	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	@Column(name = "admin_name", nullable = false)
	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	@Column(name = "admin_password", nullable = false)
	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	@Column(name = "password_salt", nullable = false)
	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	@Column(name = "admin_power")
	public String getAdminPower() {
		return this.adminPower;
	}

	public void setAdminPower(String adminPower) {
		this.adminPower = adminPower;
	}

}
