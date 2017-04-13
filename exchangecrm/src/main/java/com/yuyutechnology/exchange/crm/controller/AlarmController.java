package com.yuyutechnology.exchange.crm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yuyutechnology.exchange.crm.request.SaveAlarmConfigRequest;
import com.yuyutechnology.exchange.crm.request.UpdateAlarmConfigInfoRequest;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.CrmUserInfoManager;
import com.yuyutechnology.exchange.pojo.CrmAlarm;
import com.yuyutechnology.exchange.pojo.CrmSupervisor;
import com.yuyutechnology.exchange.utils.JsonBinder;

@Controller
public class AlarmController {
	
	@Autowired
	CrmAlarmManager crmAlarmManager;
	@Autowired
	CrmUserInfoManager crmUserInfoManager;
	@Autowired
	ConfigManager configManager;
	
	ModelAndView mav;
	
	private static Logger logger = LogManager.getLogger(AlarmController.class);
	
	@RequestMapping(value="/alarm/getAlarmConfigList",method=RequestMethod.GET)
	public ModelAndView getAlarmConfigList(){
		mav = new ModelAndView();
	
		HashMap<String, BigDecimal> userTotalAssets = crmUserInfoManager.getUserAccountTotalAssets();
		
		if(userTotalAssets !=null ){
			List<CrmAlarm> list = crmAlarmManager.getCrmAlarmConfigList();
			HashMap<String, BigDecimal> map =crmAlarmManager.getAccountInfo(userTotalAssets.get("totalAssets"));
			
			
			BigDecimal difference = (map.get("userHoldingTotalAssets")).subtract(map.get("exHoldingTotalAssets"));
			BigDecimal remaining = (map.get("reserveFunds")).subtract(difference);
			
			logger.info("reserveFunds : {},difference: {},remaining : {}",new Object[]{map.get("reserveFunds"),difference,remaining});

			mav.addObject("exHoldingTotalAssets", map.get("exHoldingTotalAssets"));
			mav.addObject("userHoldingTotalAssets", map.get("userHoldingTotalAssets"));
			mav.addObject("reserveFunds", map.get("reserveFunds"));
			mav.addObject("remaining", remaining);
			mav.addObject("reserveAvailability", map.get("reserveAvailability"));
			mav.addObject("list", list);
		}
		List<CrmSupervisor> supervisorList = crmAlarmManager.getCrmSupervisorList();
		mav.addObject("supervisorList", supervisorList);
		mav.setViewName("alarm/alarmConfigInfo");
		return mav;
	}
	
	
	@RequestMapping(value="/alarm/getLargeTransAlarmConfigList",method=RequestMethod.GET)
	public ModelAndView getLargeTransAlarmConfigList(){
		mav = new ModelAndView();
		List<CrmAlarm> list = crmAlarmManager.getCrmAlarmConfigList();
		List<CrmSupervisor> supervisorList = crmAlarmManager.getCrmSupervisorList();
		
		HashMap<String, BigDecimal> map = crmAlarmManager.getLargeTransLimit();
		
		mav.addObject("supervisorList", supervisorList);
		mav.addObject("list", list);
		mav.addObject("transferLimitPerPay", map.get("transferLimitPerPay"));
		mav.addObject("exchangeLimitPerPay", map.get("exchangeLimitPerPay"));
		mav.setViewName("alarm/largeTransAlarmConfig");
		return mav;
	}
	
	@RequestMapping(value="/alarm/getBadAccountAlarmConfigList",method=RequestMethod.GET)
	public ModelAndView getBadAccountAlarmConfigList(){
		mav = new ModelAndView();
		List<CrmAlarm> list = crmAlarmManager.getCrmAlarmConfigList();
		mav.addObject("list", list);
		List<CrmSupervisor> supervisorList = crmAlarmManager.getCrmSupervisorList();
		mav.addObject("supervisorList", supervisorList);
		mav.setViewName("alarm/badAccountAlarmConfigInfo");
		return mav;
	}

	@RequestMapping(value="/alarm/delAlarmConfig",method=RequestMethod.GET)
	public ModelAndView delAlarmConfig(Integer alarmId){
		mav = new ModelAndView();
		int alarmType = crmAlarmManager.delAlarmConfig(alarmId);
		if(alarmType == 0){
			mav.setViewName("redirect:/alarm/getAlarmConfigList");
		} else if (alarmType == 3) {
			mav.setViewName("redirect:/alarm/getBadAccountAlarmConfigList");
		}else{
			mav.setViewName("redirect:/alarm/getLargeTransAlarmConfigList");
		}
		return mav;
	}
	
	@RequestMapping(value="/alarm/updateAlarmConfig",method=RequestMethod.POST)
	public void updateAlarmConfig(HttpServletResponse response,Integer alarmId){
		
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		CrmAlarm crmAlarm = crmAlarmManager.getAlarmConfigById(alarmId);
		List<CrmSupervisor> supervisorList = crmAlarmManager.getCrmSupervisorList();
		
		map.put("retCode", 1);
		map.put("crmAlarm", crmAlarm);
		map.put("supervisorList", supervisorList);
		out.print(JsonBinder.getInstance().toJson(map));

	}
	
	@RequestMapping(value="/alarm/updateAlarmConfigInfo",method=RequestMethod.POST)
	public ModelAndView updateAlarmConfigInfo(UpdateAlarmConfigInfoRequest request){
		mav = new ModelAndView();
		crmAlarmManager.updateAlarmConfig(request.getAlarmId(), request.getAlarmType(), 
				request.getCriticalThresholdLowerLimit(), request.getCriticalThresholdUpperLimit(),
				request.getAlarmMode(), 0,JsonBinder.getInstance().toJson(request.getSupervisorId()));
		
		if(request.getAlarmType() == 0){
			mav.setViewName("redirect:/alarm/getAlarmConfigList");
		} else if (request.getAlarmType() == 3) {
			mav.setViewName("redirect:/alarm/getBadAccountAlarmConfigList");
		}else{
			mav.setViewName("redirect:/alarm/getLargeTransAlarmConfigList");
		}
		return mav;
	}
	
	@RequestMapping(value="/alarm/saveAlarmConfig",method=RequestMethod.POST)
	public ModelAndView saveAlarmConfig(HttpServletResponse response,SaveAlarmConfigRequest request){
		mav = new ModelAndView();
		crmAlarmManager.addAlarmConfig(request.getAlarmType(), request.getCriticalThresholdLowerLimit()
				,request.getCriticalThresholdUpperLimit(),
				request.getAlarmMode(), 0,JsonBinder.getInstance().toJson(request.getSupervisorId()));
		
		if(request.getAlarmType() == 0){
			mav.setViewName("redirect:/alarm/getAlarmConfigList");
		} else if (request.getAlarmType() == 3) {
			mav.setViewName("redirect:/alarm/getBadAccountAlarmConfigList");
		}else{
			mav.setViewName("redirect:/alarm/getLargeTransAlarmConfigList");
		}
		return mav;
	}
	
	@RequestMapping(value="/alarm/updateAlarmAvailable",method=RequestMethod.GET)
	public ModelAndView updateAlarmAvailable(Integer alarmId,int alarmAvailable){
		mav = new ModelAndView();
		int alarmType = crmAlarmManager.updateAlarmAvailable(alarmId, alarmAvailable);
		if(alarmType == 0){
			mav.setViewName("redirect:/alarm/getAlarmConfigList");
		} else if (alarmType == 3) {
			mav.setViewName("redirect:/alarm/getBadAccountAlarmConfigList");
		}else{
			mav.setViewName("redirect:/alarm/getLargeTransAlarmConfigList");
		}
//		mav.setViewName("redirect:/alarm/getAlarmConfigList");
		return mav;
	}
	
}
