package com.yuyutechnology.exchange.pojo;
// Generated Dec 2, 2016 4:27:04 PM by Hibernate Tools 4.0.0

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
 * User generated by hbm2java
 */
@Entity
@Table(name = "user", catalog = "anytime_exchange")
public class User implements java.io.Serializable {

	private Integer userId;
	private String userPhone;
	private String userName;
	private String userPassword;
	private String userPayPwd;
	private String createTime;
	private Date loginTime;
	private String loginIp;
	private int userType;

	public User() {
	}

	public User(String userPhone, String createTime, int userType) {
		this.userPhone = userPhone;
		this.createTime = createTime;
		this.userType = userType;
	}

	public User(String userPhone, String userName, String userPassword, String userPayPwd, String createTime,
			Date loginTime, String loginIp, int userType) {
		this.userPhone = userPhone;
		this.userName = userName;
		this.userPassword = userPassword;
		this.userPayPwd = userPayPwd;
		this.createTime = createTime;
		this.loginTime = loginTime;
		this.loginIp = loginIp;
		this.userType = userType;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "user_id", unique = true, nullable = false)
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "user_phone", nullable = false)
	public String getUserPhone() {
		return this.userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	@Column(name = "user_name")
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "user_password")
	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	@Column(name = "user_pay_pwd")
	public String getUserPayPwd() {
		return this.userPayPwd;
	}

	public void setUserPayPwd(String userPayPwd) {
		this.userPayPwd = userPayPwd;
	}

	@Column(name = "create_time", nullable = false)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "login_time", length = 19)
	public Date getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	@Column(name = "login_ip")
	public String getLoginIp() {
		return this.loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@Column(name = "user_type", nullable = false)
	public int getUserType() {
		return this.userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

}
