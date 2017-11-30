package com.yuyutechnology.exchange.server.controller.response;

import com.yuyutechnology.exchange.dto.WithdrawDetailDTO;

public class GetWithdrawDetailResponse extends BaseResponse {
	private WithdrawDetailDTO withdrawDetail;

	public WithdrawDetailDTO getWithdrawDetail() {
		return withdrawDetail;
	}

	public void setWithdrawDetail(WithdrawDetailDTO withdrawDetail) {
		this.withdrawDetail = withdrawDetail;
	}
}
