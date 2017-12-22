package com.yuyutechnology.exchange.server.controller.response;

import java.util.List;

import com.yuyutechnology.exchange.dto.FriendDTO;


public class SearchFriendResponse extends BaseResponse {
	private List<FriendDTO> friends;

	public List<FriendDTO> getFriends() {
		return friends;
	}

	public void setFriends(List<FriendDTO> friends) {
		this.friends = friends;
	}
}