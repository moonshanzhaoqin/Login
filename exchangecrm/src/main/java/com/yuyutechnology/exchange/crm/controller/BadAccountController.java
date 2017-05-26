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
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.crm.reponse.BaseResponse;
import com.yuyutechnology.exchange.crm.request.GetBadAccountByPageRequest;
import com.yuyutechnology.exchange.crm.request.GetDetailSeqByTransferIdRequest;
import com.yuyutechnology.exchange.crm.request.GetDetailSeqRequest;
import com.yuyutechnology.exchange.crm.request.GetExchangeRequest;
import com.yuyutechnology.exchange.crm.request.GetTransferRequest;
import com.yuyutechnology.exchange.crm.request.SetGoldpayRemitTaskStatusRequest;
import com.yuyutechnology.exchange.enums.Operation;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.CrmLogManager;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.GoldpayTransManager;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.manager.WalletManager;
import com.yuyutechnology.exchange.pojo.CrmLog;
import com.yuyutechnology.exchange.util.page.PageBean;

@Controller
public class BadAccountController {
	private static Logger logger = LogManager.getLogger(BadAccountController.class);

	@Autowired
	WalletManager walletManager;
	@Autowired
	CommonManager commonManager;
	@Autowired
	GoldpayTransManager goldpayTransManager;
	@Autowired
	TransferManager transferManager;
	@Autowired
	ExchangeManager exchangeManager;
	@Autowired
	CrmLogManager crmLogManager;

	/**
	 * 分页获取坏账列表
	 * 
	 * @param getBadAccountByPageRequest
	 * @param request
	 * @param response
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "/getBadAccountByPage", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public PageBean getBadAccountByPage(@RequestBody GetBadAccountByPageRequest getBadAccountByPageRequest,
			HttpServletRequest request, HttpServletResponse response) {
		return walletManager.getBadAccountByPage(Integer.parseInt(getBadAccountByPageRequest.getCurrentPage()));
	}

	/**
	 * 获取详细流水
	 * 
	 * @param getDetailSeqRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getDetailSeq", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<?> getDetailSeq(@RequestBody GetDetailSeqRequest getDetailSeqRequest, HttpServletRequest request,
			HttpServletResponse response) {
		return walletManager.getDetailSeq(getDetailSeqRequest.getBadAccountId());
	}
	/**
	 * 
	 * @param getDetailSeqByTransferIdSeqRequest
	 * @param request
	 * @param response
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping(value = "/getDetailSeqByTransferId", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<?> getDetailSeqByTransferId(@RequestBody GetDetailSeqByTransferIdRequest getDetailSeqByTransferIdRequest, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("getDetailSeqByTransferId==>transferId:{}",getDetailSeqByTransferIdRequest.getTransferId());
		return walletManager.getDetailSeqByTransferId(getDetailSeqByTransferIdRequest.getTransferId());
	}

	/**
	 *获取交易详情
	 * 
	 * @param getTransferRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getTransfer", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public Object getTransfer(@RequestBody GetTransferRequest getTransferRequest, HttpServletRequest request,
			HttpServletResponse response) {
		return transferManager.getTransfer(getTransferRequest.getTransferId());
	}
	/**
	 *  获取兑换详情
	 * @param getExchangeRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getExchange", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public Object getExchange(@RequestBody GetExchangeRequest getExchangeRequest, HttpServletRequest request,
			HttpServletResponse response) {
		return exchangeManager.getExchange(getExchangeRequest.getExchangeId());
	}

	/**
	 * 修改划账任务开启状态
	 * 
	 * @param forbidden
	 * @param request
	 * @param response
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "/setGoldpayRemitTaskStatus", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse setGoldpayRemitTaskStatus(@RequestBody SetGoldpayRemitTaskStatusRequest forbidden,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info(forbidden.getStatus());
		BaseResponse rep = new BaseResponse();
		goldpayTransManager.forbiddenGoldpayRemitWithdraws(forbidden.getStatus());
		if (forbidden.getStatus() == ServerConsts.ACCOUTING_TASK_OPEN) {
			crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
					Operation.OPEN_ACCOUTING_TASK.getOperationName()));
		}else {
			crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
					Operation.CLOSE_ACCOUTING_TASK.getOperationName()));
		}
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		return rep;
	}

	/**
	 * 获取划账任务开启状态
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getGoldpayRemitTaskStatus", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public boolean getGoldpayRemitTaskStatus(HttpServletRequest request, HttpServletResponse response) {
		return goldpayTransManager.getGoldpayRemitWithdrawsforbidden();
	}
}
