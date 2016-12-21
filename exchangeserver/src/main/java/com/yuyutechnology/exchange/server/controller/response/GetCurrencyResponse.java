package com.yuyutechnology.exchange.server.controller.response;

import java.util.List;

import com.yuyutechnology.exchange.dto.CurrencyInfo;

public class GetCurrencyResponse extends BaseResponse {
	private List<CurrencyInfo> currencyInfos;

	public List<CurrencyInfo> getCurrencyInfos() {
		return currencyInfos;
	}

	public void setCurrencyInfos(List<CurrencyInfo> currencyInfos) {
		this.currencyInfos = currencyInfos;
	}
}
