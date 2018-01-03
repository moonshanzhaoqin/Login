package com.yuyutechnology.exchange.server.controller.request;

import com.yuyutechnology.exchange.enums.FeePurpose;

public class GetFeeTemplateRequest extends BaseRequest{
	/**
	 * 
	 */
	private static final long serialVersionUID = -468160955326406997L;
	private FeePurpose feePurpose;

	public FeePurpose getFeePurpose() {
		return feePurpose;
	}

	public void setFeePurpose(FeePurpose feePurpose) {
		this.feePurpose = feePurpose;
	}

}
