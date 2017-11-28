package pojo;
// Generated Nov 28, 2017 4:13:30 PM by Hibernate Tools 5.2.6.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * EUserDeviceId generated by hbm2java
 */
@Embeddable
public class EUserDeviceId implements java.io.Serializable {

	private int userId;
	private String deviceId;

	public EUserDeviceId() {
	}

	public EUserDeviceId(int userId, String deviceId) {
		this.userId = userId;
		this.deviceId = deviceId;
	}

	@Column(name = "user_id", nullable = false)
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Column(name = "device_id", nullable = false)
	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EUserDeviceId))
			return false;
		EUserDeviceId castOther = (EUserDeviceId) other;

		return (this.getUserId() == castOther.getUserId())
				&& ((this.getDeviceId() == castOther.getDeviceId()) || (this.getDeviceId() != null
						&& castOther.getDeviceId() != null && this.getDeviceId().equals(castOther.getDeviceId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getUserId();
		result = 37 * result + (getDeviceId() == null ? 0 : this.getDeviceId().hashCode());
		return result;
	}

}
