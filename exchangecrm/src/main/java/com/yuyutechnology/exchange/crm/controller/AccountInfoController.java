package com.yuyutechnology.exchange.crm.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yuyutechnology.exchange.crm.dto.TotalAsset;
import com.yuyutechnology.exchange.crm.request.GetTotalAssetsInfoRequest;
import com.yuyutechnology.exchange.crm.request.UserFreezeRequest;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.CrmUserInfoManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.utils.page.PageBean;

@Controller
public class AccountInfoController {

	@Autowired
	UserManager userManager;
	@Autowired
	CrmUserInfoManager crmUserInfoManager;
	@Autowired
	CommonManager commonManager;

	ModelAndView mav;

	private static Logger log = LoggerFactory.getLogger(AccountInfoController.class);

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

		mav.setViewName("/accountInfo/totalAssetsDetails");
		return mav;
	}

	@RequestMapping(value = "/account/accountOverview", method = RequestMethod.GET)
	public ModelAndView getAccountOverview(GetTotalAssetsInfoRequest requst) {

		mav = new ModelAndView();
		PageBean pageBean = new PageBean();
		HashMap<String, Object> result = crmUserInfoManager.getUserAccountInfoListByPage(null, null, 3, null, null, 1,
				10);
		pageBean.setCurrentPage((int) result.get("currentPage"));
		pageBean.setPageSize((int) result.get("pageSize"));
		pageBean.setPageTotal((int) result.get("pageTotal"));
		pageBean.setTotal((long) result.get("total"));
		pageBean.setRows((List<?>) result.get("list"));
		requst.setPageBean(pageBean);

		mav.addObject("updateFlag", crmUserInfoManager.getUpdateFlag());
		mav.addObject("model", requst);

		mav.setViewName("/accountInfo/accountOverview");
		return mav;
	}

	@RequestMapping(value = "/account/getTotalAssetsInfoByPage", method = RequestMethod.POST)
	public ModelAndView getTotalAssetsInfoByPage(GetTotalAssetsInfoRequest requst) {
		mav = new ModelAndView();
		PageBean pageBean = new PageBean();
		HashMap<String, Object> result = crmUserInfoManager.getUserAccountInfoListByPage(requst.getUserPhone(),
				requst.getUserName(), Integer.parseInt(requst.getIsFrozen()), requst.getUpperLimit(),
				requst.getLowerLimit(), requst.getPageBean().getCurrentPage(), requst.getPageBean().getPageSize());

		pageBean.setCurrentPage((int) result.get("currentPage"));
		pageBean.setPageSize((int) result.get("pageSize"));
		pageBean.setPageTotal((int) result.get("pageTotal"));
		pageBean.setTotal((long) result.get("total"));
		pageBean.setRows((List<?>) result.get("list"));
		requst.setPageBean(pageBean);

		mav.addObject("updateFlag", crmUserInfoManager.getUpdateFlag());
		mav.addObject("model", requst);

		mav.setViewName("/accountInfo/accountOverview");
		return mav;
	}

	@RequestMapping(value = "/account/userFreeze", method = RequestMethod.GET)
	public ModelAndView userFreeze(UserFreezeRequest request) {
		mav = new ModelAndView();
		userManager.userFreeze(request.getUserId(), request.getOperate());
		crmUserInfoManager.userFreeze(request.getUserId(), request.getOperate());
		mav.setViewName("redirect:/account/accountOverview");
		return mav;
	}

	@RequestMapping(value = "/account/updateImmediately", method = RequestMethod.GET)
	public ModelAndView updateImmediately() {
		mav = new ModelAndView();
		crmUserInfoManager.updateImmediately();
		mav.setViewName("redirect:/account/accountOverview");
		return mav;
	}

}
