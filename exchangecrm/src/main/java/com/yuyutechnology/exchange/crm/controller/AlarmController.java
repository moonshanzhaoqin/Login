package com.yuyutechnology.exchange.crm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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
import com.yuyutechnology.exchange.crm.request.SaveSupervisorRequest;
import com.yuyutechnology.exchange.crm.request.UpdateAlarmConfigInfoRequest;
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
	CrmAlarmManager crmAlarmManager;
	@Autowired
	CrmUserInfoManager crmUserInfoManager;
	
	ModelAndView mav;
	
	private static Logger logger = LoggerFactory.getLogger(AlarmController.class);
	
	@RequestMapping(value="/alarm/getAlarmConfigList",method=RequestMethod.GET)
	public ModelAndView getAlarmConfigList(){
		mav = new ModelAndView();
		
		HashMap<String, BigDecimal> systemTotalAssets = crmUserInfoManager.getSystemAccountTotalAssets();
		HashMap<String, BigDecimal> userTotalAssets = crmUserInfoManager.getUserAccountTotalAssets();
		BigDecimal difference = systemTotalAssets.get("totalAssets").subtract(userTotalAssets.get("totalAssets"));
		List<CrmSupervisor> supervisorList = crmAlarmManager.getCrmSupervisorList();
		List<CrmAlarm> list = crmAlarmManager.getCrmAlarmConfigList();
		mav.addObject("supervisorList", supervisorList);
		mav.addObject("list", list);
		mav.addObject("systemTotalAssets", systemTotalAssets.get("totalAssets"));
		mav.addObject("userTotalAssets", userTotalAssets.get("totalAssets"));
		mav.addObject("difference", difference);
		mav.setViewName("/alarm/alarmConfigInfo");
		return mav;
	}
	
	@RequestMapping(value="/alarm/delAlarmConfig",method=RequestMethod.GET)
	public ModelAndView delAlarmConfig(Integer alarmId){
		mav = new ModelAndView();
		crmAlarmManager.delAlarmConfig(alarmId);
		mav.setViewName("redirect:/alarm/getAlarmConfigList");
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
		map.put("list", supervisorList);
		out.print(JsonBinder.getInstance().toJson(map));

	}
	
	@RequestMapping(value="/alarm/updateAlarmConfigInfo",method=RequestMethod.POST)
	public ModelAndView updateAlarmConfigInfo(UpdateAlarmConfigInfoRequest request){
		mav = new ModelAndView();
		crmAlarmManager.updateAlarmConfig(request.getAlarmId(), request.getAlarmGrade(), 
				request.getCriticalThresholdLowerLimit(), request.getCriticalThresholdUpperLimit(),
				request.getAlarmMode(), 0,JsonBinder.getInstance().toJson(request.getSupervisorId()));
		mav.setViewName("redirect:/alarm/getAlarmConfigList");
		return mav;
	}
	
	
	
	@RequestMapping(value="/alarm/saveAlarmConfig",method=RequestMethod.POST)
	public ModelAndView saveAlarmConfig(HttpServletResponse response,SaveAlarmConfigRequest request){
		mav = new ModelAndView();
		crmAlarmManager.addAlarmConfig(request.getAlarmGrade(), request.getCriticalThresholdLowerLimit()
				,request.getCriticalThresholdUpperLimit(),
				request.getAlarmMode(), 0,JsonBinder.getInstance().toJson(request.getSupervisorId()));
		mav.setViewName("redirect:/alarm/getAlarmConfigList");
		return mav;
	}
	
	@RequestMapping(value="/alarm/getSupervisorList",method=RequestMethod.GET)
	public ModelAndView getSupervisorList(){
		mav = new ModelAndView();
		List<CrmSupervisor> list = crmAlarmManager.getCrmSupervisorList();
		mav.addObject("list", list);
		mav.setViewName("/alarm/supervisorInfo");
		return mav;
	}
	
	@RequestMapping(value="/alarm/delSupervisor",method=RequestMethod.GET)
	public ModelAndView delSupervisor(Integer supervisorId){
		mav = new ModelAndView();
		crmAlarmManager.delSupervisorById(supervisorId);
		mav.setViewName("redirect:/alarm/getSupervisorList");
		return mav;
	}
	
	@RequestMapping(value="/alarm/saveSupervisor",method=RequestMethod.POST)
	public ModelAndView saveSupervisor(SaveSupervisorRequest request){
		mav = new ModelAndView();
		crmAlarmManager.saveSupervisor(request.getSupervisorName(), 
				request.getSupervisorMobile(), request.getSupervisorEmail());
		mav.setViewName("redirect:/alarm/getSupervisorList");
		return mav;
	}
	
}
