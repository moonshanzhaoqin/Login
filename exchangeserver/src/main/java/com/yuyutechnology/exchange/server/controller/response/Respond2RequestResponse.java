package com.yuyutechnology.exchange.server.controller.response;

import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(value="retCode=00000,00001,03001,03006,03011,03012,03018,03019,03020,03021,03022")
public class Respond2RequestResponse extends BaseResponse {
	private String transferId;

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
}
