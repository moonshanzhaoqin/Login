package com.yuyutechnology.exchange.crm.controller;

import org.apache.logging.log4j.Logger;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
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
import com.yuyutechnology.exchange.pojo.Transfer;
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

	// TODO 分页获取坏账列表
	@ResponseBody
	@RequestMapping(value = "/getBadAccountByPage", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public PageBean getBadAccountByPage(@RequestBody GetBadAccountByPageRequest getBadAccountByPageRequest,
			HttpServletRequest request, HttpServletResponse response) {
		return walletManager.getBadAccountByPage(Integer.parseInt(getBadAccountByPageRequest.getCurrentPage()));
	}

	// TODO 获取详细流水
	@ResponseBody
	@RequestMapping(value = "/getDetailSeq", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<?> getDetailSeq(@RequestBody GetDetailSeqRequest getDetailSeqRequest, HttpServletRequest request,
			HttpServletResponse response) {
		return walletManager.getDetailSeq(getDetailSeqRequest.getBadAccountId());
	}

	// TODO 获取交易详情
	@ResponseBody
	@RequestMapping(value = "/getTransfer", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public Transfer getTransfer(@RequestBody GetTransferRequest getTransferRequest, HttpServletRequest request,
			HttpServletResponse response) {
		return transferManager.getTransfer(getTransferRequest.getTransferId());
	}
}
