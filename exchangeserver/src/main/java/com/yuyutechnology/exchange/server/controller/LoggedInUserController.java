/**
 * 
 */
package com.yuyutechnology.exchange.server.controller;

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
import com.yuyutechnology.exchange.server.controller.request.BindGoldpayRequest;
import com.yuyutechnology.exchange.server.controller.request.ModifyPasswordRequest;
import com.yuyutechnology.exchange.server.controller.request.SetUserPayPwdRequest;
import com.yuyutechnology.exchange.server.controller.response.BaseResponse;
import com.yuyutechnology.exchange.session.SessionData;
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

	// TODO Modify password 修改用户密码
	@ResponseBody
	@ApiOperation(value = "修改用户密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/modifyPassword", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse modifyPassword(@PathVariable String token,
			@RequestBody ModifyPasswordRequest modifyPasswordRequest) {
		logger.info("========setUserPayPwd : {}============", token);
		BaseResponse rep = new BaseResponse();
		if (modifyPasswordRequest.isEmpty()) {
			logger.info("PARAMETER_IS_EMPTY");
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage("");
		} else {
			SessionData sessionData = sessionManager.get(token);
			userManager.updatePassword(sessionData.getUserId(), modifyPasswordRequest.getNewPassword());
			logger.info("********Operation succeeded********");
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage("");
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
	public BaseResponse setUserPayPwd(@PathVariable String token,
			@RequestBody SetUserPayPwdRequest setUserPayPwdRequest) {
		logger.info("========setUserPayPwd : {}============", token);
		BaseResponse rep = new BaseResponse();
		if (setUserPayPwdRequest.isEmpty()) {
			logger.info("PARAMETER_IS_EMPTY");
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage("");
		} else {
			SessionData sessionData = sessionManager.get(token);
			userManager.updateUserPayPwd(sessionData.getUserId(), setUserPayPwdRequest.getUserPayPwd());
			logger.info("********Operation succeeded********");
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage("");
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
	public BaseResponse bindGoldpay(@PathVariable String token, @RequestBody BindGoldpayRequest bindGoldpayRequest) {
		logger.info("========bindGoldpay : {}============", token);
		BaseResponse rep = new BaseResponse();
		if (bindGoldpayRequest.isEmpty()) {
			logger.info("PARAMETER_IS_EMPTY");
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = sessionManager.get(token);
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
			case ServerConsts.GOLDPAY_PHONE_NOT_MATCH:
				logger.info(MessageConsts.GOLDPAY_PHONE_NOT_MATCH);
				rep.setRetCode(ServerConsts.GOLDPAY_PHONE_NOT_MATCH);
				rep.setMessage(MessageConsts.GOLDPAY_PHONE_NOT_MATCH);
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
