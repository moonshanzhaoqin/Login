package com.yuyutechnology.exchange.server.controller.response;

import java.util.List;

import com.yuyutechnology.exchange.server.controller.dto.FriendInfo;

public class FriendsListResponse extends BaseResponse {
	private List<FriendInfo> friends;

	public List<FriendInfo> getFriends() {
		return friends;
	}

	public void setFriends(List<FriendInfo> friends) {
		this.friends = friends;
	}
}
