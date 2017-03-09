package com.yuyutechnology.exchange.crm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.GoldpayTransManager;
import com.yuyutechnology.exchange.pojo.Withdraw;

@Controller
public class WithdrawController {
	private static Logger logger = LogManager.getLogger(WithdrawController.class);

	@Autowired
	GoldpayTransManager goldpayTransManager;
	@Autowired
	CommonManager commonManager;

	// TODO 获取提现列表 getWithdrawList
	@ResponseBody
	@RequestMapping(value = "/getWithdrawList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<Withdraw> getWithdrawList(HttpServletRequest request, HttpServletResponse response) {
		return goldpayTransManager.getWithdrawList();
	}

//	// TODO 保存货币信息 updateCurrency
//	@ResponseBody
//	@RequestMapping(value = "/updateCurrency", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
//	public BaseResponse updateCurrency(@RequestBody Currency currency, HttpServletRequest request,
//			HttpServletResponse response) {
//		BaseResponse rep = new BaseResponse();
//		logger.info("{}", currency.toString());
//		currencyManager.updateCurrency(currency);
//		logger.info("ok");
//		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
//		return rep;
//	}
//
//	// TODO 获取待添加货币列表
//	@ResponseBody
//	@RequestMapping(value = "/getAddingCurrencyList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
//	public List<String> getAddingCurrencyList(HttpServletRequest request, HttpServletResponse response) {
//		return commonManager.getAllConfigurableCurrencies();
//	}
//
//	// TODO 添加新币种
//	@ResponseBody
//	@RequestMapping(value = "/addCurrency", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
//	public BaseResponse addCurrency(@RequestBody AddCurrencyRequest addCurrencyRequest, HttpServletRequest request,
//			HttpServletResponse response) {
//		BaseResponse rep = new BaseResponse();
//		if (addCurrencyRequest.isEmpty()) {
//			logger.info("parameter is empty");
//			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
//		} else {
//			logger.info("add {}", addCurrencyRequest.getCurrency());
//			String retCode = currencyManager.addCurrency(addCurrencyRequest.getCurrency());
//			rep.setRetCode(retCode);
//		}
//		return rep;
//	}
//
//	// TODO 修改币种状态
//	@ResponseBody
//	@RequestMapping(value = "/changeCurrencyStatus", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
//	public BaseResponse changeCurrencyStatus(@RequestBody ChangeCurrencyStatusRequest changeCurrencyStatusRequest,
//			HttpServletRequest request, HttpServletResponse response) {
//		BaseResponse rep = new BaseResponse();
//		if (changeCurrencyStatusRequest.isEmpty()) {
//			logger.info("parameter is empty");
//			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
//		} else {
//			logger.info("change {} Status {}", changeCurrencyStatusRequest.getCurrency(),
//					changeCurrencyStatusRequest.getStatus());
//			String retCode = currencyManager.changeCurrencyStatus(changeCurrencyStatusRequest.getCurrency(),
//					changeCurrencyStatusRequest.getStatus());
//			rep.setRetCode(retCode);
//		}
//		return rep;
//	}

}
