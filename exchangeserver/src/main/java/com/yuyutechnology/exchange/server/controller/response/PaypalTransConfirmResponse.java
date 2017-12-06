package com.yuyutechnology.exchange.server.controller.response;

import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(value="retCode=00000,00001,03024,03025,03026")
public class PaypalTransConfirmResponse extends BaseResponse {

	private String transId;

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

}
