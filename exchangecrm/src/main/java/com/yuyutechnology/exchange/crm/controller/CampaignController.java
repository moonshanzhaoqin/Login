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
import com.yuyutechnology.exchange.crm.request.AddBudgetRequest;
import com.yuyutechnology.exchange.crm.request.AddCampaignRequest;
import com.yuyutechnology.exchange.crm.request.CampaignRequest;
import com.yuyutechnology.exchange.crm.request.ChangeBonusRequest;
import com.yuyutechnology.exchange.crm.request.GetCampaignListRequest;
import com.yuyutechnology.exchange.enums.Operation;
import com.yuyutechnology.exchange.manager.CampaignManager;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.CrmLogManager;
import com.yuyutechnology.exchange.pojo.CrmLog;
import com.yuyutechnology.exchange.util.DateFormatUtils;
import com.yuyutechnology.exchange.util.page.PageBean;

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

	/**
	 * 获取活动列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCampaignList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public PageBean getCampaignList(@RequestBody GetCampaignListRequest getCampaignListRequest, HttpServletRequest request, HttpServletResponse response) {

		return campaignManager.getCampaignList(getCampaignListRequest.getCurrentPage());

	}

	/**
	 * 新增活动
	 * 
	 * @param addCampaignRequest
	 * @param request
	 * @param response
	 * @return
	 */
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

	/**
	 * 开启活动
	 * @param campaignRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/openCampaign", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse openCampaign(@RequestBody CampaignRequest campaignRequest, HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();

		logger.info("open Campaign : {}", campaignRequest.getCampaignId());

		Integer activeCampaignId = campaignManager.openCampaign(campaignRequest.getCampaignId());
		if (campaignRequest.getCampaignId() == activeCampaignId) {
			crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
					Operation.ON_CAMPAIGN.getOperationName(), campaignRequest.getCampaignId().toString()));
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		} else {
			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
			rep.setMessage(activeCampaignId.toString());
		}
		return rep;
	}

	// TODO 关闭活动
	@ResponseBody
	@RequestMapping(value = "/closeCampaign", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse closeCampaign(@RequestBody CampaignRequest campaignRequest, HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();

		logger.info("close Campaign : {}", campaignRequest.getCampaignId());

		campaignManager.closeCampaign(campaignRequest.getCampaignId());

		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
				Operation.OFF_CAMPAIGN.getOperationName(), campaignRequest.getCampaignId().toString()));

		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		return rep;
	}

	/**
	 * 修改奖励金
	 * 
	 * @param changeBonusRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/changeBonus", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse changeBonus(@RequestBody ChangeBonusRequest changeBonusRequest, HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();

		logger.info("change Bonus : {}", changeBonusRequest.toString());

		campaignManager.changeBonus(changeBonusRequest.getCampaignId(), changeBonusRequest.getInviterBonus(),
				changeBonusRequest.getInviteeBonus());

		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
				Operation.CHANGE_BONUS.getOperationName(), changeBonusRequest.toString()));
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);

		return rep;
	}

	/**
	 * 追加预算
	 * 
	 * @param addBudgetRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addBudget", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse addBudget(@RequestBody AddBudgetRequest addBudgetRequest, HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();

		logger.info("add Budget : {}", addBudgetRequest.toString());

		campaignManager.additionalBudget(addBudgetRequest.getCampaignId(), addBudgetRequest.getAdditionalBudget());

		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
				Operation.ADDITIONAL_BUDGET.getOperationName(), addBudgetRequest.toString()));
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		return rep;
	}

}
