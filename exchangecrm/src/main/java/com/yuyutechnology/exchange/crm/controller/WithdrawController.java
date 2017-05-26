package com.yuyutechnology.exchange.crm.controller;

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
import com.yuyutechnology.exchange.crm.request.GetWithdrawListRequest;
import com.yuyutechnology.exchange.crm.request.WithdrawRequest;
import com.yuyutechnology.exchange.enums.Operation;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.CrmLogManager;
import com.yuyutechnology.exchange.manager.GoldpayTransManager;
import com.yuyutechnology.exchange.pojo.CrmLog;
import com.yuyutechnology.exchange.util.page.PageBean;

@Controller
public class WithdrawController {
	private static Logger logger = LogManager.getLogger(WithdrawController.class);

	@Autowired
	GoldpayTransManager goldpayTransManager;
	@Autowired
	CommonManager commonManager;
	@Autowired
	CrmLogManager crmLogManager;

	/**
	 * 获取提现列表 getWithdrawList
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getWithdrawList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public PageBean getWithdrawList(@RequestBody GetWithdrawListRequest getWithdrawListRequest,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info(getWithdrawListRequest.toString());
		return goldpayTransManager.getWithdrawList(Integer.parseInt(getWithdrawListRequest.getCurrentPage()),
				getWithdrawListRequest.getUserPhone(), getWithdrawListRequest.getTransferId(),
				getWithdrawListRequest.getTransferStatus());
	}

	/**
	 * 提现审批
	 * 
	 * @param withdrawRequest
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value = "/withdrawReview", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse withdrawReview(@RequestBody WithdrawRequest withdrawRequest, HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		goldpayTransManager.withdrawReviewPending(withdrawRequest.getTransferId());
		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
				Operation.WITHDRAW_RE_REVIEW.getOperationName(), withdrawRequest.toString()));
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		return rep;
	}

	/**
	 * Goldpay划账
	 * 
	 * @param withdrawRequest
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value = "/goldpayRemit", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse goldpayRemit(@RequestBody WithdrawRequest withdrawRequest, HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		goldpayTransManager.goldpayRemitPending(withdrawRequest.getTransferId());
		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
				Operation.WITHDRAW_RE_REMIT.getOperationName(), withdrawRequest.toString()));
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		return rep;
	}

	/**
	 * 提现退款
	 * 
	 * @param withdrawRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/refund", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse refund(@RequestBody WithdrawRequest withdrawRequest, HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		goldpayTransManager.withdrawRefund(withdrawRequest.getTransferId());
		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
				Operation.WITHDRAW_REFUND.getOperationName(), withdrawRequest.toString()));
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		return rep;
	}

}
