package com.yuyutechnology.exchange.pojo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "e_crm_user_info")
public class CrmUserInfo {

	// private long infoId;
	private Integer userId;
	private String areaCode;
	private String userPhone;
	private String userName;
	private Date createTime;
	private Date loginTime;
	private int userType;
	private int userAvailable;
	private int loginAvailable;
	private int payAvailable;
	private BigDecimal userTotalAssets;
	private Date updateAt;

	public CrmUserInfo() {
		super();
	}

	public CrmUserInfo(Integer userId, String areaCode, String userPhone, String userName,Date createTime, int userType,
			int userAvailable, int loginAvailable, int payAvailable, BigDecimal userTotalAssets, Date updateAt) {
		super();
		// this.infoId = infoId;
		this.userId = userId;
		this.areaCode = areaCode;
		this.userPhone = userPhone;
		this.userName = userName;
		this.createTime = createTime;
		this.userType = userType;
		this.userAvailable = userAvailable;
		this.loginAvailable = loginAvailable;
		this.payAvailable = payAvailable;
		this.userTotalAssets = userTotalAssets;
		this.updateAt = updateAt;
	}

	public CrmUserInfo(User user) {
		super();
		this.userId = user.getUserId();
		this.areaCode = user.getAreaCode();
		this.userPhone = user.getUserPhone();
		this.userName = user.getUserName();
		this.createTime = user.getCreateTime();
		this.userType = user.getUserType();
		this.userAvailable = user.getUserAvailable();
		this.loginAvailable = user.getLoginAvailable();
		this.payAvailable = user.getPayAvailable();
	}

	@Id
	@Column(name = "user_id")
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "area_code")
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Column(name = "user_phone")
	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	@Column(name = "user_name")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "login_time")
	public Date getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	@Column(name = "user_type")
	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	@Column(name = "user_available")
	public int getUserAvailable() {
		return userAvailable;
	}

	public void setUserAvailable(int userAvailable) {
		this.userAvailable = userAvailable;
	}

	@Column(name = "login_available", nullable = false)
	public int getLoginAvailable() {
		return loginAvailable;
	}

	public void setLoginAvailable(int loginAvailable) {
		this.loginAvailable = loginAvailable;
	}

	@Column(name = "pay_available", nullable = false)
	public int getPayAvailable() {
		return payAvailable;
	}

	public void setPayAvailable(int payAvailable) {
		this.payAvailable = payAvailable;
	}

	@Column(name = "user_total_assets")
	public BigDecimal getUserTotalAssets() {
		return userTotalAssets;
	}

	public void setUserTotalAssets(BigDecimal userTotalAssets) {
		this.userTotalAssets = userTotalAssets;
	}

	@Column(name = "update_at")
	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
