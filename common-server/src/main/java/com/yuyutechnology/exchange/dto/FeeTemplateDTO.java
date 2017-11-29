package com.yuyutechnology.exchange.dto;

import java.math.BigDecimal;

public class FeeTemplateDTO {
	private BigDecimal exemptAmount;
	private BigDecimal feePercent;
	private BigDecimal minFee;
	private BigDecimal maxFee;

	public BigDecimal getExemptAmount() {
		return exemptAmount;
	}

	public void setExemptAmount(BigDecimal exemptAmount) {
		this.exemptAmount = exemptAmount;
	}

	public BigDecimal getFeePercent() {
		return feePercent;
	}

	public void setFeePercent(BigDecimal feePercent) {
		this.feePercent = feePercent;
	}

	public BigDecimal getMinFee() {
		return minFee;
	}

	public void setMinFee(BigDecimal minFee) {
		this.minFee = minFee;
	}

	public BigDecimal getMaxFee() {
		return maxFee;
	}

	public void setMaxFee(BigDecimal maxFee) {
		this.maxFee = maxFee;
	}
}
