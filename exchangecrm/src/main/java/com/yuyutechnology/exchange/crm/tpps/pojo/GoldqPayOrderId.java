package com.yuyutechnology.exchange.crm.tpps.pojo;
import javax.persistence.Column;
import javax.persistence.Embeddable;
/**
 * GoldqPayOrderId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class GoldqPayOrderId implements java.io.Serializable
{
	// Fields
	private String orderId;
	private String clientId;
	// Constructors
	/** default constructor */
	public GoldqPayOrderId()
	{
	}
	/** full constructor */
	public GoldqPayOrderId(String clientId,String orderId)
	{
		this.orderId = orderId;
		this.clientId = clientId;
	}
	// Property accessors
	@Column(name = "order_id", nullable = false, length = 32)
	public String getOrderId()
	{
		return this.orderId;
	}
	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}
	@Column(name = "client_id", nullable = false, length = 32)
	public String getClientId()
	{
		return this.clientId;
	}
	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}
	public boolean equals(Object other)
	{
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof GoldqPayOrderId))
			return false;
		GoldqPayOrderId castOther = (GoldqPayOrderId) other;
		return ((this.getOrderId() == castOther.getOrderId()) || (this
				.getOrderId() != null && castOther.getOrderId() != null && this
				.getOrderId().equals(castOther.getOrderId())))
				&& ((this.getClientId() == castOther.getClientId()) || (this
						.getClientId() != null
						&& castOther.getClientId() != null && this
						.getClientId().equals(castOther.getClientId())));
	}
	public int hashCode()
	{
		int result = 17;
		result = 37 * result
				+ (getOrderId() == null ? 0 : this.getOrderId().hashCode());
		result = 37 * result
				+ (getClientId() == null ? 0 : this.getClientId().hashCode());
		return result;
	}
}