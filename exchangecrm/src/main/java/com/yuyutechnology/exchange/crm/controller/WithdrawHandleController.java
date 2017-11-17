/**
 * 
 */
package com.yuyutechnology.exchange.crm.controller;

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

import com.yuyutechnology.exchange.crm.request.GetWithdrawByPageRequest;
import com.yuyutechnology.exchange.manager.WithdrawManager;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.CrmLogManager;
import com.yuyutechnology.exchange.util.page.PageBean;

/**
 * @author suzan.wu
 *
 */
@Controller
public class WithdrawHandleController {
	private static Logger logger = LogManager.getLogger(WithdrawHandleController.class);

	@Autowired
	WithdrawManager withdrawManager;
	@Autowired
	CommonManager commonManager;
	@Autowired
	CrmLogManager crmLogManager;

	/**
	 * 获取提现列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getWithdrawByPage", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public PageBean getWithdrawByPage(@RequestBody GetWithdrawByPageRequest getWithdrawByPageRequest,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info(getWithdrawByPageRequest.toString());
		return withdrawManager.getWithdrawByPage(Integer.parseInt(getWithdrawByPageRequest.getCurrentPage()),
				getWithdrawByPageRequest.getUserPhone(), getWithdrawByPageRequest.getUserName(),
				getWithdrawByPageRequest.getStartTime(), getWithdrawByPageRequest.getEndTime());

	}

//	/**
//	 * 新增活动
//	 * 
//	 * @param addWithdrawRequest
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/addWithdraw", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
//	public BaseResponse addWithdraw(@RequestBody AddWithdrawRequest addWithdrawRequest, HttpServletRequest request,
//			HttpServletResponse response) {
//		BaseResponse rep = new BaseResponse();
//		if (addWithdrawRequest.Empty()) {
//			logger.info("parameter is empty");
//			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
//		} else {
//			logger.info("add Withdraw : {}", addWithdrawRequest.toString());
//
//			withdrawManager.addWithdraw(DateFormatUtils.fromString(addWithdrawRequest.getStartTime()),
//					DateFormatUtils.fromString(addWithdrawRequest.getEndTime()),
//					new BigDecimal(addWithdrawRequest.getWithdrawBudget()),
//					new BigDecimal(addWithdrawRequest.getInviterBonus()),
//					new BigDecimal(addWithdrawRequest.getInviteeBonus()));
//
//			crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
//					Operation.ADD_CAMPAIGN.getOperationName(), addWithdrawRequest.toString()));
//			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
//		}
//		return rep;
//	}
//
//	/**
//	 * 开启活动
//	 * 
//	 * @param withdrawRequest
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/openWithdraw", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
//	public BaseResponse openWithdraw(@RequestBody WithdrawRequest withdrawRequest, HttpServletRequest request,
//			HttpServletResponse response) {
//		BaseResponse rep = new BaseResponse();
//
//		logger.info("open Withdraw : {}", withdrawRequest.getWithdrawId());
//
//		Integer activeWithdrawId = withdrawManager.openWithdraw(withdrawRequest.getWithdrawId());
//		if (withdrawRequest.getWithdrawId() == activeWithdrawId) {
//			crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
//					Operation.ON_CAMPAIGN.getOperationName(), withdrawRequest.getWithdrawId().toString()));
//			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
//		} else {
//			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
//			rep.setMessage(activeWithdrawId.toString());
//		}
//		return rep;
//	}
//
//	/**
//	 * 关闭活动
//	 * 
//	 * @param withdrawRequest
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/closeWithdraw", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
//	public BaseResponse closeWithdraw(@RequestBody WithdrawRequest withdrawRequest, HttpServletRequest request,
//			HttpServletResponse response) {
//		BaseResponse rep = new BaseResponse();
//
//		logger.info("close Withdraw : {}", withdrawRequest.getWithdrawId());
//
//		withdrawManager.closeWithdraw(withdrawRequest.getWithdrawId());
//
//		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
//				Operation.OFF_CAMPAIGN.getOperationName(), withdrawRequest.getWithdrawId().toString()));
//
//		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
//		return rep;
//	}
//
//	/**
//	 * 修改奖励金
//	 * 
//	 * @param changeBonusRequest
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/changeBonus", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
//	public BaseResponse changeBonus(@RequestBody ChangeBonusRequest changeBonusRequest, HttpServletRequest request,
//			HttpServletResponse response) {
//		BaseResponse rep = new BaseResponse();
//
//		logger.info("change Bonus : {}", changeBonusRequest.toString());
//
//		withdrawManager.changeBonus(changeBonusRequest.getWithdrawId(), changeBonusRequest.getInviterBonus(),
//				changeBonusRequest.getInviteeBonus());
//
//		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
//				Operation.CHANGE_BONUS.getOperationName(), changeBonusRequest.toString()));
//		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
//
//		return rep;
//	}
//
//	/**
//	 * 追加预算
//	 * 
//	 * @param addBudgetRequest
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/addBudget", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
//	public BaseResponse addBudget(@RequestBody AddBudgetRequest addBudgetRequest, HttpServletRequest request,
//			HttpServletResponse response) {
//		BaseResponse rep = new BaseResponse();
//
//		logger.info("add Budget : {}", addBudgetRequest.toString());
//
//		withdrawManager.additionalBudget(addBudgetRequest.getWithdrawId(), addBudgetRequest.getAdditionalBudget());
//
//		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
//				Operation.ADDITIONAL_BUDGET.getOperationName(), addBudgetRequest.toString()));
//		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
//		return rep;
//	}

}
