package com.yuyutechnology.exchange.crm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
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
import com.yuyutechnology.exchange.dto.WithdrawDetail;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.GoldpayTransManager;
import com.yuyutechnology.exchange.utils.page.PageBean;

@Controller
public class WithdrawController {
	private static Logger logger = LogManager.getLogger(WithdrawController.class);

	@Autowired
	GoldpayTransManager goldpayTransManager;
	@Autowired
	CommonManager commonManager;

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
		return	goldpayTransManager.getWithdrawList(Integer.parseInt(getWithdrawListRequest.getCurrentPage()),
				getWithdrawListRequest.getUserId(), getWithdrawListRequest.getReviewStatus(),
				getWithdrawListRequest.getGoldpayRemit());
	}

	/**
	 * 获取提现详细信息
	 * 
	 * @param withdrawRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getWithdrawDetail", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public WithdrawDetail getWithdrawDetail(@RequestBody WithdrawRequest withdrawRequest, HttpServletRequest request,
			HttpServletResponse response) {
		return goldpayTransManager.getWithdrawDetail(withdrawRequest.getWithdrawId());
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
		goldpayTransManager.withdrawReview(withdrawRequest.getWithdrawId());
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
		goldpayTransManager.goldpayRemit(withdrawRequest.getWithdrawId());
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		return rep;
	}
}
