package com.yuyutechnology.exchange.server.controller.request;

import com.yuyutechnology.exchange.enums.FeePurpose;

public class GetFeeTemplateRequest {
	private FeePurpose feePurpose;

	public FeePurpose getFeePurpose() {
		return feePurpose;
	}

	public void setFeePurpose(FeePurpose feePurpose) {
		this.feePurpose = feePurpose;
	}

}
