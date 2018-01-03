package com.yuyutechnology.exchange.server.controller.request;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class GetUserConfigRequest extends BaseRequest{

	private String key;
	private String value;

	@ApiModelProperty(allowableValues = "SHOW_EXCHANGE_TIPS")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
