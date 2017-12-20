package com.yuyutechnology.exchange.server.controller.response;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="retCode=00000 03001 03006 03011 03012 03016 03017 03018 03020 03021 03022")
public class TransactionPreviewResponse extends BaseResponse {
	
	private String userAccount;
	private String userName;
	private String currency;
	private String transAmount;
	private String avatarUrl;
	private String addFriends;
	
	@ApiModelProperty(required=true,value="用户账户（区号+手机号）",notes="")
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	@ApiModelProperty(required=true,value="用户名",notes="")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	@ApiModelProperty(required=true,value="头像url",notes="")
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	
	@ApiModelProperty(required=true,value="好友关系：0已经是好友1还不是好友",notes="")
	public String getAddFriends() {
		return addFriends;
	}
	public void setAddFriends(String addFriends) {
		this.addFriends = addFriends;
	}
}
