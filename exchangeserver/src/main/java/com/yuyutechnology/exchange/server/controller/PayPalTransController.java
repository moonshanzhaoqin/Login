package com.yuyutechnology.exchange.server.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

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
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.manager.PayPalTransManager;
import com.yuyutechnology.exchange.server.controller.request.PaypalTransConfirmRequest;
import com.yuyutechnology.exchange.server.controller.request.PaypalTransInitRequest;
import com.yuyutechnology.exchange.server.controller.response.GetExchangeRate4GDQResponse;
import com.yuyutechnology.exchange.server.controller.response.PaypalTransConfirmResponse;
import com.yuyutechnology.exchange.server.controller.response.PaypalTransInitResponse;
import com.yuyutechnology.exchange.server.security.annotation.RequestDecryptBody;
import com.yuyutechnology.exchange.server.security.annotation.ResponseEncryptBody;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionDataHolder;

@Controller
public class PayPalTransController {

	@Autowired
	OandaRatesManager oandaRatesManager;
	@Autowired
	PayPalTransManager payPalTransManager;
	@Autowired
	ConfigManager configManager;

	public static Logger logger = LogManager.getLogger(PayPalTransController.class);

	@ApiOperation(value = "paypal交易  获取汇率")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/paypalTrans/getExchangeRate4GDQ")
	public @ResponseEncryptBody GetExchangeRate4GDQResponse getExchangeRate4GDQ(@PathVariable String token) {

		GetExchangeRate4GDQResponse rep = new GetExchangeRate4GDQResponse();

		Double max = configManager.getConfigDoubleValue(ConfigKeyEnum.PAYPALMAXLIMITEACHTIME, 100000000d);
		Double mini = configManager.getConfigDoubleValue(ConfigKeyEnum.PAYPALMINILIMITEACHTIME, 100d);

		logger.info("Maximum amount of single transaction : {}", max);
		logger.info("The minimum amount of a single transaction : {}", mini);

		HashMap<String, Double> result = oandaRatesManager
				.getExchangeRateDiffLeft4OneRight(ServerConsts.CURRENCY_OF_GOLDPAY);
		Date updateDate = oandaRatesManager.getExchangeRateUpdateDate();
		result.remove(ServerConsts.CURRENCY_OF_CNY);

		rep.setRates(result);
		rep.setUpdateDate(updateDate);
		rep.setOpts(new String[] { mini.toString(), max.toString() });
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);

		return rep;
	}

	@ApiOperation(value = "paypal交易 订单初始化")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/paypalTrans/paypalTransInit")
	public @ResponseEncryptBody PaypalTransInitResponse paypalTransInit(@PathVariable String token,
			@RequestDecryptBody PaypalTransInitRequest reqMsg) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		PaypalTransInitResponse rep = new PaypalTransInitResponse();

		Double max = configManager.getConfigDoubleValue(ConfigKeyEnum.PAYPALMAXLIMITEACHTIME, 100000000d);
		Double mini = configManager.getConfigDoubleValue(ConfigKeyEnum.PAYPALMINILIMITEACHTIME, 100d);

		logger.info("Maximum amount of single transaction : {}", max);
		logger.info("The minimum amount of a single transaction : {}", mini);

		// 判断条件.币种合法，GDQ数量为整数且大于
		if (reqMsg.getAmount() == null
				|| ((reqMsg.getAmount().doubleValue() < mini || reqMsg.getAmount().doubleValue() > max)
						|| reqMsg.getAmount().longValue() % 1 > 0)) {
			logger.warn("The number of inputs does not meet the requirements");
			rep.setRetCode(RetCodeConsts.TRANSFER_PAYPALTRANS_ILLEGAL_DATA);
			rep.setMessage("The number of inputs does not meet the requirements");
			rep.setOpts(new String[] { mini.toString(), max.toString() });
			return rep;
		}

		HashMap<String, Object> result = payPalTransManager.paypalTransInit(sessionData.getUserId(),
				reqMsg.getCurrency(), reqMsg.getAmount());
		if (RetCodeConsts.EXCHANGE_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY.equals(result.get("retCode"))) {
			rep.setRetCode(RetCodeConsts.EXCHANGE_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY);
			rep.setMessage((String) result.get("msg"));
			return rep;
		}

		if (RetCodeConsts.TRANSFER_PAYPALTRANS_TOTAL_AMOUNT_OF_GDQ.equals(result.get("retCode"))) {
			rep.setRetCode(RetCodeConsts.TRANSFER_PAYPALTRANS_TOTAL_AMOUNT_OF_GDQ);
			rep.setMessage((String) result.get("msg"));
			return rep;
		}

		rep.setTransId((String) result.get("transId"));
		rep.setAccessToken((String) result.get("token"));
		rep.setCurrency(reqMsg.getCurrency());
		rep.setAmount(((BigDecimal) result.get("amount")).doubleValue());
		rep.setUnit((String) result.get("unit"));

		rep.setCreateAt(oandaRatesManager.getExchangeRateUpdateDate());
		rep.setExpiration((long) result.get("expiration"));

		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);

		return rep;
	}

	@ApiOperation(value = "paypal交易  交易确认")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/paypalTrans/paypalTransConfirm")
	public @ResponseEncryptBody PaypalTransConfirmResponse paypalTransConfirm(@PathVariable String token,
			@RequestDecryptBody PaypalTransConfirmRequest reqMsg) {

		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		PaypalTransConfirmResponse rep = new PaypalTransConfirmResponse();

		HashMap<String, String> result = payPalTransManager.paypalTransConfirm(sessionData.getUserId(),
				reqMsg.getTransId(), reqMsg.getPaymentMethodNonce());

		rep.setRetCode(result.get("retCode"));
		rep.setMessage(result.get("msg"));

		rep.setTransId(reqMsg.getTransId());

		return rep;

	}

}
