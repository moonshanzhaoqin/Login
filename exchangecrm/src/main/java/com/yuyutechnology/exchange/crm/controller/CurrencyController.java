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
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.crm.request.CurrencyRequest;
import com.yuyutechnology.exchange.manager.CurrencyManager;
import com.yuyutechnology.exchange.pojo.Currency;

@Controller
public class CurrencyController {
	private static Logger logger = LoggerFactory.getLogger(CurrencyController.class);

	@Autowired
	CurrencyManager currencyManager;

	// TODO 启用货币 enableCurrency
//	@ResponseBody
//	@RequestMapping(value = "/enableCurrency", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
//	public String enableCurrency(CurrencyRequest currencyRequest) {
//
//		int retCode = currencyManager.enableCurrency(currencyRequest.getCurrency());
//		switch (retCode) {
//		case RetCodeConsts.CURRENCY_NOT_EXIST:
//
//			break;
//		case RetCodeConsts.CURRENCY_HAS_BEEN_AVAILABLE:
//
//			break;
//		case RetCodeConsts.SUCCESS:
//
//			break;
//		default:
//			break;
//		}
//		return null;
//	}

	// TODO 禁用货币 disableCurrency
//	@ResponseBody
//	@RequestMapping(value = "/disableCurrency", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
//	public String disableCurrency(CurrencyRequest currencyRequest) {
//
//		int retCode = currencyManager.disableCurrency(currencyRequest.getCurrency());
//		switch (retCode) {
//		case RetCodeConsts.CURRENCY_NOT_EXIST:
//
//			break;
//		case RetCodeConsts.CURRENCY_HAS_BEEN_UNAVAILABLE:
//
//			break;
//		case RetCodeConsts.SUCCESS:
//
//			break;
//		default:
//			break;
//		}
//		return null;
//	}

	// TODO 获取货币列表 getCurrencyList
	@ResponseBody
	@RequestMapping(value = "/getCurrencyList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<Currency> getCurrencyList(HttpServletRequest request, HttpServletResponse response) {
		return currencyManager.getCurrencyList();
	}

	// TODO 保存货币信息 updateCurrency
<<<<<<< .mine
	@RequestMapping(value = "/updateCurrency", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String updateCurrency(@RequestBody CurrencyRequest currencyRequest, HttpServletRequest request,
			HttpServletResponse response) {

=======
	@ResponseBody
	// @RequestMapping(value = "/updateCurrency", method = RequestMethod.POST)
	@RequestMapping(value = "/updateCurrency", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public void updateCurrency(@RequestBody CurrencyRequest currencyRequest, HttpServletRequest request) {
>>>>>>> .theirs

		logger.info("{}", currencyRequest.toString());
//		currencyManager.updateCurrency(currencyRequest.getCurrency(), currencyRequest.getNameCn(),
//				currencyRequest.getNameEn(), currencyRequest.getNameHk(), currencyRequest.getCurrencyUnit(),
//				currencyRequest.getCurrencyStatus(), currencyRequest.getCurrencyOrder());

	}

}
