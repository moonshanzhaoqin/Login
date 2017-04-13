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
import com.yuyutechnology.exchange.crm.request.GetDetailSeqRequest;
import com.yuyutechnology.exchange.crm.request.GetTransferRequest;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.manager.WalletManager;
import com.yuyutechnology.exchange.utils.page.PageBean;

@Controller
public class BadAccountController {
	private static Logger logger = LogManager.getLogger(BadAccountController.class);

	@Autowired
	WalletManager walletManager;
	@Autowired
	CommonManager commonManager;

	@Autowired
	TransferManager transferManager;

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
	 * TODO 获取交易详情
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
}
