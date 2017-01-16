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
	
	ModelAndView mav;
	
	private static Logger logger = LoggerFactory.getLogger(AlarmController.class);
	
	@RequestMapping(value="/alarm/getAlarmConfigList",method=RequestMethod.GET)
	public ModelAndView getAlarmConfigList(){
		mav = new ModelAndView();
	
		HashMap<String, BigDecimal> userTotalAssets = crmUserInfoManager.getUserAccountTotalAssets();
		
		if(userTotalAssets !=null ){
			List<CrmAlarm> list = crmAlarmManager.getCrmAlarmConfigList();
			HashMap<String, BigDecimal> map =crmAlarmManager.getAccountInfo(userTotalAssets.get("totalAssets"));
			
			mav.addObject("exHoldingTotalAssets", map.get("exHoldingTotalAssets"));
			mav.addObject("userHoldingTotalAssets", map.get("userHoldingTotalAssets"));
			mav.addObject("reserveFunds", map.get("reserveFunds"));
			mav.addObject("reserveAvailability", map.get("reserveAvailability"));
			mav.addObject("list", list);
		}
		List<CrmSupervisor> supervisorList = crmAlarmManager.getCrmSupervisorList();
		mav.addObject("supervisorList", supervisorList);
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
		map.put("supervisorList", supervisorList);
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
	
	@RequestMapping(value="/alarm/updateAlarmAvailable",method=RequestMethod.GET)
	public ModelAndView updateAlarmAvailable(Integer alarmId,int alarmAvailable){
		mav = new ModelAndView();
		crmAlarmManager.updateAlarmAvailable(alarmId, alarmAvailable);
		mav.setViewName("redirect:/alarm/getAlarmConfigList");
		return mav;
	}
	
	@RequestMapping(value="/alarm/getSupervisorList",method=RequestMethod.GET)
	public ModelAndView getSupervisorList(){
		mav = new ModelAndView();
		List<CrmSupervisor> supervisorList = crmAlarmManager.getCrmSupervisorList();
		mav.addObject("supervisorList", supervisorList);
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
	public void saveSupervisor(HttpServletResponse response,SaveSupervisorRequest request){

		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		String retCode = crmAlarmManager.saveSupervisor(request.getSupervisorName(), 
				request.getSupervisorMobile(), request.getSupervisorEmail());
		map.put("retCode", retCode);
		out.print(JsonBinder.getInstanceNonNull().toJson(map));
	}

}
