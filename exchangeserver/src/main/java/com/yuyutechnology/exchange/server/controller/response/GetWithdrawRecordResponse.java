package com.yuyutechnology.exchange.server.controller.response;

import java.util.List;

import com.yuyutechnology.exchange.dto.WithdrawDTO;

public class GetWithdrawRecordResponse extends BaseResponse {
	public List<WithdrawDTO> getList() {
		return list;
	}

	public void setList(List<WithdrawDTO> list) {
		this.list = list;
	}

	private List<WithdrawDTO> list;
}
