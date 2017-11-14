package pojo;
// Generated Nov 14, 2017 2:36:25 PM by Hibernate Tools 5.2.6.Final

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
 * ECrmSupervisor generated by hbm2java
 */
@Entity
@Table(name = "e_crm_supervisor", catalog = "anytime_exchange")
public class ECrmSupervisor implements java.io.Serializable {

	private Long supervisorId;
	private String supervisorName;
	private String supervisorMobile;
	private String supervisorEmail;
	private Date updateAt;

	public ECrmSupervisor() {
	}

	public ECrmSupervisor(String supervisorName, String supervisorMobile, String supervisorEmail, Date updateAt) {
		this.supervisorName = supervisorName;
		this.supervisorMobile = supervisorMobile;
		this.supervisorEmail = supervisorEmail;
		this.updateAt = updateAt;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "supervisor_id", unique = true, nullable = false)
	public Long getSupervisorId() {
		return this.supervisorId;
	}

	public void setSupervisorId(Long supervisorId) {
		this.supervisorId = supervisorId;
	}

	@Column(name = "supervisor_name")
	public String getSupervisorName() {
		return this.supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}

	@Column(name = "supervisor_mobile")
	public String getSupervisorMobile() {
		return this.supervisorMobile;
	}

	public void setSupervisorMobile(String supervisorMobile) {
		this.supervisorMobile = supervisorMobile;
	}

	@Column(name = "supervisor_email")
	public String getSupervisorEmail() {
		return this.supervisorEmail;
	}

	public void setSupervisorEmail(String supervisorEmail) {
		this.supervisorEmail = supervisorEmail;
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
