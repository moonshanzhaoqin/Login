/**
 * 
 */
package com.yuyutechnology.exchange.server.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.dto.UserDTO;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.server.controller.request.GetUserRequest;
import com.yuyutechnology.exchange.server.controller.response.GetUserResponse;
import com.yuyutechnology.exchange.server.security.annotation.RequestDecryptBody;
import com.yuyutechnology.exchange.server.security.annotation.ResponseEncryptBody;

/**
 * @author silent.sun
 *
 */
@Controller
public class ThirdPartyController {

	public static Logger logger = LogManager.getLogger(ThirdPartyController.class);
	
	@Autowired
	UserManager userManager;
	
	@ResponseEncryptBody
	@ApiOperation(value = "获取用户", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/getUser", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
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
}
