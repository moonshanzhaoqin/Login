package pojo;
// Generated Dec 13, 2017 10:49:46 AM by Hibernate Tools 4.0.0

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * EUserDevice generated by hbm2java
 */
@Entity
@Table(name = "e_user_device", catalog = "anytime_exchange")
public class EUserDevice implements java.io.Serializable {

	private EUserDeviceId id;
	private String deviceName;

	public EUserDevice() {
	}

	public EUserDevice(EUserDeviceId id, String deviceName) {
		this.id = id;
		this.deviceName = deviceName;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false)),
			@AttributeOverride(name = "deviceId", column = @Column(name = "device_id", nullable = false)) })
	public EUserDeviceId getId() {
		return this.id;
	}

	public void setId(EUserDeviceId id) {
		this.id = id;
	}

	@Column(name = "device_name", nullable = false)
	public String getDeviceName() {
		return this.deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

}
