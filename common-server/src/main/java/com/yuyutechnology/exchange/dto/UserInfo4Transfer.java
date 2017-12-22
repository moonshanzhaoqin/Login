package com.yuyutechnology.exchange.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class UserInfo4Transfer {
	@ApiModelProperty(value = "用户名")
	private String name;
	@ApiModelProperty(value = "用户头像")
	private String portrait;
	@ApiModelProperty(value = "是否是好友")
	private boolean friend;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public boolean isFriend() {
		return friend;
	}

	public void setFriend(boolean friend) {
		this.friend = friend;
	}
}
