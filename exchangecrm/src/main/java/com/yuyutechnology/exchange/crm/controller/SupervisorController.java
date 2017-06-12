package com.yuyutechnology.exchange.crm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yuyutechnology.exchange.crm.request.SaveSupervisorRequest;
import com.yuyutechnology.exchange.enums.Operation;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.CrmLogManager;
import com.yuyutechnology.exchange.manager.CrmUserInfoManager;
import com.yuyutechnology.exchange.pojo.CrmLog;
import com.yuyutechnology.exchange.pojo.CrmSupervisor;
import com.yuyutechnology.exchange.util.JsonBinder;

@Controller
public class SupervisorController {

	@Autowired
	CrmAlarmManager crmAlarmManager;
	@Autowired
	CrmUserInfoManager crmUserInfoManager;
	@Autowired
	ConfigManager configManager;
	@Autowired
	CrmLogManager crmLogManager;

	ModelAndView mav;

	private static Logger logger = LogManager.getLogger(AlarmController.class);

	@RequestMapping(value = "/alarm/getSupervisorList", method = RequestMethod.GET)
	public ModelAndView getSupervisorList() {
		mav = new ModelAndView();
		List<CrmSupervisor> supervisorList = crmAlarmManager.getCrmSupervisorList();
		mav.addObject("supervisorList", supervisorList);
		mav.setViewName("alarm/supervisorInfo");
		return mav;
	}

	@RequestMapping(value = "/alarm/delSupervisor", method = RequestMethod.GET)
	public ModelAndView delSupervisor(Integer supervisorId, HttpServletRequest request, HttpServletResponse response) {
		mav = new ModelAndView();
		crmAlarmManager.delSupervisorById(supervisorId);

		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
				Operation.DELETE_SUPERVISOR.getOperationName(), supervisorId.toString()));

		mav.setViewName("redirect:/alarm/getSupervisorList");
		return mav;
	}

	@RequestMapping(value = "/alarm/saveSupervisor", method = RequestMethod.POST)
	public void saveSupervisor(SaveSupervisorRequest saveSupervisorRequest, HttpServletRequest request,
			HttpServletResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String retCode = crmAlarmManager.saveSupervisor(saveSupervisorRequest.getSupervisorName().trim(),
				saveSupervisorRequest.getSupervisorMobile().trim(), saveSupervisorRequest.getSupervisorEmail().trim());
		
		if(retCode=="success"){
			crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
					Operation.ADD_SUPERVISOR.getOperationName(), saveSupervisorRequest.toString()));
		}
		map.put("retCode", retCode);
		out.print(JsonBinder.getInstanceNonNull().toJson(map));
	}

	@RequestMapping(value = "/alarm/updateReserveFunds", method = RequestMethod.POST)
	public ModelAndView updateReserveFunds(String reserveFunds) {
		mav = new ModelAndView();
		configManager.updateConfig("reserve_funds", reserveFunds);
		configManager.refreshConfig();
		
		mav.setViewName("redirect:/alarm/getAlarmConfigList");
		return mav;
	}

}
