package com.yuyutechnology.exchange.crm.controller;

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

import com.yuyutechnology.exchange.crm.request.GetBadAccountByPageRequest;
import com.yuyutechnology.exchange.crm.request.GetDetailSeqByTransferIdRequest;
import com.yuyutechnology.exchange.crm.request.GetDetailSeqRequest;
import com.yuyutechnology.exchange.crm.request.GetExchangeRequest;
import com.yuyutechnology.exchange.crm.request.GetTransferRequest;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.CrmLogManager;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.manager.WalletManager;
import com.yuyutechnology.exchange.util.page.PageBean;

@Controller
public class BadAccountController {
	private static Logger logger = LogManager.getLogger(BadAccountController.class);

	@Autowired
	WalletManager walletManager;
	@Autowired
	CommonManager commonManager;
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
	public List<?> getDetailSeqByTransferId(
			@RequestBody GetDetailSeqByTransferIdRequest getDetailSeqByTransferIdRequest, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("getDetailSeqByTransferId==>transferId:{}", getDetailSeqByTransferIdRequest.getTransferId());
		return walletManager.getDetailSeqByTransferId(getDetailSeqByTransferIdRequest.getTransferId());
	}

	/**
	 * 获取交易详情
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
		logger.info("request:{}", getTransferRequest.getTransferId());

		return transferManager.getTransfer(getTransferRequest.getTransferId());
	}

	/**
	 * 获取兑换详情
	 * 
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
}
