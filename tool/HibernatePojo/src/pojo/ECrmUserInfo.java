package pojo;
// Generated Dec 1, 2017 5:32:27 PM by Hibernate Tools 5.2.6.Final

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ECrmUserInfo generated by hbm2java
 */
@Entity
@Table(name = "e_crm_user_info", catalog = "anytime_exchange")
public class ECrmUserInfo implements java.io.Serializable {

	private long userId;
	private String areaCode;
	private String userPhone;
	private String userName;
	private Date createTime;
	private Date loginTime;
	private Integer userType;
	private Integer userAvailable;
	private int loginAvailable;
	private int payAvailable;
	private BigDecimal userTotalAssets;
	private Date updateAt;

	public ECrmUserInfo() {
	}

	public ECrmUserInfo(long userId, int loginAvailable, int payAvailable) {
		this.userId = userId;
		this.loginAvailable = loginAvailable;
		this.payAvailable = payAvailable;
	}

	public ECrmUserInfo(long userId, String areaCode, String userPhone, String userName, Date createTime,
			Date loginTime, Integer userType, Integer userAvailable, int loginAvailable, int payAvailable,
			BigDecimal userTotalAssets, Date updateAt) {
		this.userId = userId;
		this.areaCode = areaCode;
		this.userPhone = userPhone;
		this.userName = userName;
		this.createTime = createTime;
		this.loginTime = loginTime;
		this.userType = userType;
		this.userAvailable = userAvailable;
		this.loginAvailable = loginAvailable;
		this.payAvailable = payAvailable;
		this.userTotalAssets = userTotalAssets;
		this.updateAt = updateAt;
	}

	@Id

	@Column(name = "user_id", unique = true, nullable = false)
	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Column(name = "area_code", length = 10)
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Column(name = "user_phone", length = 30)
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
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

	@Column(name = "user_type")
	public Integer getUserType() {
		return this.userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	@Column(name = "user_available")
	public Integer getUserAvailable() {
		return this.userAvailable;
	}

	public void setUserAvailable(Integer userAvailable) {
		this.userAvailable = userAvailable;
	}

	@Column(name = "login_available", nullable = false)
	public int getLoginAvailable() {
		return this.loginAvailable;
	}

	public void setLoginAvailable(int loginAvailable) {
		this.loginAvailable = loginAvailable;
	}

	@Column(name = "pay_available", nullable = false)
	public int getPayAvailable() {
		return this.payAvailable;
	}

	public void setPayAvailable(int payAvailable) {
		this.payAvailable = payAvailable;
	}

	@Column(name = "user_total_assets", precision = 20, scale = 4)
	public BigDecimal getUserTotalAssets() {
		return this.userTotalAssets;
	}

	public void setUserTotalAssets(BigDecimal userTotalAssets) {
		this.userTotalAssets = userTotalAssets;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_at", length = 19)
	public Date getUpdateAt() {
		return this.updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
