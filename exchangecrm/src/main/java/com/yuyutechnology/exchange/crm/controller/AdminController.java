package com.yuyutechnology.exchange.crm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.crm.request.LoginRquest;
import com.yuyutechnology.exchange.crm.request.ModifyPasswordRquest;
import com.yuyutechnology.exchange.manager.AdminManager;

@Controller
public class AdminController {
	private static Logger logger = LoggerFactory.getLogger(PageController.class);

	@Autowired
	AdminManager adminManager;

	ModelAndView mav;

	@RequestMapping(value = "/dologin", method = { RequestMethod.POST })
	public ModelAndView dologin(LoginRquest loginRquest, HttpServletRequest request, HttpServletResponse response) {
		mav = new ModelAndView();

		if (loginRquest.isEmpty()) {
			logger.info("parameter is empty");

		} else {
			int retCode = adminManager.login(loginRquest.getAdminName(), loginRquest.getAdminPassword());
			switch (retCode) {
			case RetCodeConsts.ADMIN_NOT_EXIST:
				mav.setViewName("login");
				mav.addObject("retCode", RetCodeConsts.ADMIN_NOT_EXIST);
				mav.addObject("message", "ADMIN_NOT_EXIST");
				break;
			case RetCodeConsts.PASSWORD_NOT_MATCH_NAME:
				mav.setViewName("login");
				mav.addObject("retCode", RetCodeConsts.PASSWORD_NOT_MATCH_NAME);
				mav.addObject("message", "PASSWORD_NOT_MATCH_NAME");
				break;
			case RetCodeConsts.SUCCESS:
				// 写入session
				request.getSession().setAttribute("adminName", loginRquest.getAdminName());
				mav.setViewName("home");
				mav.addObject("retCode", RetCodeConsts.SUCCESS);
				break;
			default:
				break;
			}

		}

		return mav;
	}

	@RequestMapping(value = "/modifyPassword", method = { RequestMethod.POST })
	public ModelAndView modifyPassword(@RequestBody ModifyPasswordRquest modifyPasswordRquest,
			HttpServletRequest request, HttpServletResponse response) {
		mav = new ModelAndView();
		if (modifyPasswordRquest.isEmpty()) {
			logger.info("parameter is empty");
		} else {
			String adminName = (String) request.getSession().getAttribute("adminName");
			int retCode = adminManager.modifyPassword(adminName, modifyPasswordRquest.getOldPassword(),
					modifyPasswordRquest.getNewPassword());
			switch (retCode) {
			case RetCodeConsts.PASSWORD_NOT_MATCH_NAME:
				mav.setViewName("setting");
				mav.addObject("retCode", RetCodeConsts.PASSWORD_NOT_MATCH_NAME);
				mav.addObject("message", "PASSWORD_NOT_MATCH_NAME");
				break;
			case RetCodeConsts.SUCCESS:
				mav.setViewName("home");
				mav.addObject("retCode", RetCodeConsts.SUCCESS);
				break;
			default:
				break;
			}
		}
		return mav;
	}

	@RequestMapping(value = "/exit", method = RequestMethod.GET)
	public String exit(HttpServletRequest request) {
		request.getSession().invalidate();
		return "login";
	}

}
