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

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.crm.reponse.BaseResponse;
import com.yuyutechnology.exchange.crm.request.GetWithdrawByPageRequest;
import com.yuyutechnology.exchange.crm.request.WithdrawRequest;
import com.yuyutechnology.exchange.dao.AdminDAO;
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
	@Autowired
	AdminDAO adminDAO;

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

	@ResponseBody
	@RequestMapping(value = "/finishWithdraw", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse finishWithdraw(@RequestBody WithdrawRequest withdrawRequest, HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		logger.info("finishWithdraw:{}", withdrawRequest.getWithdrawId());
		try {
			withdrawManager.finishWithdraw(withdrawRequest.getWithdrawId(),
					(String) request.getSession().getAttribute("adminName"));
		} catch (Exception e) {
			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
		}
		return rep;
	}

	@ResponseBody
	@RequestMapping(value = "/cancelWithdraw", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse cancelWithdraw(@RequestBody WithdrawRequest withdrawRequest, HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		logger.info("cancelWithdraw:{}", withdrawRequest.getWithdrawId());
		try {
			withdrawManager.cancelWithdraw(withdrawRequest.getWithdrawId(),
					(String) request.getSession().getAttribute("adminName"));
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		} catch (Exception e) {
			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
		}

		return rep;
	}

}
