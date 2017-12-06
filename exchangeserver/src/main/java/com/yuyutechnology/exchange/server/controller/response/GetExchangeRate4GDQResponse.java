package com.yuyutechnology.exchange.server.controller.response;

import java.util.Date;
import java.util.HashMap;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="retCode=00000")
public class GetExchangeRate4GDQResponse extends BaseResponse {

	private HashMap<String, Double> rates;
	private Date updateDate;

	@ApiModelProperty(required=true,value="汇率列表")
	public HashMap<String, Double> getRates() {
		return rates;
	}

	public void setRates(HashMap<String, Double> rates) {
		this.rates = rates;
	}

	@ApiModelProperty(required=true,value="汇率更新时间")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
