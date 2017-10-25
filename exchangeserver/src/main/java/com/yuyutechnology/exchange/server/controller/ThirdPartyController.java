/**
 * 
 */
package com.yuyutechnology.exchange.server.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
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
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dto.UserDTO;
import com.yuyutechnology.exchange.dto.UserInfo;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.server.controller.request.GetUserRequest;
import com.yuyutechnology.exchange.server.controller.request.ResendTransferPinRequest;
import com.yuyutechnology.exchange.server.controller.request.TransPwdConfirmRequest;
import com.yuyutechnology.exchange.server.controller.request.TransferConfirmRequest;
import com.yuyutechnology.exchange.server.controller.request.TransferInitiateRequest;
import com.yuyutechnology.exchange.server.controller.response.GetUserResponse;
import com.yuyutechnology.exchange.server.controller.response.ResendTransferPinResponse;
import com.yuyutechnology.exchange.server.controller.response.TransPwdConfirmResponse;
import com.yuyutechnology.exchange.server.controller.response.TransferConfirmResponse;
import com.yuyutechnology.exchange.server.controller.response.TransferInitiateResponse;
import com.yuyutechnology.exchange.server.security.annotation.RequestDecryptBody;
import com.yuyutechnology.exchange.server.security.annotation.ResponseEncryptBody;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionDataHolder;

/**
 * @author silent.sun
 *
 */
@Controller
public class ThirdPartyController {

	public static Logger logger = LogManager.getLogger(ThirdPartyController.class);
	
	@Autowired
	UserManager userManager;
	@Autowired
	ConfigManager configManager;
	@Autowired
	TransferManager transferManager;
	
	@ResponseBody
	@ApiOperation(value = "获取用户", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/getUser", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public GetUserResponse getUser(@RequestBody GetUserRequest getUserRequest) {
		logger.info("========getUser : {}============");
		GetUserResponse rep = new GetUserResponse();
		if (getUserRequest.empty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			UserDTO userDTO = userManager.getUser(getUserRequest.getAreaCode(), getUserRequest.getUserPhone());
			if (userDTO == null) {
				logger.info(MessageConsts.PHONE_NOT_EXIST);
				rep.setRetCode(RetCodeConsts.PHONE_NOT_EXIST);
				rep.setMessage(MessageConsts.PHONE_NOT_EXIST);
			} else {
				rep.setUserDTO(userDTO);
				logger.info("********Operation succeeded********");
				rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			}
		}
		return rep;
	}
	
	@ApiOperation(value = "交易初始化")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/transferInitiate")
	public @ResponseEncryptBody TransferInitiateResponse transferInitiate(@PathVariable String token,
			@RequestDecryptBody TransferInitiateRequest reqMsg) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		TransferInitiateResponse rep = new TransferInitiateResponse();

		if (StringUtils.isEmpty(reqMsg.getAreaCode()) || StringUtils.isEmpty(reqMsg.getUserPhone())) {
			logger.warn("Phone number is empty");
			rep.setRetCode(RetCodeConsts.TRANSFER_PHONE_NUMBER_IS_EMPTY);
			rep.setMessage("Phone number is empty");
			return rep;
		}
		// 装张金额上限
		if (!reqMsg.getCurrency().equals(ServerConsts.CURRENCY_OF_GOLDPAY) && reqMsg.getAmount() < 0.0001) {
			logger.warn("The input amount is less than the minimum amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
			rep.setMessage("The input amount is less than the minimum amount");
			return rep;
		} else if ((reqMsg.getCurrency()).equals(ServerConsts.CURRENCY_OF_GOLDPAY)
				&& (reqMsg.getAmount() % 1 > 0 || reqMsg.getAmount() == 0)) {
			logger.warn("The GDQ must be an integer value");
			rep.setRetCode(RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
			rep.setMessage("The GDQ must be an integer value");
			return rep;
		} else if (reqMsg.getAmount() > configManager.getConfigLongValue(ConfigKeyEnum.ENTERMAXIMUMAMOUNT,
				1000000000L)) {
			logger.warn("Fill out the allowable amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_FILL_OUT_THE_ALLOWABLE_AMOUNT);
			rep.setMessage("Fill out the allowable amount");
			return rep;
		}

		HashMap<String, String> map = transferManager.transferInitiate(sessionData.getUserId(), reqMsg.getAreaCode(),
				reqMsg.getUserPhone(), reqMsg.getCurrency(), new BigDecimal(Double.toString(reqMsg.getAmount())),
				reqMsg.getTransferComment(), 0);

		if (map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)) {
			rep.setTransferId(map.get("transferId"));
		} else if (map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_DAILY_PAY)) {
			rep.setOpts(new String[] { map.get("msg") + " " + map.get("unit"), map.get("thawTime") });
		} else if (map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_EACH_TIME)) {
			rep.setOpts(new String[] { map.get("msg") + " " + map.get("unit") });
		} else if (map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_NUM_OF_PAY_PER_DAY)) {
			rep.setOpts(new String[] { map.get("msg"), map.get("thawTime") });
		}

		rep.setRetCode(map.get("retCode"));
		rep.setMessage(map.get("msg"));

		return rep;

	}
	
	@ApiOperation(value = "验证支付密码")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/transPwdConfirm")
	public @ResponseEncryptBody TransPwdConfirmResponse transPwdConfirm(@PathVariable String token,
			@RequestDecryptBody TransPwdConfirmRequest reqMsg) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		TransPwdConfirmResponse rep = new TransPwdConfirmResponse();
		HashMap<String, String> result = transferManager.payPwdConfirm(sessionData.getUserId(), reqMsg.getTransferId(),
				reqMsg.getUserPayPwd());

		if (result.get("retCode").equals(RetCodeConsts.TRANSFER_REQUIRES_PHONE_VERIFICATION)) {
			// 发PIN码
			UserInfo user = userManager.getUserInfo(sessionData.getUserId());
			userManager.getPinCode(reqMsg.getTransferId(), user.getAreaCode(), user.getPhone());
			rep.setRetCode(result.get("retCode"));
			rep.setMessage("Need to send pin code verification");
		} else {
			rep.setRetCode(result.get("retCode"));
			rep.setMessage(result.get("msg"));
			rep.setOpts(new String[] { result.get("msg") });
		}

		return rep;
	}

	@ApiOperation(value = "重新发送pin")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/resendPhonePin")
	public @ResponseEncryptBody ResendTransferPinResponse resendTransferPin(@PathVariable String token,
			@RequestDecryptBody ResendTransferPinRequest reqMsg) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		ResendTransferPinResponse rep = new ResendTransferPinResponse();
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		try {
			UserInfo user = userManager.getUserInfo(sessionData.getUserId());
			userManager.getPinCode(reqMsg.getTransferId(), user.getAreaCode(), user.getPhone());
		} catch (Exception e) {
			e.printStackTrace();
			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
			rep.setMessage("Re-sending the phone pin failed");
		}
		return rep;
	}

	@ApiOperation(value = "交易确认")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/transferConfirm")
	public @ResponseEncryptBody TransferConfirmResponse transferConfirm(@PathVariable String token,
			@RequestDecryptBody TransferConfirmRequest reqMsg) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();

		UserInfo user = userManager.getUserInfo(sessionData.getUserId());
		TransferConfirmResponse rep = new TransferConfirmResponse();
		// 判断PinCode是否正确
		Boolean resultBool = userManager.testPinCode(reqMsg.getTransferId(), user.getAreaCode(), user.getPhone(),
				reqMsg.getPinCode());
		if (resultBool == null) {
			logger.info(MessageConsts.NOT_GET_CODE);
			rep.setRetCode(RetCodeConsts.NOT_GET_CODE);
			rep.setMessage(MessageConsts.NOT_GET_CODE);
		} else if (resultBool.booleanValue()) {

			String result = transferManager.transferConfirm(sessionData.getUserId(), reqMsg.getTransferId());

			if (result.equals(RetCodeConsts.RET_CODE_SUCCESS)) {
				rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			} else {
				rep.setRetCode(result);
				rep.setMessage("Current balance is insufficient");
			}
			userManager.clearPinCode(reqMsg.getTransferId(), user.getAreaCode(), user.getPhone());
		} else {
			rep.setRetCode(RetCodeConsts.PIN_CODE_INCORRECT);
			rep.setMessage("The pin code is incorrect");
		}
		return rep;
	}
	
	
	
}
