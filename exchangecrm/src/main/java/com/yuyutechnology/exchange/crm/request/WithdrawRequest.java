package com.yuyutechnology.exchange.crm.request;

public class WithdrawRequest {
	private String transferId;

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	@Override
	public String toString() {
		return "WithdrawRequest [transferId=" + transferId + "]";
	}

}
