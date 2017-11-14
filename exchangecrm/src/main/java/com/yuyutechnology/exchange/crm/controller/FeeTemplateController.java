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
import com.yuyutechnology.exchange.enums.Operation;
import com.yuyutechnology.exchange.manager.CrmLogManager;
import com.yuyutechnology.exchange.manager.FeeManager;
import com.yuyutechnology.exchange.pojo.CrmLog;
import com.yuyutechnology.exchange.pojo.FeeTemplate;

@Controller
public class FeeTemplateController {
	private static Logger logger = LogManager.getLogger(FeeTemplateController.class);
	
	@Autowired
	FeeManager feeManager;
	@Autowired
	CrmLogManager crmLogManager;
	
	@ResponseBody
	@RequestMapping(value = "/listFeeTemplate", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<FeeTemplate> listFeeTemplate() {
		return feeManager.listAllFeeTemplate();
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateFeeTemplate", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse updateFeeTemplate(@RequestBody FeeTemplate feeTemplate,HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		
		logger.info("updateFeeTemplate({})", feeTemplate.toString());
		feeManager.updateFeeTemplate(feeTemplate);
		feeManager.refresh();
		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
				Operation.UPDATE_FEETEMPLATE.getOperationName(), feeTemplate.toString()));
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		return rep;
	}
}
