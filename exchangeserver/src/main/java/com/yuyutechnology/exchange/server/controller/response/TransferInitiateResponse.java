package com.yuyutechnology.exchange.server.controller.response;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="retCode=00000,00001,03001,03006,03011,03012,03016,03017,03018,03020,03021,03022")
public class TransferInitiateResponse extends BaseResponse {

	private String transferId;

	@ApiModelProperty(required=true,value="生成的订单号",notes="")
	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

}
