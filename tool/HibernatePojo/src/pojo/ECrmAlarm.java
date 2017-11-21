package pojo;
// Generated Nov 20, 2017 6:14:04 PM by Hibernate Tools 5.2.6.Final

import java.math.BigDecimal;
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
 * ECrmAlarm generated by hbm2java
 */
@Entity
@Table(name = "e_crm_alarm", catalog = "anytime_exchange")
public class ECrmAlarm implements java.io.Serializable {

	private Long alarmId;
	private Integer alarmType;
	private String alarmGrade;
	private BigDecimal lowerLimit;
	private BigDecimal upperLimit;
	private Integer alarmMode;
	private String supervisorIdArr;
	private Integer alarmAvailable;
	private Date createAt;
	private Integer editorId;

	public ECrmAlarm() {
	}

	public ECrmAlarm(Integer alarmType, String alarmGrade, BigDecimal lowerLimit, BigDecimal upperLimit,
			Integer alarmMode, String supervisorIdArr, Integer alarmAvailable, Date createAt, Integer editorId) {
		this.alarmType = alarmType;
		this.alarmGrade = alarmGrade;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.alarmMode = alarmMode;
		this.supervisorIdArr = supervisorIdArr;
		this.alarmAvailable = alarmAvailable;
		this.createAt = createAt;
		this.editorId = editorId;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "alarm_id", unique = true, nullable = false)
	public Long getAlarmId() {
		return this.alarmId;
	}

	public void setAlarmId(Long alarmId) {
		this.alarmId = alarmId;
	}

	@Column(name = "alarm_type")
	public Integer getAlarmType() {
		return this.alarmType;
	}

	public void setAlarmType(Integer alarmType) {
		this.alarmType = alarmType;
	}

	@Column(name = "alarm_grade")
	public String getAlarmGrade() {
		return this.alarmGrade;
	}

	public void setAlarmGrade(String alarmGrade) {
		this.alarmGrade = alarmGrade;
	}

	@Column(name = "lower_limit", precision = 20, scale = 4)
	public BigDecimal getLowerLimit() {
		return this.lowerLimit;
	}

	public void setLowerLimit(BigDecimal lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	@Column(name = "upper_limit", precision = 20, scale = 4)
	public BigDecimal getUpperLimit() {
		return this.upperLimit;
	}

	public void setUpperLimit(BigDecimal upperLimit) {
		this.upperLimit = upperLimit;
	}

	@Column(name = "alarm_mode")
	public Integer getAlarmMode() {
		return this.alarmMode;
	}

	public void setAlarmMode(Integer alarmMode) {
		this.alarmMode = alarmMode;
	}

	@Column(name = "supervisor_id_arr")
	public String getSupervisorIdArr() {
		return this.supervisorIdArr;
	}

	public void setSupervisorIdArr(String supervisorIdArr) {
		this.supervisorIdArr = supervisorIdArr;
	}

	@Column(name = "alarm_available")
	public Integer getAlarmAvailable() {
		return this.alarmAvailable;
	}

	public void setAlarmAvailable(Integer alarmAvailable) {
		this.alarmAvailable = alarmAvailable;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_at", length = 19)
	public Date getCreateAt() {
		return this.createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	@Column(name = "editor_id")
	public Integer getEditorId() {
		return this.editorId;
	}

	public void setEditorId(Integer editorId) {
		this.editorId = editorId;
	}

}
