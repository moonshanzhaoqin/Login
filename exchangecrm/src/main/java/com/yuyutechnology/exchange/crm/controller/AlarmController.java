package com.yuyutechnology.exchange.crm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.crm.request.SaveAlarmConfigRequest;
import com.yuyutechnology.exchange.crm.request.UpdateAlarmConfigInfoRequest;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.enums.Operation;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.CrmLogManager;
import com.yuyutechnology.exchange.manager.CrmUserInfoManager;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.pojo.CrmAlarm;
import com.yuyutechnology.exchange.pojo.CrmLog;
import com.yuyutechnology.exchange.pojo.CrmSupervisor;
import com.yuyutechnology.exchange.util.JsonBinder;

@Controller
public class AlarmController {

	@Autowired
	CrmAlarmManager crmAlarmManager;
	@Autowired
	CrmUserInfoManager crmUserInfoManager;
	@Autowired
	ConfigManager configManager;
	@Autowired
	CrmLogManager crmLogManager;
	@Autowired
	TransferManager transferManager;

	private static final String[] VIEWNAMEARR = { "getAlarmConfigList", "getLargeTransAlarmConfigList",
			"getLargeTransAlarmConfigList", "getBadAccountAlarmConfigList", "getTotalGDQAlarmConfigList", "getRegistrationAlarmConfigList"};

	ModelAndView mav;

//	private static Logger logger = LogManager.getLogger(AlarmController.class);

	@RequestMapping(value = "/alarm/getAlarmConfigList", method = RequestMethod.GET)
	public ModelAndView getAlarmConfigList() {
		mav = new ModelAndView();

		HashMap<String, BigDecimal> userTotalAssets = crmUserInfoManager.getUserAccountTotalAssets();

		if (userTotalAssets != null) {
//			List<CrmAlarm> list = crmAlarmManager.getCrmAlarmConfigList();
			//TODO   crmAlarmManager.getAccountInfo
//			HashMap<String, BigDecimal> map = crmAlarmManager.getAccountInfo(userTotalAssets.get("totalAssets"));

//			BigDecimal difference = (map.get("userHoldingTotalAssets")).subtract(map.get("exHoldingTotalAssets"));
//			BigDecimal remaining = (map.get("reserveFunds")).subtract(difference);

//			logger.info("reserveFunds : {},difference: {},remaining : {}",
//					new Object[] { map.get("reserveFunds"), difference, remaining });
//
//			mav.addObject("exHoldingTotalAssets", map.get("exHoldingTotalAssets"));
//			mav.addObject("userHoldingTotalAssets", map.get("userHoldingTotalAssets"));
//			mav.addObject("reserveFunds", map.get("reserveFunds"));
//			mav.addObject("remaining", remaining);
//			mav.addObject("reserveAvailability", map.get("reserveAvailability"));
//			mav.addObject("list", list);
		}
		List<CrmSupervisor> supervisorList = crmAlarmManager.getCrmSupervisorList();
		mav.addObject("supervisorList", supervisorList);
		mav.setViewName("alarm/alarmConfigInfo");
		return mav;
	}

	@RequestMapping(value = "/alarm/getLargeTransAlarmConfigList", method = RequestMethod.GET)
	public ModelAndView getLargeTransAlarmConfigList() {
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

	@RequestMapping(value = "/alarm/getBadAccountAlarmConfigList", method = RequestMethod.GET)
	public ModelAndView getBadAccountAlarmConfigList() {
		mav = new ModelAndView();
		List<CrmAlarm> list = crmAlarmManager.getCrmAlarmConfigList();
		mav.addObject("list", list);
		List<CrmSupervisor> supervisorList = crmAlarmManager.getCrmSupervisorList();
		mav.addObject("supervisorList", supervisorList);
		mav.setViewName("alarm/badAccountAlarmConfigInfo");
		return mav;
	}

	@RequestMapping(value = "/alarm/getRegistrationAlarmConfigList", method = RequestMethod.GET)
	public ModelAndView getRegistrationAlarmConfigList() {
		mav = new ModelAndView();
		List<CrmAlarm> list = crmAlarmManager.getCrmAlarmConfigList();
		mav.addObject("list", list);
		List<CrmSupervisor> supervisorList = crmAlarmManager.getCrmSupervisorList();
		mav.addObject("supervisorList", supervisorList);
		mav.setViewName("alarm/registrationAlarmConfigInfo");
		return mav;
	}

	@RequestMapping(value = "/alarm/getTotalGDQAlarmConfigList", method = RequestMethod.GET)
	public ModelAndView getTotalGDQAlarmConfigList() {
		mav = new ModelAndView();
		// 显示 总量，已售，剩余，剩余百分比
		BigDecimal totalAmountOfGDQ = new BigDecimal(
				configManager.getConfigStringValue(ConfigKeyEnum.TOTALGDQCANBESOLD, "100000000"));
		BigDecimal soldAmountOfGDQ = transferManager
				.getAccumulatedAmount(ServerConsts.REDISS_KEY_OF_TOTAL_ANMOUT_OF_GDQ);
		BigDecimal remainingAmountOfGDQ = totalAmountOfGDQ.subtract(soldAmountOfGDQ);
		BigDecimal percent = (remainingAmountOfGDQ).divide(totalAmountOfGDQ, 3, BigDecimal.ROUND_DOWN)
				.multiply(new BigDecimal("100"));

		List<CrmAlarm> list = crmAlarmManager.getCrmAlarmConfigList();
		List<CrmSupervisor> supervisorList = crmAlarmManager.getCrmSupervisorList();

		mav.addObject("list", list);
		mav.addObject("supervisorList", supervisorList);

		mav.addObject("totalAmountOfGDQ", totalAmountOfGDQ);
		mav.addObject("soldAmountOfGDQ", soldAmountOfGDQ);
		mav.addObject("remainingAmountOfGDQ", remainingAmountOfGDQ);
		mav.addObject("percent", percent);

		mav.setViewName("alarm/totalGDQAlarmConfigInfo");
		return mav;
	}

	@RequestMapping(value = "/alarm/delAlarmConfig", method = RequestMethod.GET)
	public ModelAndView delAlarmConfig(Integer alarmId, HttpServletRequest request, HttpServletResponse response) {
		mav = new ModelAndView();
		int alarmType = crmAlarmManager.delAlarmConfig(alarmId);
		mav.setViewName("redirect:/alarm/" + VIEWNAMEARR[alarmType]);

		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
				Operation.DELETE_ALARM.getOperationName(), alarmId.toString()));

		return mav;
	}

	@RequestMapping(value = "/alarm/updateAlarmConfig", method = RequestMethod.POST)
	public void updateAlarmConfig(HttpServletResponse response, Integer alarmId) {

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

	@RequestMapping(value = "/alarm/updateAlarmConfigInfo", method = RequestMethod.POST)
	public ModelAndView updateAlarmConfigInfo(UpdateAlarmConfigInfoRequest updateAlarmConfigInfoRequest,
			HttpServletRequest request, HttpServletResponse response) {
		mav = new ModelAndView();
		crmAlarmManager.updateAlarmConfig(updateAlarmConfigInfoRequest.getAlarmId(),
				updateAlarmConfigInfoRequest.getAlarmType(),
				updateAlarmConfigInfoRequest.getCriticalThresholdLowerLimit(),
				updateAlarmConfigInfoRequest.getCriticalThresholdUpperLimit(),
				updateAlarmConfigInfoRequest.getAlarmMode(), 0,
				JsonBinder.getInstance().toJson(updateAlarmConfigInfoRequest.getSupervisorId()));

		mav.setViewName("redirect:/alarm/" + VIEWNAMEARR[updateAlarmConfigInfoRequest.getAlarmType()]);

		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
				Operation.EDIT_ALARM.getOperationName(), updateAlarmConfigInfoRequest.toString()));

		return mav;
	}

	@RequestMapping(value = "/alarm/saveAlarmConfig", method = RequestMethod.POST)
	public ModelAndView saveAlarmConfig(SaveAlarmConfigRequest saveAlarmConfigRequest, HttpServletRequest request,
			HttpServletResponse response) {
		mav = new ModelAndView();
		crmAlarmManager.addAlarmConfig(saveAlarmConfigRequest.getAlarmType(),
				saveAlarmConfigRequest.getCriticalThresholdLowerLimit(),
				saveAlarmConfigRequest.getCriticalThresholdUpperLimit(), saveAlarmConfigRequest.getAlarmMode(), 0,
				JsonBinder.getInstance().toJson(saveAlarmConfigRequest.getSupervisorId()));

		mav.setViewName("redirect:/alarm/" + VIEWNAMEARR[saveAlarmConfigRequest.getAlarmType()]);

		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
				Operation.ADD_ALARM.getOperationName(), saveAlarmConfigRequest.toString()));

		return mav;
	}

	@RequestMapping(value = "/alarm/updateAlarmAvailable", method = RequestMethod.GET)
	public ModelAndView updateAlarmAvailable(Integer alarmId, int alarmAvailable, HttpServletRequest request,
			HttpServletResponse response) {
		mav = new ModelAndView();
		int alarmType = crmAlarmManager.updateAlarmAvailable(alarmId, alarmAvailable);
		if (alarmAvailable == 0) {
			crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
					Operation.OFF_ALARM.getOperationName(), alarmId.toString()));
		} else {
			crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
					Operation.ON_ALARM.getOperationName(), alarmId.toString()));
		}

		mav.setViewName("redirect:/alarm/" + VIEWNAMEARR[alarmType]);

		return mav;
	}

}
