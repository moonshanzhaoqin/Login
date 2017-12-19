package pojo;
// Generated Dec 19, 2017 12:22:01 PM by Hibernate Tools 5.2.6.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * EWalletNowId generated by hbm2java
 */
@Embeddable
public class EWalletNowId implements java.io.Serializable {

	private int userId;
	private String currency;

	public EWalletNowId() {
	}

	public EWalletNowId(int userId, String currency) {
		this.userId = userId;
		this.currency = currency;
	}

	@Column(name = "user_id", nullable = false)
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Column(name = "currency", nullable = false, length = 3)
	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EWalletNowId))
			return false;
		EWalletNowId castOther = (EWalletNowId) other;

		return (this.getUserId() == castOther.getUserId())
				&& ((this.getCurrency() == castOther.getCurrency()) || (this.getCurrency() != null
						&& castOther.getCurrency() != null && this.getCurrency().equals(castOther.getCurrency())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getUserId();
		result = 37 * result + (getCurrency() == null ? 0 : this.getCurrency().hashCode());
		return result;
	}

}
