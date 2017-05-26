package com.yuyutechnology.exchange.crm.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.crm.reponse.BaseResponse;
import com.yuyutechnology.exchange.crm.request.AddCurrencyRequest;
import com.yuyutechnology.exchange.crm.request.ChangeCurrencyStatusRequest;
import com.yuyutechnology.exchange.enums.Operation;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.CrmLogManager;
import com.yuyutechnology.exchange.manager.CurrencyManager;
import com.yuyutechnology.exchange.pojo.CrmLog;
import com.yuyutechnology.exchange.pojo.Currency;

@Controller
public class CurrencyController {
	private static Logger logger = LogManager.getLogger(CurrencyController.class);

	@Autowired
	CurrencyManager currencyManager;
	@Autowired
	CommonManager commonManager;
	@Autowired
	CrmLogManager crmLogManager;

	/**
	 * 获取货币列表 getCurrencyList
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCurrencyList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<Currency> getCurrencyList(HttpServletRequest request, HttpServletResponse response) {
		return currencyManager.getCurrencyList();
	}

	/**
	 * 保存货币信息 updateCurrency
	 * @param currency
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateCurrency", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse updateCurrency(@RequestBody Currency currency, HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		currencyManager.updateCurrency(currency);
		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
				Operation.EDIT_CURRENCY.getOperationName(),currency.toString()));		
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		return rep;
	}

	/**
	 * 获取待添加货币列表
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAddingCurrencyList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<String> getAddingCurrencyList(HttpServletRequest request, HttpServletResponse response) {
		return commonManager.getAllConfigurableCurrencies();
	}

	/**
	 * 添加新币种
	 * @param addCurrencyRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addCurrency", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse addCurrency(@RequestBody AddCurrencyRequest addCurrencyRequest, HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		if (addCurrencyRequest.Empty()) {
			logger.info("parameter is empty");
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
		} else {
			logger.info("add {}", addCurrencyRequest.getCurrency());
			String retCode = currencyManager.addCurrency(addCurrencyRequest.getCurrency());
			crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
					Operation.ADD_CURRENCY.getOperationName(),addCurrencyRequest.getCurrency()));		
			rep.setRetCode(retCode);
		}
		return rep;
	}

	/**
	 * 修改币种状态
	 * @param changeCurrencyStatusRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/changeCurrencyStatus", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse changeCurrencyStatus(@RequestBody ChangeCurrencyStatusRequest changeCurrencyStatusRequest,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("adminName=",(String) request.getSession().getAttribute("adminName"));
		BaseResponse rep = new BaseResponse();
		if (changeCurrencyStatusRequest.isEmpty()) {
			logger.info("parameter is empty");
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
		} else {
			logger.info("change {} Status {}", changeCurrencyStatusRequest.getCurrency(),
					changeCurrencyStatusRequest.getStatus());
			String retCode = currencyManager.changeCurrencyStatus(changeCurrencyStatusRequest.getCurrency(),
					changeCurrencyStatusRequest.getStatus());
			if (changeCurrencyStatusRequest.getStatus()==ServerConsts.CURRENCY_AVAILABLE) {
				crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
						Operation.ON_CURRENCY.getOperationName(),changeCurrencyStatusRequest.getCurrency()));		
			}else{
				crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
						Operation.OFF_CURRENCY.getOperationName(),changeCurrencyStatusRequest.getCurrency()));	
			}
			rep.setRetCode(retCode);
		}
		return rep;
	}

}
