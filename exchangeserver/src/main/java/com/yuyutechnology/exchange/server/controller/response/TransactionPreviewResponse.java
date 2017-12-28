package com.yuyutechnology.exchange.server.controller.response;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="retCode=00000 03001 03006 03011 03012 03016 03017 03018 03020 03021 03022")
public class TransactionPreviewResponse extends BaseResponse {
	
	private String userAccount;
	private String userName;
	private String portrait;
	private boolean friend;
	private String transAmount;
	private String currency;

	@ApiModelProperty(value="用户账户（区号+手机号）",notes="")
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	@ApiModelProperty(value="货币类型",notes="")
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	@ApiModelProperty(value="交易金额",notes="")
	public String getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}
	@ApiModelProperty(value = "用户名")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@ApiModelProperty(value = "用户头像")
	public String getPortrait() {
		return portrait;
	}
	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
	@ApiModelProperty(value = "是否是好友")
	public boolean isFriend() {
		return friend;
	}
	public void setFriend(boolean friend) {
		this.friend = friend;
	}
}
