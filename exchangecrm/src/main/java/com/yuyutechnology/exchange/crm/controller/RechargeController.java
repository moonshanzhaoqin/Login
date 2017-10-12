/**
 * 
 */
package com.yuyutechnology.exchange.crm.controller;

import java.text.ParseException;

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

import com.yuyutechnology.exchange.crm.request.GetRechargeListRequest;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.CrmLogManager;
import com.yuyutechnology.exchange.manager.GoldpayTransManager;
import com.yuyutechnology.exchange.util.page.PageBean;

/**
 * @author suzan.wu
 *
 */
@Controller
public class RechargeController {
	private static Logger logger = LogManager.getLogger(RechargeController.class);

	@Autowired
	GoldpayTransManager goldpayTransManager;
	@Autowired
	CommonManager commonManager;
	@Autowired
	CrmLogManager crmLogManager;

	/**
	 * 获取充值列表 getRechargeList
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 * @throws NumberFormatException
	 */
	@ResponseBody
	@RequestMapping(value = "/getRechargeList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public PageBean getRechargeList(@RequestBody GetRechargeListRequest getRechargeListRequest,
			HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, ParseException {
		logger.info(getRechargeListRequest.toString());
		return goldpayTransManager.getRechargeList(Integer.parseInt(getRechargeListRequest.getCurrentPage()),
				getRechargeListRequest.getUserPhone(), getRechargeListRequest.getLowerAmount(),
				getRechargeListRequest.getUpperAmount(), getRechargeListRequest.getStartTime(),
				getRechargeListRequest.getEndTime(), getRechargeListRequest.getTransferType());
	}
}
