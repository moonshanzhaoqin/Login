/**
 * 
 */
package com.yuyutechnology.exchange.crm.controller;

import java.math.BigDecimal;
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
import com.yuyutechnology.exchange.crm.request.AddBudgetRequest;
import com.yuyutechnology.exchange.crm.request.AddCampaignRequest;
import com.yuyutechnology.exchange.crm.request.ChangeBounsRequest;
import com.yuyutechnology.exchange.enums.Operation;
import com.yuyutechnology.exchange.manager.CampaignManager;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.CrmLogManager;
import com.yuyutechnology.exchange.pojo.Campaign;
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

	// TODO 获取活动
	@ResponseBody
	@RequestMapping(value = "/getCampaignList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<Campaign> getCampaignList(HttpServletRequest request, HttpServletResponse response) {

		return campaignManager.getCampaignList();

	}

	// TODO 新增活动
	@ResponseBody
	@RequestMapping(value = "/addCampaign", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse addCampaign(@RequestBody AddCampaignRequest addCampaignRequest, HttpServletRequest request,
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
	@ResponseBody
	@RequestMapping(value = "/changeBouns", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse changeBouns(@RequestBody ChangeBounsRequest changeBounsRequest, HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();

		logger.info("change Bouns : {}", changeBounsRequest.toString());

		campaignManager.changeBouns(changeBounsRequest.getCampaignId(),changeBounsRequest.getInviterBonus(),changeBounsRequest.getInviteeBonus());

		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
				Operation.CHANGE_BOUNS.getOperationName(), changeBounsRequest.toString()));
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);

		return rep;
	}

	// TODO 追加预算
	@ResponseBody
	@RequestMapping(value = "/addBudget", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse addBudget(@RequestBody AddBudgetRequest addBudgetRequest,
			HttpServletRequest request, HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();

		logger.info("add Budget : {}", addBudgetRequest.toString());

		campaignManager.additionalBudget(addBudgetRequest.getCampaignId(),addBudgetRequest.getAdditionalBudget());

		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
				Operation.ADDITIONAL_BUDGET.getOperationName(), addBudgetRequest.toString()));
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		return rep;
	}

}
