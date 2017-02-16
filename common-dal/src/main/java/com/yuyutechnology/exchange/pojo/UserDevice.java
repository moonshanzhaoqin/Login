package com.yuyutechnology.exchange.pojo;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "e_user_device")
public class UserDevice implements java.io.Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = -6654159191959410119L;
	private UserDeviceId id;
	private String deviceName;

	public UserDevice() {
	}

	public UserDevice(UserDeviceId id, String deviceName) {
		super();
		this.id = id;
		this.deviceName = deviceName;
	}

	@EmbeddedId
	@AttributeOverrides({ @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false)),
			@AttributeOverride(name = "deviceId", column = @Column(name = "device_id", nullable = false)) })
	public UserDeviceId getId() {
		return id;
	}

	public void setId(UserDeviceId id) {
		this.id = id;
	}

	@Column(name = "device_name", nullable = false)
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

}
