/**
 * 
 */
package com.yuyutechnology.exchange.server.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Friend;
import com.yuyutechnology.exchange.server.controller.dto.FriendInfo;
import com.yuyutechnology.exchange.server.controller.request.AddFriendRequest;
import com.yuyutechnology.exchange.server.controller.request.BindGoldpayRequest;
import com.yuyutechnology.exchange.server.controller.request.ChangePhoneRequest;
import com.yuyutechnology.exchange.server.controller.request.CheckPasswordRequest;
import com.yuyutechnology.exchange.server.controller.request.CheckPayPwdRequest;
import com.yuyutechnology.exchange.server.controller.request.ModifyPasswordRequest;
import com.yuyutechnology.exchange.server.controller.request.ModifyUserNameRequest;
import com.yuyutechnology.exchange.server.controller.request.SetUserPayPwdRequest;
import com.yuyutechnology.exchange.server.controller.response.AddFriendResponse;
import com.yuyutechnology.exchange.server.controller.response.BindGoldpayResponse;
import com.yuyutechnology.exchange.server.controller.response.ChangePhoneResponse;
import com.yuyutechnology.exchange.server.controller.response.CheckPasswordResponse;
import com.yuyutechnology.exchange.server.controller.response.CheckPayPwdResponse;
import com.yuyutechnology.exchange.server.controller.response.FriendsListResponse;
import com.yuyutechnology.exchange.server.controller.response.ModifyPasswordResponse;
import com.yuyutechnology.exchange.server.controller.response.ModifyUserNameResponse;
import com.yuyutechnology.exchange.server.controller.response.SetUserPayPwdResponse;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionDataHolder;
import com.yuyutechnology.exchange.session.SessionManager;

/**
 * @author suzan.wu
 */
@Controller
public class LoggedInUserController {
	public static Logger logger = LoggerFactory.getLogger(LoggedInUserController.class);

	@Autowired
	UserManager userManager;
	@Autowired
	ExchangeManager exchangeManager;
	@Autowired
	SessionManager sessionManager;

	/**
	 * Modify password 修改用户密码
	 * 
	 * @param token
	 * @param modifyPasswordRequest
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "修改用户密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/modifyPassword", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ModifyPasswordResponse modifyPassword(@PathVariable String token,
			@RequestBody ModifyPasswordRequest modifyPasswordRequest) {
		logger.info("========modifyPassword : {}============", token);
		ModifyPasswordResponse rep = new ModifyPasswordResponse();
		if (modifyPasswordRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			userManager.checkUserPassword(sessionData.getUserId(), modifyPasswordRequest.getOldPassword());
			userManager.updatePassword(sessionData.getUserId(), modifyPasswordRequest.getNewPassword());
			sessionManager.logout(sessionData.getSessionId());
			sessionManager.delLoginToken(sessionData.getUserId());
			logger.info("********Operation succeeded********");
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		}
		return rep;
	}

	/**
	 * 修改用户名 Modify userName
	 * 
	 * @param token
	 * @param modifyUserNameRequest
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "修改用户名", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/modifyUserName", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ModifyUserNameResponse modifyUserName(@PathVariable String token,
			@RequestBody ModifyUserNameRequest modifyUserNameRequest) {
		logger.info("========modifyPassword : {}============", token);
		ModifyUserNameResponse rep = new ModifyUserNameResponse();
		if (modifyUserNameRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			userManager.updateUserName(sessionData.getUserId(), modifyUserNameRequest.getNewUserName());
			logger.info("********Operation succeeded********");
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		}
		return rep;
	}

	/**
	 * 设置支付密码
	 * 
	 * @param token
	 * @param setUserPayPwdRequest
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "设置支付密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/setUserPayPwd", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public SetUserPayPwdResponse setUserPayPwd(@PathVariable String token,
			@RequestBody SetUserPayPwdRequest setUserPayPwdRequest) {
		logger.info("========setUserPayPwd : {}============", token);
		SetUserPayPwdResponse rep = new SetUserPayPwdResponse();
		if (setUserPayPwdRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			// PayPwd 6位数字
			if (setUserPayPwdRequest.getUserPayPwd().length() == 6
					&& StringUtils.isNumeric(setUserPayPwdRequest.getUserPayPwd())) {
				userManager.updateUserPayPwd(sessionData.getUserId(), setUserPayPwdRequest.getUserPayPwd());
				logger.info("********Operation succeeded********");
				rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			} else {
				logger.info(MessageConsts.PAY_PASSWORD_IS_ILLEGAL);
				rep.setRetCode(ServerConsts.PAY_PASSWORD_IS_ILLEGAL);
				rep.setMessage(MessageConsts.PAY_PASSWORD_IS_ILLEGAL);
			}
		}
		return rep;
	}

	/**
	 * 绑定goldpay
	 * 
	 * @param token
	 * @param bindGoldpayRequest
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "绑定goldpay", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/bindGoldpay", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BindGoldpayResponse bindGoldpay(@PathVariable String token,
			@RequestBody BindGoldpayRequest bindGoldpayRequest) {
		logger.info("========bindGoldpay : {}============", token);
		BindGoldpayResponse rep = new BindGoldpayResponse();
		if (bindGoldpayRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			String retCode = userManager.bindGoldpay(sessionData.getUserId(), bindGoldpayRequest.getGoldpayToken());
			switch (retCode) {
			case ServerConsts.RET_CODE_SUCCESS:
				logger.info("********Operation succeeded********");
				rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
				break;
			case ServerConsts.GOLDPAY_PHONE_IS_NOT_EXIST:
				logger.info(MessageConsts.GOLDPAY_PHONE_IS_NOT_EXIST);
				rep.setRetCode(ServerConsts.GOLDPAY_PHONE_IS_NOT_EXIST);
				rep.setMessage(MessageConsts.GOLDPAY_PHONE_IS_NOT_EXIST);
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
			case ServerConsts.ADD_FRIEND_OWEN:
				logger.info(MessageConsts.ADD_FRIEND_OWEN);
				rep.setRetCode(ServerConsts.ADD_FRIEND_OWEN);
				rep.setMessage(MessageConsts.ADD_FRIEND_OWEN);
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
		// TODO 分页
		logger.info("========friendsList : {}============", token);
		FriendsListResponse rep = new FriendsListResponse();
		SessionData sessionData = SessionDataHolder.getSessionData();
		List<FriendInfo> friendInfos = new ArrayList<FriendInfo>();
		List<Friend> friends = userManager.getFriends(sessionData.getUserId());
		for (Friend friend : friends) {
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
	 * changePhone 换绑手机
	 * 
	 * @param token
	 * @param changePhoneRequest
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "换绑手机", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/changePhone", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ChangePhoneResponse changePhone(@PathVariable String token,
			@RequestBody ChangePhoneRequest changePhoneRequest) {
		logger.info("========changePhone : {}============", token);
		ChangePhoneResponse rep = new ChangePhoneResponse();
		SessionData sessionData = SessionDataHolder.getSessionData();

		// 校验验证码
		if (userManager.testPinCode(ServerConsts.PIN_FUNC_CHANGEPHONE, changePhoneRequest.getAreaCode(),
				changePhoneRequest.getUserPhone(), changePhoneRequest.getVerificationCode())) {
			userManager.changePhone(sessionData.getUserId(), changePhoneRequest.getAreaCode(),
					changePhoneRequest.getUserPhone());
			sessionManager.logout(sessionData.getSessionId());
			sessionManager.delLoginToken(sessionData.getUserId());
			logger.info("********Operation succeeded********");
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		} else {
			logger.info(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
			rep.setRetCode(ServerConsts.PHONE_AND_CODE_NOT_MATCH);
			rep.setMessage(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
		}

		return rep;
	}

	/**
	 * checkPassword 校验登录密码
	 * 
	 * @param token
	 * @param checkPasswordRequest
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "换绑手机-校验登录密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/checkPassword", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public CheckPasswordResponse checkPassword(@PathVariable String token,
			@RequestBody CheckPasswordRequest checkPasswordRequest) {
		logger.info("========checkPassword : {}============", token);
		CheckPasswordResponse rep = new CheckPasswordResponse();
		SessionData sessionData = SessionDataHolder.getSessionData();
		if (userManager.checkUserPassword(sessionData.getUserId(), checkPasswordRequest.getUserPassword())) {
			logger.info("********Operation succeeded********");
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		} else {
			logger.info(MessageConsts.PASSWORD_NOT_MATCH);
			rep.setRetCode(ServerConsts.PASSWORD_NOT_MATCH);
			rep.setMessage(MessageConsts.PASSWORD_NOT_MATCH);
		}
		return rep;
	}

	/**
	 * checkPayPwd 校验支付密码
	 * 
	 * @param token
	 * @param checkPayPwdRequest
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "更换支付密码-校验支付密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/checkPayPwd", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public CheckPayPwdResponse checkPayPwd(@PathVariable String token,
			@RequestBody CheckPayPwdRequest checkPayPwdRequest) {
		logger.info("========checkPassword : {}============", token);
		CheckPayPwdResponse rep = new CheckPayPwdResponse();
		SessionData sessionData = SessionDataHolder.getSessionData();
		if (userManager.checkUserPayPwd(sessionData.getUserId(), checkPayPwdRequest.getUserPayPwd())) {
			logger.info("********Operation succeeded********");
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		} else {
			logger.info(MessageConsts.PAY_PWD_NOT_MATCH);
			rep.setRetCode(ServerConsts.PAY_PWD_NOT_MATCH);
			rep.setMessage(MessageConsts.PAY_PWD_NOT_MATCH);
		}
		return rep;
	}

}
