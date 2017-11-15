/**
 * 
 */
package com.yuyutechnology.exchange.server.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dto.UserDTO;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.server.controller.request.GetUserRequest;
import com.yuyutechnology.exchange.server.controller.request.TransConfirmRequest;
import com.yuyutechnology.exchange.server.controller.request.TransInitRequest;
import com.yuyutechnology.exchange.server.controller.response.GetUserResponse;
import com.yuyutechnology.exchange.server.security.annotation.RequestDecryptBody;
import com.yuyutechnology.exchange.server.security.annotation.ResponseEncryptBody;
import com.yuyutechnology.exchange.server.controller.response.TransConfirmResponse;
import com.yuyutechnology.exchange.server.controller.response.TransInitResponse;

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
	
	@ResponseEncryptBody
	@ApiOperation(value = "获取用户", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/3rd/getUser", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public GetUserResponse getUser(@RequestDecryptBody GetUserRequest getUserRequest) {
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
	
	@ResponseEncryptBody
	@ApiOperation(value = "交易初始化")
	@RequestMapping(method = RequestMethod.POST, value = "/3rd/transferInitiate")
	public TransInitResponse transInit(@ RequestDecryptBody TransInitRequest reqMsg) {

		TransInitResponse rep = new TransInitResponse();

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

		HashMap<String, String> map = transferManager.transferInitiate(reqMsg.getPayerId(), reqMsg.getPayeeId(),
				 reqMsg.getCurrency(), new BigDecimal(Double.toString(reqMsg.getAmount())),
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

	@ResponseEncryptBody
	@ApiOperation(value = "交易确认")
	@RequestMapping(method = RequestMethod.POST, value = "/3rd/transferConfirm")
	public TransConfirmResponse transConfirm(@RequestDecryptBody TransConfirmRequest reqMsg) {
		
		TransConfirmResponse rep = new TransConfirmResponse();
		HashMap<String, String> map = transferManager.transConfirm4TPPS(reqMsg.getUserId(), 
				reqMsg.getTransferId(), reqMsg.getUserPayPwd());
		
		rep.setRetCode(map.get("retCode"));
		rep.setMessage(map.get("msg"));
		
		return rep;
	}
	
	
	
}
