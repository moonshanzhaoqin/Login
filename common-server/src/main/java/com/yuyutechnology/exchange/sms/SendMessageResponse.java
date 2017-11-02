package com.yuyutechnology.exchange.sms;

public class SendMessageResponse {

	private Long limitCount;
	private Long limitTime;
	private boolean ok;

	public SendMessageResponse() {
		super();
	}

	public SendMessageResponse(boolean ok) {
		super();
		this.ok = ok;
	}

	public Long getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(Long limitCount) {
		this.limitCount = limitCount;
	}

	public Long getLimitTime() {
		return limitTime;
	}

	public void setLimitTime(Long limitTime) {
		this.limitTime = limitTime;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

}
