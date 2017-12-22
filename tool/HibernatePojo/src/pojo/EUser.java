package pojo;
// Generated Dec 19, 2017 12:22:01 PM by Hibernate Tools 5.2.6.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * EUser generated by hbm2java
 */
@Entity
@Table(name = "e_user", catalog = "anytime_exchange", uniqueConstraints = @UniqueConstraint(columnNames = { "area_code",
		"user_phone" }))
public class EUser implements java.io.Serializable {

	private Integer userId;
	private String areaCode;
	private String userPhone;
	private String userName;
	private String namePinyin;
	private String userPortrait;
	private String userPassword;
	private String userPayPwd;
	private String userPayToken;
	private Date createTime;
	private Date loginTime;
	private String loginIp;
	private int userType;
	private int userAvailable;
	private int loginAvailable;
	private int payAvailable;
	private String passwordSalt;
	private String pushId;
	private String pushTag;

	public EUser() {
	}

	public EUser(String areaCode, String userPhone, String userName, String userPassword, Date createTime, int userType,
			int userAvailable, int loginAvailable, int payAvailable, String passwordSalt, String pushTag) {
		this.areaCode = areaCode;
		this.userPhone = userPhone;
		this.userName = userName;
		this.userPassword = userPassword;
		this.createTime = createTime;
		this.userType = userType;
		this.userAvailable = userAvailable;
		this.loginAvailable = loginAvailable;
		this.payAvailable = payAvailable;
		this.passwordSalt = passwordSalt;
		this.pushTag = pushTag;
	}

	public EUser(String areaCode, String userPhone, String userName, String namePinyin, String userPortrait,
			String userPassword, String userPayPwd, String userPayToken, Date createTime, Date loginTime,
			String loginIp, int userType, int userAvailable, int loginAvailable, int payAvailable, String passwordSalt,
			String pushId, String pushTag) {
		this.areaCode = areaCode;
		this.userPhone = userPhone;
		this.userName = userName;
		this.namePinyin = namePinyin;
		this.userPortrait = userPortrait;
		this.userPassword = userPassword;
		this.userPayPwd = userPayPwd;
		this.userPayToken = userPayToken;
		this.createTime = createTime;
		this.loginTime = loginTime;
		this.loginIp = loginIp;
		this.userType = userType;
		this.userAvailable = userAvailable;
		this.loginAvailable = loginAvailable;
		this.payAvailable = payAvailable;
		this.passwordSalt = passwordSalt;
		this.pushId = pushId;
		this.pushTag = pushTag;
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

	@Column(name = "user_name", nullable = false)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "name_pinyin")
	public String getNamePinyin() {
		return this.namePinyin;
	}

	public void setNamePinyin(String namePinyin) {
		this.namePinyin = namePinyin;
	}

	@Column(name = "user_portrait")
	public String getUserPortrait() {
		return this.userPortrait;
	}

	public void setUserPortrait(String userPortrait) {
		this.userPortrait = userPortrait;
	}

	@Column(name = "user_password", nullable = false)
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

	@Column(name = "user_pay_token", length = 128)
	public String getUserPayToken() {
		return this.userPayToken;
	}

	public void setUserPayToken(String userPayToken) {
		this.userPayToken = userPayToken;
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

	@Column(name = "password_salt", nullable = false)
	public String getPasswordSalt() {
		return this.passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	@Column(name = "push_id")
	public String getPushId() {
		return this.pushId;
	}

	public void setPushId(String pushId) {
		this.pushId = pushId;
	}

	@Column(name = "push_tag", nullable = false)
	public String getPushTag() {
		return this.pushTag;
	}

	public void setPushTag(String pushTag) {
		this.pushTag = pushTag;
	}

}
