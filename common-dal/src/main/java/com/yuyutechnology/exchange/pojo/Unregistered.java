package com.yuyutechnology.exchange.pojo;
// Generated Dec 2, 2016 4:27:04 PM by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Unregistered generated by hbm2java
 */
@Entity
@Table(name = "unregistered", catalog = "anytime_exchange")
public class Unregistered implements java.io.Serializable {

	private Integer unregisteredId;
	private byte[] phone;
	private String currency;
	private BigDecimal amount;
	private Date createTime;
	private int unregisteredStatus;

	public Unregistered() {
	}

	public Unregistered(byte[] phone, String currency, BigDecimal amount, Date createTime, int unregisteredStatus) {
		this.phone = phone;
		this.currency = currency;
		this.amount = amount;
		this.createTime = createTime;
		this.unregisteredStatus = unregisteredStatus;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "unregistered_id", unique = true, nullable = false)
	public Integer getUnregisteredId() {
		return this.unregisteredId;
	}

	public void setUnregisteredId(Integer unregisteredId) {
		this.unregisteredId = unregisteredId;
	}

	@Column(name = "phone", nullable = false)
	public byte[] getPhone() {
		return this.phone;
	}

	public void setPhone(byte[] phone) {
		this.phone = phone;
	}

	@Column(name = "currency", nullable = false, length = 3)
	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Column(name = "amount", nullable = false, precision = 10)
	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "unregistered_status", nullable = false)
	public int getUnregisteredStatus() {
		return this.unregisteredStatus;
	}

	public void setUnregisteredStatus(int unregisteredStatus) {
		this.unregisteredStatus = unregisteredStatus;
	}

}
