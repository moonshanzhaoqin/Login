package com.yuyutechnology.exchange.server.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	public static Logger logger = LoggerFactory.getLogger(ExchangeController.class);

	@ApiOperation(value = "获取当前余额")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/exchange/getCurrentBalance")
	public @ResponseBody GetCurrentBalanceResponse getCurrentBalance(@PathVariable String token) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		GetCurrentBalanceResponse rep = new GetCurrentBalanceResponse();
		List<WalletInfo> wallets = exchangeManager.getWalletsByUserId(sessionData.getUserId());
		if (wallets.isEmpty()) {
			rep.setRetCode(ServerConsts.RET_CODE_FAILUE);
			rep.setMessage(MessageConsts.RET_CODE_FAILUE);
		} else {
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
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
			rep.setRetCode(ServerConsts.EXCHANGE_THE_SAME_CURRENCY_CAN_NOT_BE_EXCHANGED);
			rep.setMessage("The same currency can not be exchanged");
			return rep;
		}
		if ((reqMsg.getCurrencyOut().equals(ServerConsts.CURRENCY_OF_GOLDPAY) && reqMsg.getAmountOut() < 1)
				|| (!reqMsg.getCurrencyOut().equals(ServerConsts.CURRENCY_OF_GOLDPAY)
						&& reqMsg.getAmountOut() < 0.01)) {
			rep.setRetCode(ServerConsts.EXCHANGE_ENTER_THE_AMOUNT_OF_VIOLATION);
			rep.setMessage("Enter the amount of violation");
			return rep;
		}

		HashMap<String, String> result = exchangeManager.exchangeCalculation(sessionData.getUserId(),
				reqMsg.getCurrencyOut(), reqMsg.getCurrencyIn(), new BigDecimal(reqMsg.getAmountOut()));

		rep.setRetCode(result.get("retCode"));
		rep.setMessage(result.get("msg"));

		if (result.get("retCode").equals(ServerConsts.RET_CODE_SUCCESS)) {
			rep.setAmountIn(Double.parseDouble(result.get("in")));
			rep.setAmountOut(Double.parseDouble(result.get("out")));
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
			rep.setRetCode(ServerConsts.RET_CODE_FAILUE);
			rep.setMessage("This currency is not a tradable currency");
			return rep;
		}
		HashMap<String, Double> map = exchangeRateManager.getExchangeRate(reqMsg.getBase());
		if (map.isEmpty()) {
			rep.setRetCode(ServerConsts.RET_CODE_FAILUE);
			rep.setMessage("Failed to get exchange rate");
			return rep;
		}
		rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		rep.setBase(reqMsg.getBase());
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
				reqMsg.getCurrencyOut(), reqMsg.getCurrencyIn(), new BigDecimal(reqMsg.getAmountOut()),
				new BigDecimal(reqMsg.getAmountIn()));

		rep.setRetCode(result.get("retCode"));
		rep.setMessage(result.get("msg"));

		if (result.get("retCode").equals(ServerConsts.RET_CODE_SUCCESS)) {
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
		@SuppressWarnings("unchecked")
		List<ExchangeDTO> exs = new ArrayList<ExchangeDTO>();
		List<Exchange> list = (List<Exchange>) map.get("list");
		if (list.isEmpty()) {
			rep.setRetCode(ServerConsts.TRANSFER_HISTORY_NOT_ACQUIRED);
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
		}
		rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		rep.setCurrentPage((int) map.get("currentPage"));
		rep.setPageSize((int) map.get("pageSize"));
		rep.setPageTotal((int) map.get("pageTotal"));
		rep.setTotal(Integer.parseInt(map.get("total") + ""));
		rep.setList(exs);

		return rep;

	}

}
