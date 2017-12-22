package com.yuyutechnology.exchange.server.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.server.controller.request.AddFriendRequest;
import com.yuyutechnology.exchange.server.controller.request.DeleteFriendRequest;
import com.yuyutechnology.exchange.server.controller.request.SearchFriendRequest;
import com.yuyutechnology.exchange.server.controller.response.AddFriendResponse;
import com.yuyutechnology.exchange.server.controller.response.DeleteFriendResponse;
import com.yuyutechnology.exchange.server.controller.response.FriendsListResponse;
import com.yuyutechnology.exchange.server.controller.response.SearchFriendResponse;
import com.yuyutechnology.exchange.server.security.annotation.RequestDecryptBody;
import com.yuyutechnology.exchange.server.security.annotation.ResponseEncryptBody;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionDataHolder;

/**
 * @author suzan.wu
 */
@Controller
public class FriendController {
	public static Logger logger = LogManager.getLogger(FriendController.class);

	@Autowired
	UserManager userManager;

	/**
	 * addFriend 添加好友
	 * 
	 * @param token
	 * @param addFriendRequest
	 * @return
	 */
	@ResponseEncryptBody
	@ApiOperation(value = "添加好友", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/addFriend", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public AddFriendResponse addFriend(@PathVariable String token,
			@RequestDecryptBody AddFriendRequest addFriendRequest) {
		logger.info("========addFriend : {}============", token);
		AddFriendResponse rep = new AddFriendResponse();

		if (addFriendRequest.empty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			String retCode = userManager.addfriend(sessionData.getUserId(), addFriendRequest.getAreaCode(),
					addFriendRequest.getUserPhone());
			switch (retCode) {
			case RetCodeConsts.RET_CODE_SUCCESS:
				logger.info("********Operation succeeded********");
				rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
				break;
			case RetCodeConsts.PHONE_NOT_EXIST:
				logger.info(MessageConsts.PHONE_NOT_EXIST);
				rep.setRetCode(RetCodeConsts.PHONE_NOT_EXIST);
				rep.setMessage(MessageConsts.PHONE_NOT_EXIST);
				break;
			case RetCodeConsts.PHONE_ID_YOUR_OWEN:
				logger.info(MessageConsts.PHONE_ID_YOUR_OWEN);
				rep.setRetCode(RetCodeConsts.PHONE_ID_YOUR_OWEN);
				rep.setMessage(MessageConsts.PHONE_ID_YOUR_OWEN);
				break;
			case RetCodeConsts.FRIEND_HAS_ADDED:
				logger.info(MessageConsts.FRIEND_HAS_ADDED);
				rep.setRetCode(RetCodeConsts.FRIEND_HAS_ADDED);
				rep.setMessage(MessageConsts.FRIEND_HAS_ADDED);
				break;
			default:
				logger.info(MessageConsts.RET_CODE_FAILUE);
				rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
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
	@ResponseEncryptBody
	@ApiOperation(value = "好友列表", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/friendsList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public FriendsListResponse friendsList(@PathVariable String token) {
		logger.info("========friendsList : {}============", token);
		FriendsListResponse rep = new FriendsListResponse();
		SessionData sessionData = SessionDataHolder.getSessionData();
	
		rep.setInitials(userManager.getFriends(sessionData.getUserId()));
		logger.info("********Operation succeeded********");
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
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
	@ResponseEncryptBody
	@ApiOperation(value = "删除好友", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/deleteFriend", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public DeleteFriendResponse deleteFriend(@PathVariable String token,
			@RequestDecryptBody DeleteFriendRequest deleteFriendRequest) {
		logger.info("========deleteFriend : {}============", token);
		DeleteFriendResponse rep = new DeleteFriendResponse();
		SessionData sessionData = SessionDataHolder.getSessionData();
		if (deleteFriendRequest.empty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			String retCode = userManager.deleteFriend(sessionData.getUserId(), deleteFriendRequest.getAreaCode(),
					deleteFriendRequest.getPhone());
			switch (retCode) {
			case RetCodeConsts.RET_CODE_SUCCESS:
				logger.info("********Operation succeeded********");
				rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
				break;
			case RetCodeConsts.PHONE_NOT_EXIST:
				logger.info(MessageConsts.PHONE_NOT_EXIST);
				rep.setRetCode(RetCodeConsts.PHONE_NOT_EXIST);
				rep.setMessage(MessageConsts.PHONE_NOT_EXIST);
				break;
			case RetCodeConsts.PHONE_ID_YOUR_OWEN:
				logger.info(MessageConsts.PHONE_ID_YOUR_OWEN);
				rep.setRetCode(RetCodeConsts.PHONE_ID_YOUR_OWEN);
				rep.setMessage(MessageConsts.PHONE_ID_YOUR_OWEN);
				break;
			case RetCodeConsts.PHONE_IS_NOT_FRIEND:
				logger.info(MessageConsts.PHONE_IS_NOT_FRIEND);
				rep.setRetCode(RetCodeConsts.PHONE_IS_NOT_FRIEND);
				rep.setMessage(MessageConsts.PHONE_IS_NOT_FRIEND);
				break;
			default:
				logger.info(MessageConsts.RET_CODE_FAILUE);
				rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
				rep.setMessage(MessageConsts.RET_CODE_FAILUE);
				break;
			}
		}
		return rep;
	}
	
	//TODO 搜索好友
	@ResponseEncryptBody
	@ApiOperation(value = "搜索好友", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/searchFriend", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public SearchFriendResponse searchFriend(@PathVariable String token,@RequestDecryptBody SearchFriendRequest searchFriendRequest) {
		logger.info("========friendsList : {}============", token);
		SearchFriendResponse rep = new SearchFriendResponse();
		SessionData sessionData = SessionDataHolder.getSessionData();
		if (searchFriendRequest.empty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
		rep.setFriends(userManager.searchFriend(sessionData.getUserId(),searchFriendRequest.getKeyWords()));
		logger.info("********Operation succeeded********");
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);}
		return rep;
	}
}
