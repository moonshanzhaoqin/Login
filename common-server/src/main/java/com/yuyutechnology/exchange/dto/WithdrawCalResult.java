package com.yuyutechnology.exchange.dto;

import java.math.BigDecimal;

public class WithdrawCalResult {
private  String  retCode;
private BigDecimal goldpay;
private BigDecimal fee;
	private String formula;

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public BigDecimal getGoldpay() {
		return goldpay;
	}

	public void setGoldpay(BigDecimal goldpay) {
		this.goldpay = goldpay;
	}

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
