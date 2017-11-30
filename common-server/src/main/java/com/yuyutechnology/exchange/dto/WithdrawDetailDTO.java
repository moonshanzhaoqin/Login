package com.yuyutechnology.exchange.dto;

import java.math.BigDecimal;
import java.util.Date;

public class WithdrawDetailDTO {
	private String withdrawId;
	private int quantity;
	private BigDecimal goldpay;
	private BigDecimal fee;
	private Date applyTime;
	private Date handleTime;
	private byte handleResult;

	public String getWithdrawId() {
		return withdrawId;
	}

	public void setWithdrawId(String withdrawId) {
		this.withdrawId = withdrawId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
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

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public byte getHandleResult() {
		return handleResult;
	}

	public void setHandleResult(byte handleResult) {
		this.handleResult = handleResult;
	}
}
