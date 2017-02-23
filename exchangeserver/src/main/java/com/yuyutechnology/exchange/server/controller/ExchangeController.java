package com.yuyutechnology.exchange.server.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
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
import com.yuyutechnology.exchange.dto.WalletInfo;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.ExchangeRateManager;
import com.yuyutechnology.exchange.pojo.Exchange;
import com.yuyutechnology.exchange.server.controller.dto.ExchangeDTO;
import com.yuyutechnology.exchange.server.controller.request.ExchangeCalculationRequest;
import com.yuyutechnology.exchange.server.controller.request.ExchangeConfirmRequest;
import com.yuyutechnology.exchange.server.controller.request.GetExchangeHistoryRequest;
import com.yuyutechnology.exchange.server.controller.request.GetExchangeRateRequest;
import com.yuyutechnology.exchange.server.controller.response.ExchangeCalculationResponse;
import com.yuyutechnology.exchange.server.controller.response.ExchangeConfirmResponse;
import com.yuyutechnology.exchange.server.controller.response.GetCurrentBalanceResponse;
import com.yuyutechnology.exchange.server.controller.response.GetExchangeHistoryResponse;
import com.yuyutechnology.exchange.server.controller.response.GetExchangeRateResponse;
import com.yuyutechnology.exchange.server.session.SessionData;
import com.yuyutechnology.exchange.server.session.SessionDataHolder;

@Controller
public class ExchangeController {

	@Autowired
	ExchangeManager exchangeManager;
	@Autowired
	ExchangeRateManager exchangeRateManager;
	@Autowired
	CommonManager commonManager;

	public static Logger logger = LogManager.getLogger(ExchangeController.class);

	@ApiOperation(value = "获取当前余额")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/exchange/getCurrentBalance")
	public @ResponseBody GetCurrentBalanceResponse getCurrentBalance(@PathVariable String token) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		GetCurrentBalanceResponse rep = new GetCurrentBalanceResponse();
		List<WalletInfo> wallets = exchangeManager.getWalletsByUserId(sessionData.getUserId());
		if (wallets.isEmpty()) {
			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
			rep.setMessage(MessageConsts.RET_CODE_FAILUE);
		} else {
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			rep.setWallets(wallets);
		}
		return rep;
	}

	@ApiOperation(value = "兑换计算")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/exchange/exchangeCalculation")
	public @ResponseBody ExchangeCalculationResponse exchangeCalculation(@PathVariable String token,
			@RequestBody ExchangeCalculationRequest reqMsg) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		ExchangeCalculationResponse rep = new ExchangeCalculationResponse();

		if (reqMsg.getCurrencyIn().equals(reqMsg.getCurrencyOut())) {
			rep.setRetCode(RetCodeConsts.EXCHANGE_THE_SAME_CURRENCY_CAN_NOT_BE_EXCHANGED);
			rep.setMessage("The same currency can not be exchanged");
			return rep;
		}
		if ((reqMsg.getCurrencyOut().equals(ServerConsts.CURRENCY_OF_GOLDPAY) && reqMsg.getAmountOut() < 1)
				|| (!reqMsg.getCurrencyOut().equals(ServerConsts.CURRENCY_OF_GOLDPAY)
						&& reqMsg.getAmountOut() < 0.0001)) {
			rep.setRetCode(RetCodeConsts.EXCHANGE_ENTER_THE_AMOUNT_OF_VIOLATION);
			rep.setMessage("Enter the amount of violation");
			return rep;
		}

		HashMap<String, String> result = exchangeManager.exchangeCalculation(sessionData.getUserId(),
				reqMsg.getCurrencyOut(), reqMsg.getCurrencyIn(), new BigDecimal(Double.toString(reqMsg.getAmountOut())));

		rep.setRetCode(result.get("retCode"));
		rep.setMessage(result.get("msg"));

		if (result.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)) {
			rep.setAmountIn(Double.parseDouble(result.get("in")));
			rep.setAmountOut(Double.parseDouble(result.get("out")));
			rep.setRateUpdateTime(exchangeRateManager.getExchangeRateUpdateDate());
		}else if((result.get("retCode").equals(RetCodeConsts.EXCHANGE_LIMIT_DAILY_PAY)||
				result.get("retCode").equals(RetCodeConsts.EXCHANGE_LIMIT_NUM_OF_PAY_PER_DAY))||
				result.get("retCode").equals(RetCodeConsts.EXCHANGE_LIMIT_EACH_TIME)){
			rep.setOpts(new String[]{result.get("msg"),result.get("thawTime")});
		}

		return rep;
	}

	@ApiOperation(value = "获取汇率")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/exchange/getExchangeRate")
	public @ResponseBody GetExchangeRateResponse getExchangeRate(@PathVariable String token,
			@RequestBody GetExchangeRateRequest reqMsg) {
		GetExchangeRateResponse rep = new GetExchangeRateResponse();
		if (!commonManager.verifyCurrency(reqMsg.getBase())) {
			logger.warn("This currency is not a tradable currency");
			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
			rep.setMessage("This currency is not a tradable currency");
			return rep;
		}
		HashMap<String, Double> map = exchangeRateManager.getExchangeRate(reqMsg.getBase());
		if (map.isEmpty()) {
			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
			rep.setMessage("Failed to get exchange rate");
			return rep;
		}
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		rep.setBase(reqMsg.getBase());
		rep.setRateUpdateTime(exchangeRateManager.getExchangeRateUpdateDate());
		rep.setExchangeRates(map);

		return rep;

	}

	@ApiOperation(value = "兑换确认")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/exchange/exchangeConfirm")
	public @ResponseBody ExchangeConfirmResponse exchangeConfirm(@PathVariable String token,
			@RequestBody ExchangeConfirmRequest reqMsg) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		ExchangeConfirmResponse rep = new ExchangeConfirmResponse();
		HashMap<String, String> result = exchangeManager.exchangeConfirm(sessionData.getUserId(),
				reqMsg.getCurrencyOut(), reqMsg.getCurrencyIn(), new BigDecimal(Double.toString(reqMsg.getAmountOut())),
				new BigDecimal(Double.toString(reqMsg.getAmountIn())));

		rep.setRetCode(result.get("retCode"));
		rep.setMessage(result.get("msg"));

		if (result.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)) {
			rep.setAmountIn(Double.parseDouble(result.get("in")));
			rep.setAmountOut(Double.parseDouble(result.get("out")));
		}

		return rep;
	}

	@ApiOperation(value = "获取兑换历史记录")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/exchange/getExchangeHistory")
	public @ResponseBody GetExchangeHistoryResponse getExchangeHistory(@PathVariable String token,
			@RequestBody GetExchangeHistoryRequest reqMsg) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		GetExchangeHistoryResponse rep = new GetExchangeHistoryResponse();
		HashMap<String, Object> map = exchangeManager.getExchangeRecordsByPage(sessionData.getUserId(),
				reqMsg.getPeriod(), reqMsg.getCurrentPage(), reqMsg.getPageSize());
		List<ExchangeDTO> exs = new ArrayList<ExchangeDTO>();
		@SuppressWarnings("unchecked")
		List<Exchange> list = (List<Exchange>) map.get("list");
		if (list.isEmpty()) {
			rep.setRetCode(RetCodeConsts.TRANSFER_HISTORY_NOT_ACQUIRED);
			rep.setMessage("No data is available");
			rep.setList(exs);
			return rep;
		}
		for (Exchange exchange : list) {
			ExchangeDTO dto = new ExchangeDTO();
			dto.setCurrencyOut(exchange.getCurrencyOut());
			dto.setCurrencyIn(exchange.getCurrencyIn());
			dto.setCurrencyOutUnit(commonManager.getCurreny(dto.getCurrencyOut()).getCurrencyUnit());
			dto.setCurrencyInUnit(commonManager.getCurreny(dto.getCurrencyIn()).getCurrencyUnit());
			dto.setAmountOut(exchange.getAmountOut());
			dto.setAmountIn(exchange.getAmountIn());
			dto.setCreateTime(exchange.getCreateTime());
			dto.setFinishTime(exchange.getFinishTime());
			dto.setExchangeStatus(exchange.getExchangeStatus());
			exs.add(dto);
		}
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		rep.setCurrentPage((int) map.get("currentPage"));
		rep.setPageSize((int) map.get("pageSize"));
		rep.setPageTotal((int) map.get("pageTotal"));
		rep.setTotal(Integer.parseInt(map.get("total") + ""));
		rep.setList(exs);

		return rep;

	}

}
