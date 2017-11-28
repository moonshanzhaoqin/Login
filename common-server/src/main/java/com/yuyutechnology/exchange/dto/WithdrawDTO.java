package com.yuyutechnology.exchange.dto;

import java.util.Date;

public class WithdrawDTO {
	private String withdrawId;
	private int quantity;
	private Date applyTime;
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

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public byte getHandleResult() {
		return handleResult;
	}

	public void setHandleResult(byte handleResult) {
		this.handleResult = handleResult;
	}
}
