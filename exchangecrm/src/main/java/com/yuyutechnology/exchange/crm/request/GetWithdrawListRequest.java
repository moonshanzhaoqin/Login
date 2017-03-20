package com.yuyutechnology.exchange.crm.request;

public class GetWithdrawListRequest {
	@Override
	public String toString() {
		return "GetWithdrawListRequest [currentPage=" + currentPage + ", userPhone=" + userPhone + ", reviewStatus="
				+ reviewStatus + ", goldpayRemit=" + goldpayRemit + "]";
	}

	private String currentPage;
	private String userPhone;
	private String reviewStatus;
	private String goldpayRemit;

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

	public String getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public String getGoldpayRemit() {
		return goldpayRemit;
	}

	public void setGoldpayRemit(String goldpayRemit) {
		this.goldpayRemit = goldpayRemit;
	}

}
