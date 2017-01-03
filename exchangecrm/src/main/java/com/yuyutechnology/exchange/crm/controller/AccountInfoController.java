package com.yuyutechnology.exchange.crm.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yuyutechnology.exchange.crm.request.GetTotalAssetsInfoRequest;
import com.yuyutechnology.exchange.manager.CrmUserInfoManager;
import com.yuyutechnology.exchange.utils.page.PageBean;

@Controller
public class AccountInfoController {
	
	@Autowired
	CrmUserInfoManager crmUserInfoManager;
	
	ModelAndView mav;
	
	private static Logger log = LoggerFactory.getLogger(AccountInfoController.class);

	@RequestMapping(value="/account/getTotalAssetsInfo",method=RequestMethod.GET)
	public ModelAndView getTotalAssetsInfo(GetTotalAssetsInfoRequest requst){

		mav = new ModelAndView();
		PageBean pageBean = new PageBean();
		HashMap<String, Object> result = crmUserInfoManager.getUserAccountInfoListByPage(
				null, null, 3, new BigDecimal(0), new BigDecimal(0), 1, 10);
		pageBean.setCurrentPage((int) result.get("currentPage"));
		pageBean.setPageSize((int) result.get("pageSize"));
		pageBean.setPageTotal((int) result.get("pageTotal"));
		pageBean.setTotal((long) result.get("total"));
		pageBean.setRows((List<?>) result.get("list"));
		requst.setPageBean(pageBean);

		HashMap<String, BigDecimal> systemTotalAssets = crmUserInfoManager.getSystemAccountTotalAssets();
		HashMap<String, BigDecimal> userTotalAssets = crmUserInfoManager.getUserAccountTotalAssets();
		
		log.info("systemTotalAssets : {}",systemTotalAssets.toString());
		log.info("userTotalAssets : {}",userTotalAssets.toString());
		
		mav.addObject("systemTotalAssets", systemTotalAssets);
		mav.addObject("userTotalAssets", userTotalAssets);
		mav.addObject("model", requst);
		
		mav.setViewName("/accountInfo/accountOverview");
		return mav;
	}
	
	@RequestMapping(value="/account/getTotalAssetsInfoByPage",method=RequestMethod.POST)
	public ModelAndView getTotalAssetsInfoByPage(GetTotalAssetsInfoRequest requst){
		mav = new ModelAndView();
		PageBean pageBean = new PageBean();
		HashMap<String, Object> result = crmUserInfoManager.getUserAccountInfoListByPage(
				requst.getUserPhone(), requst.getUserName(), requst.getIsFrozen(),
				requst.getUpperLimit(),requst.getLowerLimit(), requst.getPageBean().getCurrentPage(), 
				requst.getPageBean().getPageSize());
		
		pageBean.setCurrentPage((int) result.get("currentPage"));
		pageBean.setPageSize((int) result.get("pageSize"));
		pageBean.setPageTotal((int) result.get("pageTotal"));
		pageBean.setTotal((long) result.get("total"));
		pageBean.setRows((List<?>) result.get("list"));
		requst.setPageBean(pageBean);

		HashMap<String, BigDecimal> systemTotalAssets = crmUserInfoManager.getSystemAccountTotalAssets();
		HashMap<String, BigDecimal> userTotalAssets = crmUserInfoManager.getUserAccountTotalAssets();
		
		log.info("systemTotalAssets : {}",systemTotalAssets.toString());
		log.info("userTotalAssets : {}",userTotalAssets.toString());
		
		mav.addObject("systemTotalAssets", systemTotalAssets);
		mav.addObject("userTotalAssets", userTotalAssets);
		mav.addObject("model", requst);
		
		mav.setViewName("/accountInfo/accountOverview");
		return mav;
	}
	
}
