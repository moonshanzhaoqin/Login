package com.yuyutechnology.exchange.crm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.crm.reponse.BaseResponse;
import com.yuyutechnology.exchange.crm.request.AddCurrencyRequest;
import com.yuyutechnology.exchange.crm.request.CurrencyRequest;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.CurrencyManager;
import com.yuyutechnology.exchange.pojo.Currency;

@Controller
public class CurrencyController {
	private static Logger logger = LoggerFactory.getLogger(CurrencyController.class);

	@Autowired
	CurrencyManager currencyManager;
	@Autowired
	CommonManager commonManager;

	// TODO 获取货币列表 getCurrencyList
	@ResponseBody
	@RequestMapping(value = "/getCurrencyList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<Currency> getCurrencyList(HttpServletRequest request, HttpServletResponse response) {
		return currencyManager.getCurrencyList();
	}
	// TODO 保存货币信息 updateCurrency
	@ResponseBody
	@RequestMapping(value = "/updateCurrency", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse updateCurrency(@RequestBody Currency currency, HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		logger.info("{}", currency.toString());
		currencyManager.updateCurrency(currency);
		logger.info("ok");
		rep.setRetCode(RetCodeConsts.SUCCESS);
		return rep;
	}
	//TODO 获取待添加货币列表
	@ResponseBody
	@RequestMapping(value = "/getAddingCurrencyList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<String> getAddingCurrencyList(HttpServletRequest request, HttpServletResponse response) {
		return commonManager.getAllConfigurableCurrencies();
	}
	//TODO 添加新币种
	@ResponseBody
	@RequestMapping(value = "/addCurrency", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse addCurrency(@RequestBody AddCurrencyRequest addCurrencyRequest, HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		logger.info("add {}", addCurrencyRequest.getCurrency());
		int retCode=currencyManager.addCurrency(addCurrencyRequest.getCurrency());
		rep.setRetCode(retCode);
		return rep;
	}

}
