package com.yuyutechnology.exchange.dto;

import java.util.List;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class FriendInitial {

	@ApiModelProperty(value = "用户名首字母")
	private char initial;
	private List<FriendDTO> friends;

	public char getInitial() {
		return initial;
	}

	public void setInitial(char initial) {
		this.initial = initial;
	}

	public List<FriendDTO> getFriends() {
		return friends;
	}

	public void setFriends(List<FriendDTO> friends) {
		this.friends = friends;
	}
}
