package com.yuyutechnology.exchange.crm.request;

public class GetGoldqPayClientByPageRequest {
	@Override
	public String toString() {
		return "GetGoldqPayClientByPageRequest [currentPage=" + currentPage + "]";
	}

	private String currentPage;

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
}
