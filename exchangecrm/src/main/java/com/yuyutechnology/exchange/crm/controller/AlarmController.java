package com.yuyutechnology.exchange.crm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yuyutechnology.exchange.crm.request.SaveAlarmConfigRequest;
import com.yuyutechnology.exchange.manager.AdminManager;
import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.CrmUserInfoManager;
import com.yuyutechnology.exchange.manager.impl.CrmAlarmManagerImpl;
import com.yuyutechnology.exchange.pojo.CrmAlarm;
import com.yuyutechnology.exchange.pojo.CrmSupervisor;
import com.yuyutechnology.exchange.utils.JsonBinder;

@Controller
public class AlarmController {
	
	@Autowired
	AdminManager adminManager;
	@Autowired
	CrmAlarmManager crmAlarmManager;
	@Autowired
	CrmUserInfoManager crmUserInfoManager;
	
	ModelAndView mav;
	
	private static Logger logger = LoggerFactory.getLogger(CrmAlarmManagerImpl.class);
	
	@RequestMapping(value="/alarm/getAlarmConfigList",method=RequestMethod.GET)
	public ModelAndView getAlarmConfigList(){
		mav = new ModelAndView();
		
		HashMap<String, BigDecimal> systemTotalAssets = crmUserInfoManager.getSystemAccountTotalAssets();
		HashMap<String, BigDecimal> userTotalAssets = crmUserInfoManager.getUserAccountTotalAssets();
		BigDecimal difference = systemTotalAssets.get("totalAssets").subtract(userTotalAssets.get("totalAssets"));
		
		List<CrmAlarm> list = crmAlarmManager.getCrmAlarmConfigList();
		mav.addObject("list", list);
		mav.addObject("systemTotalAssets", systemTotalAssets.get("totalAssets"));
		mav.addObject("userTotalAssets", userTotalAssets.get("totalAssets"));
		mav.addObject("difference", difference);
		mav.setViewName("/alarm/alarmConfigInfo");
		return mav;
	}
	
	
	@RequestMapping(value="/alarm/addAlarmConfig",method=RequestMethod.GET)
	public ModelAndView addAlarmConfig(){
		mav = new ModelAndView();
		List<CrmSupervisor> list = crmAlarmManager.getCrmSupervisorList();
		mav.addObject("list", list);
		mav.setViewName("/alarm/addAlarmConfig");
		return mav;
	}
	
	@RequestMapping(value="/alarm/saveAlarmConfig",method=RequestMethod.POST)
	public void saveAlarmConfig(HttpServletResponse response,SaveAlarmConfigRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		crmAlarmManager.addAlarmConfig(request.getAlarmGrade(), request.getCriticalThresholdLowerLimit()
				,request.getCriticalThresholdUpperLimit(),
				request.getAlarmMode(), 0,Arrays.toString(request.getSupervisorId()));
		
		map.put("retCode", 1);
		out.print(JsonBinder.getInstance().toJson(map));
	}
	
	

}
