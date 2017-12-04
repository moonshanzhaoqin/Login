package com.yuyutechnology.exchange.crm.request;

public class ChangeGoldqPayClientAbleRequest {
	private String clientId;
	private boolean disabled;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
