package com.yuyutechnology.exchange.crm.request;

public class GetWithdrawListRequest {
	@Override
	public String toString() {
		return "GetWithdrawListRequest [currentPage=" + currentPage + ", userId=" + userId + ", reviewStatus="
				+ reviewStatus + ", goldpayRemit=" + goldpayRemit + "]";
	}

	private String currentPage;
	private String userId;
	private String reviewStatus;
	private String goldpayRemit;

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
