package com.yuyutechnology.exchange.server.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.pojo.Exchange;
import com.yuyutechnology.exchange.server.controller.dto.ExchangeDTO;
import com.yuyutechnology.exchange.server.controller.request.ExchangeCalculationRequest;
import com.yuyutechnology.exchange.server.controller.request.ExchangeConfirmRequest;
import com.yuyutechnology.exchange.server.controller.request.GetExchangeDetailsRequest;
import com.yuyutechnology.exchange.server.controller.request.GetExchangeHistoryRequest;
import com.yuyutechnology.exchange.server.controller.request.GetExchangeRateRequest;
import com.yuyutechnology.exchange.server.controller.response.ExchangeCalculationResponse;
import com.yuyutechnology.exchange.server.controller.response.ExchangeConfirmResponse;
import com.yuyutechnology.exchange.server.controller.response.GetExchangeDetailsResponse;
import com.yuyutechnology.exchange.server.controller.response.GetExchangeHistoryResponse;
import com.yuyutechnology.exchange.server.controller.response.GetExchangeRateResponse;
import com.yuyutechnology.exchange.server.security.annotation.RequestDecryptBody;
import com.yuyutechnology.exchange.server.security.annotation.ResponseEncryptBody;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionDataHolder;

@Controller
class ExchangeController {
	@Autowired
	ExchangeManager exchangeManager;
	@Autowired
	OandaRatesManager oandaRatesManager;
	@Autowired
	CommonManager commonManager;

	public static Logger logger = LogManager.getLogger(ExchangeController.class);

	@ApiOperation(value = "兑换计算")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/exchange/exchangeCalculation")
	public @ResponseEncryptBody ExchangeCalculationResponse exchangeCalculation(@PathVariable String token,
			@RequestDecryptBody ExchangeCalculationRequest reqMsg) {
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
				reqMsg.getCurrencyOut(), reqMsg.getCurrencyIn(),
				new BigDecimal(Double.toString(reqMsg.getAmountOut())));

		rep.setRetCode(result.get("retCode"));
		rep.setMessage(result.get("msg"));

		if (result.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)) {
			rep.setAmountIn(Double.parseDouble(result.get("in")));
			rep.setAmountOut(Double.parseDouble(result.get("out")));
			rep.setRateUpdateTime(oandaRatesManager.getExchangeRateUpdateDate());
		} else if (result.get("retCode").equals(RetCodeConsts.EXCHANGE_LIMIT_NUM_OF_PAY_PER_DAY)) {
			rep.setOpts(new String[] { result.get("msg"), result.get("thawTime") });
		} else if (result.get("retCode").equals(RetCodeConsts.EXCHANGE_LIMIT_EACH_TIME)) {
			rep.setOpts(new String[] { result.get("msg") + " " + result.get("unit") });
		} else if (result.get("retCode").equals(RetCodeConsts.EXCHANGE_LIMIT_DAILY_PAY)) {
			rep.setOpts(new String[] { result.get("msg") + " " + result.get("unit"), result.get("thawTime") });
		}

		return rep;
	}

	@ApiOperation(value = "获取汇率")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/exchange/getExchangeRate")
	public @ResponseEncryptBody GetExchangeRateResponse getExchangeRate(@PathVariable String token,
			@RequestDecryptBody GetExchangeRateRequest reqMsg) {
		GetExchangeRateResponse rep = new GetExchangeRateResponse();
		if (!commonManager.verifyCurrency(reqMsg.getBase())) {
			logger.info("This currency is not a tradable currency");
			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
			rep.setMessage("This currency is not a tradable currency");
			return rep;
		}
		LinkedHashMap<String, Double> map = oandaRatesManager.getExchangeRate(reqMsg.getBase());
		if (map.isEmpty()) {
			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
			rep.setMessage("Failed to get exchange rate");
			return rep;
		}
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		rep.setBase(reqMsg.getBase());
		rep.setRateUpdateTime(oandaRatesManager.getExchangeRateUpdateDate());
		rep.setExchangeRates(map);
		return rep;
	}

	@ApiOperation(value = "获取Goldpay汇率")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/exchange/getGoldRate")
	public @ResponseBody GetExchangeRateResponse getExchangeRate(@PathVariable String token) {

		GetExchangeRateResponse rep = new GetExchangeRateResponse();

		LinkedHashMap<String, Double> result = oandaRatesManager
				.getExchangeRateDiffLeft4OneRight(ServerConsts.CURRENCY_OF_GOLDPAY);

		result.remove(ServerConsts.CURRENCY_OF_CNY);

		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		rep.setBase(ServerConsts.CURRENCY_OF_GOLDPAY);
		rep.setRateUpdateTime(oandaRatesManager.getExchangeRateUpdateDate());
		rep.setExchangeRates(result);

		return rep;

	}
	
	@ApiOperation(value = "获取Goldpay汇率")
	@RequestMapping(method = RequestMethod.POST, value = "/exchange/getGoldRate4Page")
	public @ResponseBody GetExchangeRateResponse getGoldRate4Page() {

		GetExchangeRateResponse rep = new GetExchangeRateResponse();

		LinkedHashMap<String, Double> result = oandaRatesManager
				.getExchangeRateDiffLeft4OneRight(ServerConsts.CURRENCY_OF_GOLDPAY);
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		rep.setBase(ServerConsts.CURRENCY_OF_GOLDPAY);
		rep.setRateUpdateTime(oandaRatesManager.getExchangeRateUpdateDate());
		rep.setExchangeRates(result);

		return rep;

	}

	@ApiOperation(value = "兑换确认")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/exchange/exchangeConfirm")
	public @ResponseEncryptBody ExchangeConfirmResponse exchangeConfirm(@PathVariable String token,
			@RequestDecryptBody ExchangeConfirmRequest reqMsg) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		ExchangeConfirmResponse rep = new ExchangeConfirmResponse();
		HashMap<String, String> result = exchangeManager.exchangeConfirm(sessionData.getUserId(),
				reqMsg.getCurrencyOut(), reqMsg.getCurrencyIn(),
				new BigDecimal(Double.toString(reqMsg.getAmountOut())));

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
	public @ResponseEncryptBody GetExchangeHistoryResponse getExchangeHistory(@PathVariable String token,
			@RequestDecryptBody GetExchangeHistoryRequest reqMsg) {
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
			dto.setExchangeId(exchange.getExchangeId());
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

	@ApiOperation(value = "获取兑换详细内容")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/exchange/getExchangeDetails")
	public @ResponseEncryptBody GetExchangeDetailsResponse getExchangeDetails(@PathVariable String token,
			@RequestDecryptBody GetExchangeDetailsRequest reqMsg) {

		GetExchangeDetailsResponse rep = new GetExchangeDetailsResponse();
		Exchange exchange = exchangeManager.getExchangeById(reqMsg.getExchangeId());
		if (exchange == null) {
			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
			rep.setMessage(MessageConsts.RET_CODE_FAILUE);
			return rep;
		}

		rep.setCurrencyOut(exchange.getCurrencyOut());
		rep.setAmountOut(exchange.getAmountOut());
		rep.setCurrencyOutUnit(commonManager.getCurreny(exchange.getCurrencyOut()).getCurrencyUnit());
		rep.setCurrencyIn(exchange.getCurrencyIn());
		rep.setAmountIn(exchange.getAmountIn());
		rep.setCurrencyInUnit(commonManager.getCurreny(exchange.getCurrencyIn()).getCurrencyUnit());

		rep.setExchangeId(exchange.getExchangeId());
		rep.setCreateTime(exchange.getCreateTime());

		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);

		return rep;

	}

}
