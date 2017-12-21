package com.yuyutechnology.exchange.server.controller.response;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.yuyutechnology.exchange.dto.UserInfo4Transfer;

@ApiModel(value="retCode=00000 03001 03006 03011 03012 03016 03017 03018 03020 03021 03022")
public class TransactionPreviewResponse extends BaseResponse {
	
	private String userAccount;
	private String currency;
	private String transAmount;

	@ApiModelProperty(value = "用户信息")
	private UserInfo4Transfer userInfo;
	
	@ApiModelProperty(required=true,value="用户账户（区号+手机号）",notes="")
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	@ApiModelProperty(required=true,value="货币类型",notes="")
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	@ApiModelProperty(required=true,value="交易金额",notes="")
	public String getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}
	public UserInfo4Transfer getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo4Transfer userInfo) {
		this.userInfo = userInfo;
	}

}
