package com.yuyutechnology.exchange.server.controller.request;

public class GetNotificationRecordsRequest extends BaseRequest{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3324843166568488728L;
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
