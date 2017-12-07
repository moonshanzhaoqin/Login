package com.yuyutechnology.exchange.server.controller.response;

import com.yuyutechnology.exchange.dto.FeeTemplateDTO;
import com.yuyutechnology.exchange.server.controller.response.BaseResponse;

public class GetFeeTemplateResponse extends BaseResponse {
	private FeeTemplateDTO feeTemplate;

	public FeeTemplateDTO getFeeTemplate() {
		return feeTemplate;
	}

	public void setFeeTemplate(FeeTemplateDTO feeTemplate) {
		this.feeTemplate = feeTemplate;
	}
}
