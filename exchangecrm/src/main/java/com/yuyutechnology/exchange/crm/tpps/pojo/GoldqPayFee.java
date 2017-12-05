package com.yuyutechnology.exchange.crm.tpps.pojo;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "goldq_pay_fee", uniqueConstraints = @UniqueConstraint(columnNames = { "client_id", "pay_role" }))
public class GoldqPayFee implements java.io.Serializable {

	private Integer feeId;
	private String clientId;
	private byte payRole;
	private BigDecimal exemptAmount;
	private BigDecimal feePercent;
	private BigDecimal minFee;
	private BigDecimal maxFee;
	private byte feePayer;

	@Override
	public String toString() {
		return "GoldqPayFee [feeId=" + feeId + ", clientId=" + clientId + ", payRole=" + payRole + ", exemptAmount="
				+ exemptAmount + ", feePercent=" + feePercent + ", minFee=" + minFee + ", maxFee=" + maxFee
				+ ", feePayer=" + feePayer + "]";
	}

	public GoldqPayFee() {
	}

	public GoldqPayFee(String clientId, byte payRole, BigDecimal exemptAmount, BigDecimal feePercent, BigDecimal minFee,
			BigDecimal maxFee, byte feePayer) {
		this.clientId = clientId;
		this.payRole = payRole;
		this.exemptAmount = exemptAmount;
		this.feePercent = feePercent;
		this.minFee = minFee;
		this.maxFee = maxFee;
		this.feePayer = feePayer;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "fee_id", unique = true, nullable = false)
	public Integer getFeeId() {
		return this.feeId;
	}

	public void setFeeId(Integer feeId) {
		this.feeId = feeId;
	}

	@Column(name = "client_id", nullable = false, length = 32)
	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@Column(name = "pay_role", nullable = false)
	public byte getPayRole() {
		return this.payRole;
	}

	public void setPayRole(byte payRole) {
		this.payRole = payRole;
	}

	@Column(name = "exempt_amount", nullable = false, precision = 20, scale = 0)
	public BigDecimal getExemptAmount() {
		return this.exemptAmount;
	}

	public void setExemptAmount(BigDecimal exemptAmount) {
		this.exemptAmount = exemptAmount;
	}

	@Column(name = "fee_percent", nullable = false, precision = 20, scale = 4)
	public BigDecimal getFeePercent() {
		return this.feePercent;
	}

	public void setFeePercent(BigDecimal feePercent) {
		this.feePercent = feePercent;
	}

	@Column(name = "min_fee", nullable = false, precision = 20, scale = 0)
	public BigDecimal getMinFee() {
		return this.minFee;
	}

	public void setMinFee(BigDecimal minFee) {
		this.minFee = minFee;
	}

	@Column(name = "max_fee", nullable = false, precision = 20, scale = 0)
	public BigDecimal getMaxFee() {
		return this.maxFee;
	}

	public void setMaxFee(BigDecimal maxFee) {
		this.maxFee = maxFee;
	}

	@Column(name = "fee_payer", nullable = false)
	public byte getFeePayer() {
		return this.feePayer;
	}

	public void setFeePayer(byte feePayer) {
		this.feePayer = feePayer;
	}

}
