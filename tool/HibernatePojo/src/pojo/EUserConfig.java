package pojo;
// Generated Nov 28, 2017 4:13:30 PM by Hibernate Tools 5.2.6.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EUserConfig generated by hbm2java
 */
@Entity
@Table(name = "e_user_config", catalog = "anytime_exchange")
public class EUserConfig implements java.io.Serializable {

	private int userId;
	private String userConfigValue;

	public EUserConfig() {
	}

	public EUserConfig(int userId) {
		this.userId = userId;
	}

	public EUserConfig(int userId, String userConfigValue) {
		this.userId = userId;
		this.userConfigValue = userConfigValue;
	}

	@Id

	@Column(name = "user_id", unique = true, nullable = false)
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Column(name = "user_config_value", length = 65535)
	public String getUserConfigValue() {
		return this.userConfigValue;
	}

	public void setUserConfigValue(String userConfigValue) {
		this.userConfigValue = userConfigValue;
	}

}
