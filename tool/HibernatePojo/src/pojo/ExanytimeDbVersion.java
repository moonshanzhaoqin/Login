package pojo;
// Generated Dec 19, 2017 12:22:01 PM by Hibernate Tools 5.2.6.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ExanytimeDbVersion generated by hbm2java
 */
@Entity
@Table(name = "exanytime_dbVersion", catalog = "anytime_exchange")
public class ExanytimeDbVersion implements java.io.Serializable {

	private String dbVersion;
	private Date updateTime;

	public ExanytimeDbVersion() {
	}

	public ExanytimeDbVersion(String dbVersion) {
		this.dbVersion = dbVersion;
	}

	public ExanytimeDbVersion(String dbVersion, Date updateTime) {
		this.dbVersion = dbVersion;
		this.updateTime = updateTime;
	}

	@Id

	@Column(name = "dbVersion", unique = true, nullable = false, length = 50)
	public String getDbVersion() {
		return this.dbVersion;
	}

	public void setDbVersion(String dbVersion) {
		this.dbVersion = dbVersion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updateTime", length = 19)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}