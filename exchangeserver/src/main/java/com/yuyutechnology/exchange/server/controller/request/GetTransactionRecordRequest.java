package com.yuyutechnology.exchange.server.controller.request;

public class GetTransactionRecordRequest {
	
	private String period;
	private int status;
	
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

}
