package com.yuyutechnology.exchange.dto;

import java.math.BigDecimal;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class FeeTemplateDTO {
	@ApiModelProperty(value="免手续费额度")
	private BigDecimal exemptAmount;
	@ApiModelProperty(value="手续费率")
	private BigDecimal feePercent;
	@ApiModelProperty(value="最大手续费")
	private BigDecimal minFee;
	@ApiModelProperty(value="最小手续费")
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
