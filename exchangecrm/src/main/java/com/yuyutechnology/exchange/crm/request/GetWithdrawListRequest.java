package com.yuyutechnology.exchange.crm.request;

public class GetWithdrawListRequest {

	@Override
	public String toString() {
		return "GetWithdrawListRequest [currentPage=" + currentPage + ", userPhone=" + userPhone + ", transferId="
				+ transferId + ", transferStatus=" + transferStatus + "]";
	}

	private String currentPage;
	private String userPhone;
	private String transferId;
	private String transferStatus;

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public String getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}
}
