package com.yuyutechnology.exchange.pojo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserDeviceId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3232379149518525140L;
	private Integer userId;
	private String deviceId;

	public UserDeviceId() {
	}

	public UserDeviceId(Integer userId, String deviceId) {
		super();
		this.userId = userId;
		this.deviceId = deviceId;
	}

	@Column(name = "user_id", nullable = false)
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "device_id", nullable = false)
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDeviceId other = (UserDeviceId) obj;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
