package com.yuyutechnology.exchange.server.controller.response;

import java.util.List;

import com.yuyutechnology.exchange.server.controller.dto.NotificationDTO;

public class GetNotificationRecordsResponse extends BaseResponse {
	
	private int currentPage;
	private int pageSize;
	private int total;
	private int pageTotal;
	private List<NotificationDTO> list;
	
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
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPageTotal() {
		return pageTotal;
	}
	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}
	public List<NotificationDTO> getList() {
		return list;
	}
	public void setList(List<NotificationDTO> list) {
		this.list = list;
	}
}
