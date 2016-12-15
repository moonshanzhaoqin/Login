package pojo;
// Generated Dec 14, 2016 6:26:12 PM by Hibernate Tools 4.0.0

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
	private String areaCode;
	private String userPhone;
	private String userName;
	private String userPassword;
	private String userPayPwd;
	private Date createTime;
	private Date loginTime;
	private String loginIp;
	private int userType;
	private int userAvailable;
	private String passwordSalt;
	private Set<Friend> friends = new HashSet<Friend>(0);

	public User() {
	}

	public User(String areaCode, String userPhone, Date createTime, int userType, int userAvailable) {
		this.areaCode = areaCode;
		this.userPhone = userPhone;
		this.createTime = createTime;
		this.userType = userType;
		this.userAvailable = userAvailable;
	}

	public User(String areaCode, String userPhone, String userName, String userPassword, String userPayPwd,
			Date createTime, Date loginTime, String loginIp, int userType, int userAvailable, String passwordSalt,
			Set<Friend> friends) {
		this.areaCode = areaCode;
		this.userPhone = userPhone;
		this.userName = userName;
		this.userPassword = userPassword;
		this.userPayPwd = userPayPwd;
		this.createTime = createTime;
		this.loginTime = loginTime;
		this.loginIp = loginIp;
		this.userType = userType;
		this.userAvailable = userAvailable;
		this.passwordSalt = passwordSalt;
		this.friends = friends;
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

	@Column(name = "area_code", nullable = false, length = 5)
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", nullable = false, length = 19)
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

	@Column(name = "user_available", nullable = false)
	public int getUserAvailable() {
		return this.userAvailable;
	}

	public void setUserAvailable(int userAvailable) {
		this.userAvailable = userAvailable;
	}

	@Column(name = "password_salt")
	public String getPasswordSalt() {
		return this.passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<Friend> getFriends() {
		return this.friends;
	}

	public void setFriends(Set<Friend> friends) {
		this.friends = friends;
	}

}
