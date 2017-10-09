package com.yuyutechnology.exchange.crm.controller;

import java.util.Date;

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
import org.springframework.web.servlet.ModelAndView;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.crm.reponse.BaseResponse;
import com.yuyutechnology.exchange.crm.request.LoginRquest;
import com.yuyutechnology.exchange.crm.request.ModifyPasswordRquest;
import com.yuyutechnology.exchange.enums.Operation;
import com.yuyutechnology.exchange.manager.CrmAdminManager;
import com.yuyutechnology.exchange.manager.CrmLogManager;
import com.yuyutechnology.exchange.pojo.Admin;
import com.yuyutechnology.exchange.pojo.CrmLog;
import com.yuyutechnology.exchange.util.HttpClientUtils;

@Controller
public class AdminController {
	private static Logger logger = LogManager.getLogger(AdminController.class);

	@Autowired
	CrmAdminManager adminManager;
	@Autowired
	CrmLogManager crmLogManager;

	ModelAndView mav;

	@RequestMapping(value = "/dologin", method = { RequestMethod.POST })
	public ModelAndView dologin(LoginRquest loginRquest, HttpServletRequest request, HttpServletResponse response) {
		mav = new ModelAndView();

		if (loginRquest.isEmpty()) {
			logger.info("parameter is empty");
			mav.setViewName("login");
			mav.addObject("retCode", RetCodeConsts.PARAMETER_IS_EMPTY);
			mav.addObject("message", "请输入用户名、密码");
		} else {
			Admin admin = adminManager.getAdminByName(loginRquest.getAdminName());
			if (admin == null) {
				mav.setViewName("login");
				mav.addObject("retCode", RetCodeConsts.ADMIN_NOT_EXIST);
				mav.addObject("message", "用戶不存在");
			} else if (adminManager.checkPassword(admin.getAdminId(), loginRquest.getAdminPassword())) {
				// 写入session
				request.getSession().setAttribute("adminName", admin.getAdminName());
				request.getSession().setAttribute("adminPower", admin.getAdminPower());
				mav.setViewName("redirect:/exchangeRate/getAllExchangeRates");

				crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
						Operation.ADMIN_LOGIN.getOperationName(), HttpClientUtils.getIP(request)));

			} else {
				mav.setViewName("login");
				mav.addObject("retCode", RetCodeConsts.PASSWORD_NOT_MATCH_NAME);
				mav.addObject("message", "用户名密码不匹配");
			}
		}
		return mav;
	}

	@ResponseBody
	@RequestMapping(value = "/modifyPassword", method = { RequestMethod.POST })
	public BaseResponse modifyPassword(@RequestBody ModifyPasswordRquest modifyPasswordRquest,
			HttpServletRequest request, HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		if (modifyPasswordRquest.isEmpty()) {
			logger.info("parameter is empty");
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
		} else {
			String adminName = (String) request.getSession().getAttribute("adminName");
			String retCode = adminManager.modifyPassword(adminName, modifyPasswordRquest.getOldPassword(),
					modifyPasswordRquest.getNewPassword());
			switch (retCode) {
			case RetCodeConsts.RET_CODE_SUCCESS:
				rep.setMessage("modifyPassword sucess");
				break;
			case RetCodeConsts.PASSWORD_NOT_MATCH_NAME:
				rep.setMessage("oldPassword is wrong");
				break;
			default:
				break;
			}
			rep.setRetCode(retCode);
		}
		return rep;
	}

	@RequestMapping(value = "/exit", method = RequestMethod.GET)
	public String exit(HttpServletRequest request) {
		crmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
				Operation.ADMIN_LOGOUT.getOperationName(), HttpClientUtils.getIP(request)));
		request.getSession().invalidate();
		return "login";
	}

}
