package com.yuyutechnology.exchange.server.controller.request;

public class GetWithdrawRecordRequest {
	private int currentPage;
	private int pageSize;
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
