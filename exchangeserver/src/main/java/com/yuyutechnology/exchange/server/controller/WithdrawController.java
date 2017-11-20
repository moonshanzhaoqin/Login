package com.yuyutechnology.exchange.server.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

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
import com.yuyutechnology.exchange.manager.WithdrawManager;
import com.yuyutechnology.exchange.server.controller.request.WithdrawCalculateRequset;
import com.yuyutechnology.exchange.server.controller.request.WithdrawConfirmRequset;
import com.yuyutechnology.exchange.server.controller.response.WithdrawCalculateResponse;
import com.yuyutechnology.exchange.server.controller.response.WithdrawConfirmResponse;
import com.yuyutechnology.exchange.server.security.annotation.RequestDecryptBody;
import com.yuyutechnology.exchange.server.security.annotation.ResponseEncryptBody;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionDataHolder;

@Controller
public class WithdrawController {
	public static Logger logger = LogManager.getLogger(WithdrawController.class);

	@Autowired
	WithdrawManager withdrawManager;

	@ResponseEncryptBody
	@ApiOperation(value = "提现计算", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/withdraw/withdrawCalculate", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public WithdrawCalculateResponse withdrawCalculate(@PathVariable String token,
			@RequestDecryptBody WithdrawCalculateRequset withdrawCalculateRequset) throws ParseException {
		logger.info("========checkChangePhone : {}============", token);
		WithdrawCalculateResponse rep = new WithdrawCalculateResponse();
		SessionData sessionData = SessionDataHolder.getSessionData();

		Map<String, BigDecimal> resultMap = withdrawManager.goldBullion2Goldpay(sessionData.getUserId(),
				withdrawCalculateRequset.getGoldBullion());

		rep.setGoldpay(resultMap.get("goldpay").doubleValue());
		rep.setFee(resultMap.get("fee").doubleValue());
		logger.info(MessageConsts.RET_CODE_SUCCESS);
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);

		return rep;

	}

	@ResponseEncryptBody
	@ApiOperation(value = "提现确认", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/withdraw/withdrawConfirm", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public WithdrawConfirmResponse withdrawConfirm(@PathVariable String token,
			@RequestDecryptBody WithdrawConfirmRequset withdrawConfirmRequset) throws ParseException {
		logger.info("========checkChangePhone : {}============", token);
		WithdrawConfirmResponse rep = new WithdrawConfirmResponse();
		SessionData sessionData = SessionDataHolder.getSessionData();
		withdrawManager.applyConfirm(sessionData.getUserId(), withdrawConfirmRequset.getGoldBullion());

		logger.info(MessageConsts.RET_CODE_SUCCESS);
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);

		return rep;

	}

}
