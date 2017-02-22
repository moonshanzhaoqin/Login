package com.yuyutechnology.exchange.crm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
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
import com.yuyutechnology.exchange.manager.CrmAdminManager;

@Controller
public class AdminController {
	private static Logger logger = LogManager.getLogger(AdminController.class);

	@Autowired
	CrmAdminManager adminManager;

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
			String retCode = adminManager.login(loginRquest.getAdminName(), loginRquest.getAdminPassword());
			switch (retCode) {
			case RetCodeConsts.ADMIN_NOT_EXIST:
				mav.setViewName("login");
				mav.addObject("retCode", RetCodeConsts.ADMIN_NOT_EXIST);
				mav.addObject("message", "Admin不存在");
				break;
			case RetCodeConsts.PASSWORD_NOT_MATCH_NAME:
				mav.setViewName("login");
				mav.addObject("retCode", RetCodeConsts.PASSWORD_NOT_MATCH_NAME);
				mav.addObject("message", "用户名密码不匹配");
				break;
			case RetCodeConsts.RET_CODE_SUCCESS:
				// 写入session
				request.getSession().setAttribute("adminName", loginRquest.getAdminName());
				mav.setViewName("redirect:/account/getTotalAssetsDetails");
				break;
			default:
				break;
			}
		}
		return mav;
	}

	@ResponseBody
	@RequestMapping(value = "/modifyPassword", method = { RequestMethod.POST })
	public BaseResponse modifyPassword(@RequestBody ModifyPasswordRquest modifyPasswordRquest,
			HttpServletRequest request, HttpServletResponse response) {
		BaseResponse rep=new  BaseResponse();
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
		request.getSession().invalidate();
		return "login";
	}

}
