package com.yuyutechnology.exchange.dto;

import com.yuyutechnology.exchange.enums.CheckPWDStatus;

public class CheckPwdResult {
	private CheckPWDStatus status;
	private long info;

	public long getInfo() {
		return info;
	}

	public void setInfo(long info) {
		this.info = info;
	}

	public CheckPWDStatus getStatus() {
		return status;
	}

	public void setStatus(CheckPWDStatus status) {
		this.status = status;
	}
}
