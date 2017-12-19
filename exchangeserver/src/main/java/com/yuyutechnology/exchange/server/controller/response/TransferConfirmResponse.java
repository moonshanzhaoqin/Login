package com.yuyutechnology.exchange.server.controller.response;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="retCode=00000 00003 01012 01021 03005 03012 03013 03020 03021 03022 03024")
public class TransferConfirmResponse extends BaseResponse {
	
	private String makeFriends;

	@ApiModelProperty(required=true,value="是否是好友关系",notes="0是1否")
	public String getMakeFriends() {
		return makeFriends;
	}

	public void setMakeFriends(String makeFriends) {
		this.makeFriends = makeFriends;
	}

}
