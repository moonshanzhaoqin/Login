package com.yuyutechnology.exchange.crm.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.crm.dto.TotalAsset;
import com.yuyutechnology.exchange.crm.request.GetTotalAssetsInfoRequest;
import com.yuyutechnology.exchange.crm.request.UserFreezeRequest;
import com.yuyutechnology.exchange.enums.Operation;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.CrmLogManager;
import com.yuyutechnology.exchange.manager.CrmUserInfoManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.CrmLog;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionManager;
import com.yuyutechnology.exchange.util.page.PageBean;

@Controller
public class AccountInfoController {

	@Autowired
	UserManager userManager;
	@Autowired
	CrmUserInfoManager crmUserInfoManager;
	@Autowired
	CommonManager commonManager;
	@Autowired
	SessionManager sessionManager;
	@Autowired
	CrmLogManager crmLogManager;

	ModelAndView mav;

	private static Logger log = LogManager.getLogger(AccountInfoController.class);

	@RequestMapping(value = "/account/getTotalAssetsDetails", method = RequestMethod.GET)
	public ModelAndView getTotalAssetsDetails() {
		mav = new ModelAndView();
		HashMap<String, BigDecimal> userTotalAssets = crmUserInfoManager.getUserAccountTotalAssets();
		HashMap<String, BigDecimal> systemTotalAssets = crmUserInfoManager.getSystemAccountTotalAssets();
		List<Currency> currencies = commonManager.getAllCurrencies();
		List<TotalAsset> totalAssets = new ArrayList<>();

		log.info("userTotalAssets={}", userTotalAssets);
		log.info("systemTotalAssets={}", systemTotalAssets);

		if (userTotalAssets != null && systemTotalAssets != null) {
			for (Currency currency : currencies) {
				totalAssets.add(new TotalAsset(currency,
						systemTotalAssets.get(currency.getCurrency()) == null ? new BigDecimal("0.0000")
								: systemTotalAssets.get(currency.getCurrency()),
						userTotalAssets.get(currency.getCurrency()) == null ? new BigDecimal("0.0000")
								: userTotalAssets.get(currency.getCurrency())));
			}
			mav.addObject("totalAssets", totalAssets);
			mav.addObject("systemAmount", systemTotalAssets.get("totalAssets"));
			mav.addObject("usermAmount", userTotalAssets.get("totalAssets"));
		}

		mav.setViewName("accountInfo/totalAssetsDetails");
		return mav;
	}

	@RequestMapping(value = "/account/accountOverview", method = RequestMethod.GET)
	public ModelAndView getAccountOverview(GetTotalAssetsInfoRequest requst) {

		mav = new ModelAndView();
		PageBean pageBean = new PageBean();
		HashMap<String, Object> result = crmUserInfoManager.getUserAccountInfoListByPage(null, null, 3, 3, 3, null,
				null, 1, 10);
		pageBean.setCurrentPage((int) result.get("currentPage"));
		pageBean.setPageSize((int) result.get("pageSize"));
		pageBean.setPageTotal((int) result.get("pageTotal"));
		pageBean.setTotal((long) result.get("total"));
		pageBean.setRows((List<?>) result.get("list"));
		requst.setPageBean(pageBean);

		mav.addObject("updateFlag", crmUserInfoManager.getUpdateFlag());
		mav.addObject("model", requst);

		mav.setViewName("accountInfo/accountOverview");
		return mav;
	}

	@RequestMapping(value = "/account/getTotalAssetsInfoByPage", method = RequestMethod.POST)
	public ModelAndView getTotalAssetsInfoByPage(GetTotalAssetsInfoRequest requst) {
		mav = new ModelAndView();
		PageBean pageBean = new PageBean();
		HashMap<String, Object> result = crmUserInfoManager.getUserAccountInfoListByPage(requst.getUserPhone(),
				requst.getUserName(), Integer.parseInt(requst.getUserAvailable()),
				Integer.parseInt(requst.getLoginAvailable()), Integer.parseInt(requst.getPayAvailable()),
				requst.getUpperLimit(), requst.getLowerLimit(), requst.getPageBean().getCurrentPage(),
				requst.getPageBean().getPageSize());

		pageBean.setCurrentPage((int) result.get("currentPage"));
		pageBean.setPageSize((int) result.get("pageSize"));
		pageBean.setPageTotal((int) result.get("pageTotal"));
		pageBean.setTotal((long) result.get("total"));
		pageBean.setRows((List<?>) result.get("list"));
		requst.setPageBean(pageBean);

		mav.addObject("updateFlag", crmUserInfoManager.getUpdateFlag());
		mav.addObject("model", requst);

		mav.setViewName("accountInfo/accountOverview");
		return mav;
	}

	@RequestMapping(value = "/account/userFreeze", method = RequestMethod.GET)
	public ModelAndView userFreeze(UserFreezeRequest userFreezeRequest, HttpServletRequest request,
			HttpServletResponse response) {
		mav = new ModelAndView();
		userManager.userFreeze(userFreezeRequest.getUserId(), userFreezeRequest.getOperate());
		if (userFreezeRequest.getOperate() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE) {
			SessionData sessionData = sessionManager.getByUserid(userFreezeRequest.getUserId());
			if (sessionData != null) {
				sessionManager.cleanSession(sessionData.getSessionId());
			}
			sessionManager.delLoginToken(userFreezeRequest.getUserId());
			userManager.logout(userFreezeRequest.getUserId());
			crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
					Operation.FREEZE_USER.getOperationName(), userFreezeRequest.getUserId().toString()));
		} else {
			crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
					Operation.DEFROST_USER.getOperationName(), userFreezeRequest.getUserId().toString()));
		}
		mav.setViewName("redirect:/account/accountOverview");
		return mav;
	}

	@RequestMapping(value = "/account/updateImmediately", method = RequestMethod.GET)
	public ModelAndView updateImmediately(HttpServletRequest request, HttpServletResponse response) {
		mav = new ModelAndView();
		crmUserInfoManager.updateImmediately();
		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
				Operation.UPDATE_USER_INFO.getOperationName()));
		mav.setViewName("redirect:/account/accountOverview");
		return mav;
	}

}
