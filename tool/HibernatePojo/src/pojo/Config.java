package pojo;
// Generated Dec 13, 2016 10:47:38 AM by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Config generated by hbm2java
 */
@Entity
@Table(name = "config", catalog = "anytime_exchange")
public class Config implements java.io.Serializable {

	private String configKey;
	private String configValue;

	public Config() {
	}

	public Config(String configKey) {
		this.configKey = configKey;
	}

	public Config(String configKey, String configValue) {
		this.configKey = configKey;
		this.configValue = configValue;
	}

	@Id

	@Column(name = "config_key", unique = true, nullable = false)
	public String getConfigKey() {
		return this.configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	@Column(name = "config_value")
	public String getConfigValue() {
		return this.configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

}
