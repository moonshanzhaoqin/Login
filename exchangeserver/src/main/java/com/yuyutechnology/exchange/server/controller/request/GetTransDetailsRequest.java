package com.yuyutechnology.exchange.server.controller.request;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel
public class GetTransDetailsRequest extends BaseRequest{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5107076769677091448L;
	private String transferId;

	@ApiModelProperty(required=true,value="订单Id")
	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

}
