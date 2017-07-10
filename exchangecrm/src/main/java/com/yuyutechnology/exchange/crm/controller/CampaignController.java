/**
 * 
 */
package com.yuyutechnology.exchange.crm.controller;

import java.math.BigDecimal;
import java.util.Date;

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
import com.yuyutechnology.exchange.crm.request.AddCampaignRequest;
import com.yuyutechnology.exchange.crm.request.AddCurrencyRequest;
import com.yuyutechnology.exchange.enums.Operation;
import com.yuyutechnology.exchange.manager.CampaignManager;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.CrmLogManager;
import com.yuyutechnology.exchange.pojo.CrmLog;
import com.yuyutechnology.exchange.util.DateFormatUtils;

/**
 * @author suzan.wu
 *
 */
@Controller
public class CampaignController {
	private static Logger logger = LogManager.getLogger(CampaignController.class);

	@Autowired
	CampaignManager campaignManager;
	@Autowired
	CommonManager commonManager;
	@Autowired
	CrmLogManager crmLogManager;

	// TODO 新增活动
	@ResponseBody
	@RequestMapping(value = "/addCampaign", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse addCurrency(@RequestBody AddCampaignRequest addCampaignRequest, HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		if (addCampaignRequest.Empty()) {
			logger.info("parameter is empty");
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
		} else {
			logger.info("add Campaign : {}", addCampaignRequest.toString());

			campaignManager.addCampaign(DateFormatUtils.fromString(addCampaignRequest.getStartTime()),
					DateFormatUtils.fromString(addCampaignRequest.getEndTime()),
					new BigDecimal(addCampaignRequest.getCampaignBudget()),
					new BigDecimal(addCampaignRequest.getInviterBonus()),
					new BigDecimal(addCampaignRequest.getInviteeBonus()));

			crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
					Operation.ADD_CAMPAIGN.getOperationName(), addCampaignRequest.toString()));
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		}
		return rep;
	}

	// TODO 开启关闭活动
	// TODO 修改奖励金
	// TODO 追加预算

}
