package com.yuyutechnology.exchange.server.controller.request;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiParam;

@ApiModel("")
public class GetTransactionRecordRequest {

	private String period;
	private String type;
	private int currentPage;
	private int pageSize;

	@ApiParam(value = "今天:today;最近一个月:lastMonth;最近三个月:last3Month;最近一年:lastYear;一年以前:aYearAgo;")
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	@ApiParam(value = "转账：expenses；收入：income；充值：recharge；")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
