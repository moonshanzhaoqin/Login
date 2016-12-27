package com.yuyutechnology.exchange.server.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Friend;
import com.yuyutechnology.exchange.server.controller.dto.FriendInfo;
import com.yuyutechnology.exchange.server.controller.request.AddFriendRequest;
import com.yuyutechnology.exchange.server.controller.request.DeleteFriendRequest;
import com.yuyutechnology.exchange.server.controller.response.AddFriendResponse;
import com.yuyutechnology.exchange.server.controller.response.DeleteFriendResponse;
import com.yuyutechnology.exchange.server.controller.response.FriendsListResponse;
import com.yuyutechnology.exchange.server.session.SessionData;
import com.yuyutechnology.exchange.server.session.SessionDataHolder;

/**
 * @author suzan.wu
 */
@Controller
public class FriendController {
	public static Logger logger = LoggerFactory.getLogger(FriendController.class);

	@Autowired
	UserManager userManager;

	/**
	 * addFriend 添加好友
	 * 
	 * @param token
	 * @param addFriendRequest
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "添加好友", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/addFriend", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public AddFriendResponse addFriend(@PathVariable String token, @RequestBody AddFriendRequest addFriendRequest) {
		logger.info("========addFriend : {}============", token);
		AddFriendResponse rep = new AddFriendResponse();

		if (addFriendRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			String retCode = userManager.addfriend(sessionData.getUserId(), addFriendRequest.getAreaCode(),
					addFriendRequest.getUserPhone());
			switch (retCode) {
			case ServerConsts.RET_CODE_SUCCESS:
				logger.info("********Operation succeeded********");
				rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
				break;
			case ServerConsts.PHONE_NOT_EXIST:
				logger.info(MessageConsts.PHONE_NOT_EXIST);
				rep.setRetCode(ServerConsts.PHONE_NOT_EXIST);
				rep.setMessage(MessageConsts.PHONE_NOT_EXIST);
				break;
			case ServerConsts.PHONE_ID_YOUR_OWEN:
				logger.info(MessageConsts.PHONE_ID_YOUR_OWEN);
				rep.setRetCode(ServerConsts.PHONE_ID_YOUR_OWEN);
				rep.setMessage(MessageConsts.PHONE_ID_YOUR_OWEN);
				break;
			case ServerConsts.FRIEND_HAS_ADDED:
				logger.info(MessageConsts.FRIEND_HAS_ADDED);
				rep.setRetCode(ServerConsts.FRIEND_HAS_ADDED);
				rep.setMessage(MessageConsts.FRIEND_HAS_ADDED);
				break;
			default:
				logger.info(MessageConsts.RET_CODE_FAILUE);
				rep.setRetCode(ServerConsts.RET_CODE_FAILUE);
				rep.setMessage(MessageConsts.RET_CODE_FAILUE);
				break;
			}
		}
		return rep;
	}

	/**
	 * friendsList好友列表
	 * 
	 * @param token
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "好友列表", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/friendsList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public FriendsListResponse friendsList(@PathVariable String token) {
		logger.info("========friendsList : {}============", token);
		FriendsListResponse rep = new FriendsListResponse();
		SessionData sessionData = SessionDataHolder.getSessionData();
		List<FriendInfo> friendInfos = new ArrayList<FriendInfo>();
		List<Friend> friends = userManager.getFriends(sessionData.getUserId());
		for (Friend friend : friends) {
			logger.info("friend={}", friend.toString());
			friendInfos.add(new FriendInfo(friend.getUser().getAreaCode(), friend.getUser().getUserPhone(),
					friend.getUser().getUserName()));
		}
		rep.setFriends(friendInfos);
		logger.info("********Operation succeeded********");
		rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		return rep;
	}

	/**
	 * deleteFriend 删除好友
	 * 
	 * @param token
	 * @param deleteFriendRequest
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "删除好友", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/deleteFriend", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public DeleteFriendResponse deleteFriend(@PathVariable String token,
			@RequestBody DeleteFriendRequest deleteFriendRequest) {
		logger.info("========deleteFriend : {}============", token);
		DeleteFriendResponse rep = new DeleteFriendResponse();
		SessionData sessionData = SessionDataHolder.getSessionData();
		if (deleteFriendRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			String retCode = userManager.deleteFriend(sessionData.getUserId(), deleteFriendRequest.getAreaCode(),
					deleteFriendRequest.getPhone());
			switch (retCode) {
			case ServerConsts.RET_CODE_SUCCESS:
				logger.info("********Operation succeeded********");
				rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
				break;
			case ServerConsts.PHONE_NOT_EXIST:
				logger.info(MessageConsts.PHONE_NOT_EXIST);
				rep.setRetCode(ServerConsts.PHONE_NOT_EXIST);
				rep.setMessage(MessageConsts.PHONE_NOT_EXIST);
				break;
			case ServerConsts.PHONE_ID_YOUR_OWEN:
				logger.info(MessageConsts.PHONE_ID_YOUR_OWEN);
				rep.setRetCode(ServerConsts.PHONE_ID_YOUR_OWEN);
				rep.setMessage(MessageConsts.PHONE_ID_YOUR_OWEN);
				break;
			case ServerConsts.PHONE_IS_NOT_FRIEND:
				logger.info(MessageConsts.PHONE_IS_NOT_FRIEND);
				rep.setRetCode(ServerConsts.PHONE_IS_NOT_FRIEND);
				rep.setMessage(MessageConsts.PHONE_IS_NOT_FRIEND);
				break;
			default:
				logger.info(MessageConsts.RET_CODE_FAILUE);
				rep.setRetCode(ServerConsts.RET_CODE_FAILUE);
				rep.setMessage(MessageConsts.RET_CODE_FAILUE);
				break;
			}
		}
		return rep;
	}
}
