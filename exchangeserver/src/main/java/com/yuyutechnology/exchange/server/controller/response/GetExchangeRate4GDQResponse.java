package com.yuyutechnology.exchange.server.controller.response;

import java.util.Date;
import java.util.HashMap;

public class GetExchangeRate4GDQResponse extends BaseResponse{
	
	private HashMap<String, Double> rates;
	private Date updateDate;
	
	public HashMap<String, Double> getRates() {
		return rates;
	}
	public void setRates(HashMap<String, Double> rates) {
		this.rates = rates;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
