package com.yuyutechnology.exchange.server.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.dto.InviterInfo;
import com.yuyutechnology.exchange.manager.CampaignManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Friend;
import com.yuyutechnology.exchange.server.controller.dto.FriendDTO;
import com.yuyutechnology.exchange.server.controller.request.AddFriendRequest;
import com.yuyutechnology.exchange.server.controller.request.DeleteFriendRequest;
import com.yuyutechnology.exchange.server.controller.response.AddFriendResponse;
import com.yuyutechnology.exchange.server.controller.response.DeleteFriendResponse;
import com.yuyutechnology.exchange.server.controller.response.FriendsListResponse;
import com.yuyutechnology.exchange.server.controller.response.GetInviterInfoResponse;
import com.yuyutechnology.exchange.server.security.annotation.RequestDecryptBody;
import com.yuyutechnology.exchange.server.security.annotation.ResponseEncryptBody;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionDataHolder;

/**
 * @author suzan.wu
 */
@Controller
public class CampaignController {
	public static Logger logger = LogManager.getLogger(CampaignController.class);

	@Autowired
	UserManager userManager;
	@Autowired
	CampaignManager campaignManager;

	/**
	 * 邀请人信息
	 * 
	 * @param token
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "邀请人信息", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/invite/getInviterInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public GetInviterInfoResponse getInviterInfo(@PathVariable String token) {
		logger.info("========getInviterInfo : {}============", token);
		GetInviterInfoResponse rep = new GetInviterInfoResponse();

		SessionData sessionData = SessionDataHolder.getSessionData();
		InviterInfo inviterInfo = campaignManager.getInviterInfo(sessionData.getUserId());
		if (inviterInfo == null) {
			logger.info(MessageConsts.RET_CODE_FAILUE);
			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
			rep.setMessage(MessageConsts.RET_CODE_FAILUE);
		} else {
			rep.setInviterInfo(inviterInfo);
			logger.info("********Operation succeeded********");
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		}

		return rep;
	}

}
