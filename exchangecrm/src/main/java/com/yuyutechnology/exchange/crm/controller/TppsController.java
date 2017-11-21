package com.yuyutechnology.exchange.crm.controller;

import java.util.Date;

import javax.annotation.Resource;
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
import com.yuyutechnology.exchange.crm.request.AddPayClientRequset;
import com.yuyutechnology.exchange.crm.tpps.manager.TppsMananger;
import com.yuyutechnology.exchange.enums.Operation;
import com.yuyutechnology.exchange.manager.CrmLogManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.CrmLog;
import com.yuyutechnology.exchange.pojo.FeeTemplate;

@Controller
public class TppsController {
	private static Logger logger = LogManager.getLogger(TppsController.class);
	@Autowired
	CrmLogManager crmLogManager;
	@Autowired
	UserManager userManager;
	@Autowired
	TppsMananger tppsMananger;
	// TODO add new pay client

	@ResponseBody
	@RequestMapping(value = "/addPayClient", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse addPayClient(@RequestBody AddPayClientRequset addPayClientRequset, HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		logger.info("addPayClient({})", addPayClientRequset.toString());
		Integer exId = userManager.getUserId(addPayClientRequset.getAreaCode(), addPayClientRequset.getUserPhone());
		if (exId == null || exId == 0) {
			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
		} else {
			tppsMananger.addPayClient(exId);
			
			

			crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
					Operation.ADD_PAY_CLIENT.getOperationName(), addPayClientRequset.toString()));
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		}
		return rep;
	}

	// TODO edit pay client
	// TODO add new feeTemplate
	// TODO edit feeTemplate
	// TODO list pay client ,search by page
	// TODO search feeTemplate by client

}
