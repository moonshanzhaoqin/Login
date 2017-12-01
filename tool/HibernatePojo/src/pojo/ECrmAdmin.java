package pojo;
// Generated Dec 1, 2017 5:32:27 PM by Hibernate Tools 5.2.6.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ECrmAdmin generated by hbm2java
 */
@Entity
@Table(name = "e_crm_admin", catalog = "anytime_exchange")
public class ECrmAdmin implements java.io.Serializable {

	private Integer adminId;
	private String adminName;
	private String adminPassword;
	private String passwordSalt;
	private String adminPower;

	public ECrmAdmin() {
	}

	public ECrmAdmin(String adminName, String adminPassword, String passwordSalt) {
		this.adminName = adminName;
		this.adminPassword = adminPassword;
		this.passwordSalt = passwordSalt;
	}

	public ECrmAdmin(String adminName, String adminPassword, String passwordSalt, String adminPower) {
		this.adminName = adminName;
		this.adminPassword = adminPassword;
		this.passwordSalt = passwordSalt;
		this.adminPower = adminPower;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "admin_id", unique = true, nullable = false)
	public Integer getAdminId() {
		return this.adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	@Column(name = "admin_name", nullable = false)
	public String getAdminName() {
		return this.adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	@Column(name = "admin_password", nullable = false)
	public String getAdminPassword() {
		return this.adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	@Column(name = "password_salt", nullable = false)
	public String getPasswordSalt() {
		return this.passwordSalt;
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
