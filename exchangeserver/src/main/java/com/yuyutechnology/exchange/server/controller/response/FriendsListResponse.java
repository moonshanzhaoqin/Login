package com.yuyutechnology.exchange.server.controller.response;

import java.util.List;

import com.yuyutechnology.exchange.server.controller.dto.FriendDTO;

public class FriendsListResponse extends BaseResponse {
	private List<FriendDTO> friends;

	public List<FriendDTO> getFriends() {
		return friends;
	}

	public void setFriends(List<FriendDTO> friends) {
		this.friends = friends;
	}
}
