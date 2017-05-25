/**
 * 
 */
package com.yuyutechnology.exchange.crm.controller;

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
import com.yuyutechnology.exchange.crm.request.GetUserInfoByPageRequest;
import com.yuyutechnology.exchange.manager.CrmUserInfoManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.util.page.PageBean;

/**
 * @author suzan.wu
 *
 */
@Controller
public class UserInfoController {
	private static Logger logger = LogManager.getLogger(UserInfoController.class);
	@Autowired
	UserManager userManager;
	@Autowired
	CrmUserInfoManager crmUserInfoManager;

	/**
	 * 获取用户信息
	 * 
	 * @param getWithdrawListRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserInfoByPage", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public PageBean getUserInfoByPage(@RequestBody GetUserInfoByPageRequest getWithdrawListRequest,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info(getWithdrawListRequest.toString());
		return crmUserInfoManager.getUserInfoByPage(Integer.parseInt(getWithdrawListRequest.getCurrentPage()),
				getWithdrawListRequest.getUserPhone(), getWithdrawListRequest.getUserName());
	}

	/**
	 * 立即更新用户信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/updateUserInfo", method = RequestMethod.GET)
	public void updateUserInfo(HttpServletRequest request, HttpServletResponse response) {
		crmUserInfoManager.updateImmediately();
	}

}
