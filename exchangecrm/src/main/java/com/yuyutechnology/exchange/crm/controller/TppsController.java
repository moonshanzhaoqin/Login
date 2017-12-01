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
import com.yuyutechnology.exchange.crm.reponse.BaseResponse;
import com.yuyutechnology.exchange.crm.request.AddGoldqPayClientRequset;
import com.yuyutechnology.exchange.crm.request.GetExUserRequest;
import com.yuyutechnology.exchange.crm.request.GetGoldqPayClientByPageRequest;
import com.yuyutechnology.exchange.crm.request.GetGoldqPayFeeRequest;
import com.yuyutechnology.exchange.crm.request.UpdateGoldqPayClientRequest;
import com.yuyutechnology.exchange.crm.tpps.manager.TppsManager;
import com.yuyutechnology.exchange.crm.tpps.pojo.GoldqPayClient;
import com.yuyutechnology.exchange.crm.tpps.pojo.GoldqPayFee;
import com.yuyutechnology.exchange.enums.Operation;
import com.yuyutechnology.exchange.manager.CrmLogManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.CrmLog;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.util.page.PageBean;

@Controller
public class TppsController {
	private static Logger logger = LogManager.getLogger(TppsController.class);
	@Autowired
	CrmLogManager crmLogManager;
	@Autowired
	UserManager userManager;
	@Autowired
	TppsManager tppsManager;

	@ResponseBody
	@RequestMapping(value = "/addGoldqPayClient", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse addGoldqPayClient(@RequestBody AddGoldqPayClientRequset addGoldqPayClientRequset,
			HttpServletRequest request, HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		logger.info("addGoldqPayClient({})", addGoldqPayClientRequset.toString());
		Integer exId = userManager.getUserId(addGoldqPayClientRequset.getAreaCode(),
				addGoldqPayClientRequset.getUserPhone());
		if (exId == null) {
			rep.setRetCode(RetCodeConsts.PHONE_NOT_EXIST);
		} else if (exId == 0) {
			rep.setRetCode(RetCodeConsts.USER_BLOCKED);
		} else {
			userManager.updatePayToken(exId,
					addGoldqPayClientRequset.getUserPayToken());
			tppsManager.addGoldqPayClient(exId, addGoldqPayClientRequset.getName(),
					addGoldqPayClientRequset.getRedirectUrl(), addGoldqPayClientRequset.getCustomDomain());
			crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
					Operation.ADD_GOLDQPAYCLIENT.getOperationName(), addGoldqPayClientRequset.toString()));
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		}

		return rep;
	}

	@ResponseBody
	@RequestMapping(value = "/getExUser", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public GetExUserResponse getExUser(@RequestBody GetExUserRequest getExUserRequest, HttpServletRequest request,
			HttpServletResponse response) {
		GetExUserResponse rep = new GetExUserResponse();
		logger.info("getExUser({})", getExUserRequest.toString());
		User user = userManager.getUserById(getExUserRequest.getExId());
		rep.setAreaCode(user.getAreaCode());
		rep.setUserPhone(user.getUserPhone());
		rep.setUserName(user.getUserName());
		rep.setUserPayToken(user.getUserPayToken());
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		return rep;
	}

	@ResponseBody
	@RequestMapping(value = "/updateGoldqPayClient", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse updateGoldqPayClient(@RequestBody UpdateGoldqPayClientRequest updateGoldqPayClientRequest,
			HttpServletRequest request, HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		logger.info("updateGoldqPayClient({})", updateGoldqPayClientRequest.toString());

		userManager.updatePayToken(updateGoldqPayClientRequest.getExId(),
				updateGoldqPayClientRequest.getUserPayToken());

		tppsManager.updateGoldqPayClient(updateGoldqPayClientRequest.getClientId(),
				updateGoldqPayClientRequest.getName(), updateGoldqPayClientRequest.getRedirectUrl(),
				updateGoldqPayClientRequest.getCustomDomain());

		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
				Operation.UPDATE_GOLDQPAYCLIENT.getOperationName(), updateGoldqPayClientRequest.toString()));

		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		return rep;
	}

	@ResponseBody
	@RequestMapping(value = "/updateGoldqPayFee", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse updateGoldqPayFee(@RequestBody GoldqPayFee goldqPayFee, HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		logger.info("updategoldqPayClient({})", goldqPayFee.toString());
		tppsManager.updateGoldqPayFee(goldqPayFee);
		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
				Operation.UPDATE_GOLDQPAYFEE.getOperationName(), goldqPayFee.toString()));
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		return rep;
	}

	@ResponseBody
	@RequestMapping(value = "/getGoldqPayClientByPage", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public PageBean getGoldqPayClientByPage(@RequestBody GetGoldqPayClientByPageRequest getGoldqPayClientByPageRequest,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info(getGoldqPayClientByPageRequest.toString());
		return tppsManager.getGoldqPayClientByPage(Integer.parseInt(getGoldqPayClientByPageRequest.getCurrentPage()));

	}

	@ResponseBody
	@RequestMapping(value = "/getGoldqPayFee", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<GoldqPayFee> getGoldqPayFee(@RequestBody GetGoldqPayFeeRequest getGoldqPayFeeRequest) {
		logger.info("getGoldqPayFee({})", getGoldqPayFeeRequest.getClientId());
		return tppsManager.getGoldqPayFeeByClientId(getGoldqPayFeeRequest.getClientId());
	}
}
