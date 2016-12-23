package com.yuyutechnology.exchange.server.controller.response;

public class CheckChangePhoneResponse extends BaseResponse {
	private boolean change;
	private long time;

	public boolean isChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
