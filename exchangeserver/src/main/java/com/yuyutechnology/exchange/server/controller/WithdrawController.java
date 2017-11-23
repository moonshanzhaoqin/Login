package com.yuyutechnology.exchange.server.controller;

import java.text.ParseException;
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
import com.yuyutechnology.exchange.dto.WithdrawCalResult;
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
		logger.info("========withdrawCalculate : {}============", token);
		WithdrawCalculateResponse rep = new WithdrawCalculateResponse();
		SessionData sessionData = SessionDataHolder.getSessionData();

		WithdrawCalResult result = withdrawManager.withdrawCalculate(sessionData.getUserId(),
				withdrawCalculateRequset.getGoldBullion());
		switch (result.getRetCode()) {
		case RetCodeConsts.RET_CODE_SUCCESS:
			rep.setGoldpay(result.getGoldpay().intValue());
			rep.setFee(result.getFee().intValue());
			logger.info(MessageConsts.RET_CODE_SUCCESS);
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			break;
		case RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT:
			logger.info(MessageConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
			rep.setRetCode(RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
			rep.setMessage(MessageConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
			break;
		default:
			logger.info(MessageConsts.RET_CODE_FAILUE);
			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
			rep.setMessage(MessageConsts.RET_CODE_FAILUE);
			break;
		}

		return rep;

	}

	@ResponseEncryptBody
	@ApiOperation(value = "提现确认", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/withdraw/withdrawConfirm", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public WithdrawConfirmResponse withdrawConfirm(@PathVariable String token,
			@RequestDecryptBody WithdrawConfirmRequset withdrawConfirmRequset) throws ParseException {
		logger.info("========withdrawConfirm : {}============", token);
		WithdrawConfirmResponse rep = new WithdrawConfirmResponse();
		SessionData sessionData = SessionDataHolder.getSessionData();

		Integer withdrawId = withdrawManager.applyConfirm(sessionData.getUserId(),
				withdrawConfirmRequset.getGoldBullion());
		if (withdrawId == null) {
			logger.info(MessageConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
			rep.setRetCode(RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
			rep.setMessage(MessageConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
		} else {
			withdrawManager.goldpayTrans4Apply(withdrawId);
			/* 通知管理员 */
			withdrawManager.notifyWithdraw(sessionData.getUserId(), withdrawConfirmRequset.getGoldBullion());
			logger.info(MessageConsts.RET_CODE_SUCCESS);
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		}
		return rep;
	}

}
