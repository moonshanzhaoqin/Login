package com.yuyutechnology.exchange.dto;

import java.math.BigDecimal;

public class FeeResult {
	private BigDecimal fee;
	private String formula;

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

}
