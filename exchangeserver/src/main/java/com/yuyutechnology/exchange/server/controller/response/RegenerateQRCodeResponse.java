package com.yuyutechnology.exchange.server.controller.response;

import java.math.BigDecimal;

public class RegenerateQRCodeResponse extends BaseResponse{
	private BigDecimal transferLimitPerPay;

	public BigDecimal getTransferLimitPerPay() {
		return transferLimitPerPay;
	}

	public void setTransferLimitPerPay(BigDecimal transferLimitPerPay) {
		this.transferLimitPerPay = transferLimitPerPay;
	}
}
