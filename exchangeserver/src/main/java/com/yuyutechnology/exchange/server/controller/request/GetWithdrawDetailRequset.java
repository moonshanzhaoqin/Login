package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class GetWithdrawDetailRequset {
	private String withdrawId;

	public String getWithdrawId() {
		return withdrawId;
	}

	public void setWithdrawId(String withdrawId) {
		this.withdrawId = withdrawId;
	}

	public boolean empty() {
		if (StringUtils.isBlank(this.withdrawId)) {
			return true;
		}
		return false;
	}
}
