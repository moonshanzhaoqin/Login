package com.yuyutechnology.exchange.pojo;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "e_fee_template")
public class FeeTemplate implements java.io.Serializable {

	@Override
	public String toString() {
		return "FeeTemplate [feePurpose=" + feePurpose + ", exemptAmount=" + exemptAmount + ", feePercent=" + feePercent
				+ ", minFee=" + minFee + ", maxFee=" + maxFee + "]";
	}

	private String feePurpose;
	private BigDecimal exemptAmount;
	private BigDecimal feePercent;
	private BigDecimal minFee;
	private BigDecimal maxFee;

	public FeeTemplate() {
	}

	public FeeTemplate(String feePurpose, BigDecimal exemptAmount, BigDecimal feePercent, BigDecimal minFee,
			BigDecimal maxFee) {
		this.feePurpose = feePurpose;
		this.exemptAmount = exemptAmount;
		this.feePercent = feePercent;
		this.minFee = minFee;
		this.maxFee = maxFee;
	}

	@Id

	@Column(name = "fee_purpose", unique = true, nullable = false)
	public String getFeePurpose() {
		return this.feePurpose;
	}

	public void setFeePurpose(String feePurpose) {
		this.feePurpose = feePurpose;
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


}
