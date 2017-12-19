package com.yuyutechnology.exchange.server.controller.response;

import java.util.List;

import com.yuyutechnology.exchange.dto.FriendDTO;
import com.yuyutechnology.exchange.dto.FriendInitial;


public class FriendsListResponse extends BaseResponse {
	private List<FriendInitial> initials;

	public List<FriendInitial> getInitials() {
		return initials;
	}

	public void setInitials(List<FriendInitial> initials) {
		this.initials = initials;
	}

}
