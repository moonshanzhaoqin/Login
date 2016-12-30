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
	
//	private long infoId;
	private Integer userId;
	private String areaCode;
	private String userPhone;
	private String userName;
	private int userType;
	private int userAvailable;
	private BigDecimal userTotalAssets;
	private Date updateAt;
	
	public CrmUserInfo() {
		super();
	}
	
	public CrmUserInfo(Integer userId, String areaCode, String userPhone, String userName, int userType,
			int userAvailable, BigDecimal userTotalAssets,Date updateAt) {
		super();
//		this.infoId = infoId;
		this.userId = userId;
		this.areaCode = areaCode;
		this.userPhone = userPhone;
		this.userName = userName;
		this.userType = userType;
		this.userAvailable = userAvailable;
		this.userTotalAssets = userTotalAssets;
		this.updateAt = updateAt;
	}
	
	public CrmUserInfo(User user) {
		super();
		this.userId = user.getUserId();
		this.areaCode = user.getAreaCode();
		this.userPhone = user.getUserPhone();
		this.userName = user.getUserName();
		this.userType = user.getUserType();
		this.userAvailable = user.getUserAvailable();
	}
	
	@Id
//	@Column(name = "info_id", unique = true, nullable = false)
//	public long getInfoId() {
//		return infoId;
//	}
//	public void setInfoId(long infoId) {
//		this.infoId = infoId;
//	}
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
