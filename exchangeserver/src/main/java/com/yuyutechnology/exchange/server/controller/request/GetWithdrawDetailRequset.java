package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class GetWithdrawDetailRequset extends BaseRequest{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4832526980209823912L;
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
