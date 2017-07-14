package com.yuyutechnology.exchange.server.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.dto.CampaignInfo;
import com.yuyutechnology.exchange.dto.InviterInfo;
import com.yuyutechnology.exchange.manager.CampaignManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.server.controller.request.CollectRequest;
import com.yuyutechnology.exchange.server.controller.response.CollectResponse;
import com.yuyutechnology.exchange.server.controller.response.GetCampaignInfoResponse;
import com.yuyutechnology.exchange.server.controller.response.GetInviterInfoResponse;
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

	/**
	 * 活动信息
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "活动信息", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/invite/getCampaignInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public GetCampaignInfoResponse getCampaignInfo() {
		logger.info("========getCampaignInfo : {}============");
		GetCampaignInfoResponse rep = new GetCampaignInfoResponse();

		CampaignInfo CampaignInfo = campaignManager.getCampaignInfo();
		if (CampaignInfo == null) {
			logger.info(MessageConsts.RET_CODE_FAILUE);
			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
			rep.setMessage(MessageConsts.RET_CODE_FAILUE);
		} else {
			rep.setCampaignInfo(CampaignInfo);
			logger.info("********Operation succeeded********");
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		}

		return rep;
	}

	/**
	 * 领取
	 * 
	 * @param collectRequest
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "领取", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/invite/collect", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public CollectResponse collect(@RequestBody CollectRequest collectRequest) {
		logger.info("========collect : {}============");
		CollectResponse rep = new CollectResponse();

		if (collectRequest.Empty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else if (userManager.getUserId(collectRequest.getAreaCode(), collectRequest.getUserPhone()) != null) {
			logger.info(MessageConsts.PHONE_IS_REGISTERED);
			rep.setRetCode(RetCodeConsts.PHONE_IS_REGISTERED);
			rep.setMessage(MessageConsts.PHONE_IS_REGISTERED);
		} else if (campaignManager.activeCollect(collectRequest.getAreaCode(), collectRequest.getUserPhone()) != null) {
			logger.info(MessageConsts.PHONE_IS_COLLECTED);
			rep.setRetCode(RetCodeConsts.PHONE_IS_COLLECTED);
			rep.setMessage(MessageConsts.PHONE_IS_COLLECTED);
		} else {

			String retCode = campaignManager.collect(collectRequest.getAreaCode(), collectRequest.getUserPhone(),
					collectRequest.getInviterCode(), Integer.parseInt(collectRequest.getSharePath()));
			switch (retCode) {
			case RetCodeConsts.NO_CAMPAIGN:
				logger.info(MessageConsts.NO_CAMPAIGN);
				rep.setRetCode(RetCodeConsts.NO_CAMPAIGN);
				rep.setMessage(MessageConsts.NO_CAMPAIGN);
				break;
			case RetCodeConsts.EXCESS_BUDGET:
				logger.info(MessageConsts.EXCESS_BUDGET);
				rep.setRetCode(RetCodeConsts.EXCESS_BUDGET);
				rep.setMessage(MessageConsts.EXCESS_BUDGET);
				break;
			case RetCodeConsts.INVITERCODE_INCORRECT:
				logger.info(MessageConsts.INVITERCODE_INCORRECT);
				rep.setRetCode(RetCodeConsts.INVITERCODE_INCORRECT);
				rep.setMessage(MessageConsts.INVITERCODE_INCORRECT);
				break;
			case RetCodeConsts.RET_CODE_SUCCESS:
				logger.info("********Operation succeeded********");
				rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
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

}
