/**
 * 
 */
package com.yuyutechnology.exchange.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.session.SessionManager;

/**
 * @author suzan.wu
 *
 */
@Controller
@RequestMapping("/token/{token}/user/")
public class LoggedInUserController {
	public static Logger logger = LoggerFactory.getLogger(LoggedInUserController.class);

	@Autowired
	UserManager userManager;
	@Autowired
	ExchangeManager exchangeManager;
	@Autowired
	SessionManager sessionManager;
	
	//TODO  修改密码
	
	//TODO 设置支付密码
	
	//TODO 修改支付密码
	
	//TODO 绑定goldpay
	
	
}
