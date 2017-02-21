package com.yuyutechnology.exchange.utils.oanda;

public class Meta {
	
	private EffectiveParams effective_params;
	private String request_time;
	private String[] skipped_currencies;
	
	public EffectiveParams getEffective_params() {
		return effective_params;
	}
	public void setEffective_params(EffectiveParams effective_params) {
		this.effective_params = effective_params;
	}
	public String getRequest_time() {
		return request_time;
	}
	public void setRequest_time(String request_time) {
		this.request_time = request_time;
	}
	public String[] getSkipped_currencies() {
		return skipped_currencies;
	}
	public void setSkipped_currencies(String[] skipped_currencies) {
		this.skipped_currencies = skipped_currencies;
	}

}
