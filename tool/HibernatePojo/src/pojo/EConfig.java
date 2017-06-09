package pojo;
// Generated Jun 5, 2017 10:04:57 AM by Hibernate Tools 5.2.1.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EConfig generated by hbm2java
 */
@Entity
@Table(name = "e_config", catalog = "anytime_exchange")
public class EConfig implements java.io.Serializable {

	private String configKey;
	private String configValue;
	private String configName;
	private int configOrder;
	private int configCanChange;

	public EConfig() {
	}

	public EConfig(String configKey, int configOrder, int configCanChange) {
		this.configKey = configKey;
		this.configOrder = configOrder;
		this.configCanChange = configCanChange;
	}

	public EConfig(String configKey, String configValue, String configName, int configOrder, int configCanChange) {
		this.configKey = configKey;
		this.configValue = configValue;
		this.configName = configName;
		this.configOrder = configOrder;
		this.configCanChange = configCanChange;
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

	@Column(name = "config_name")
	public String getConfigName() {
		return this.configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	@Column(name = "config_order", nullable = false)
	public int getConfigOrder() {
		return this.configOrder;
	}

	public void setConfigOrder(int configOrder) {
		this.configOrder = configOrder;
	}

	@Column(name = "config_canChange", nullable = false)
	public int getConfigCanChange() {
		return this.configCanChange;
	}

	public void setConfigCanChange(int configCanChange) {
		this.configCanChange = configCanChange;
	}

}
