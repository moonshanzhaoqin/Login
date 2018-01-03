package com.yuyutechnology.exchange.server.controller.request;

public class GetExchangeRateRequest extends BaseRequest{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1273217711265857212L;
	private String base;

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

}
