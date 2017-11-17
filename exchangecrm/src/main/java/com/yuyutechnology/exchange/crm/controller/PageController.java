package com.yuyutechnology.exchange.crm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {

	@RequestMapping(value = "/login", method = { RequestMethod.GET })
	public String login(HttpServletRequest request, HttpServletResponse response) {
		return "login";
	}

	@RequestMapping(value = "/currency", method = { RequestMethod.GET })
	public String currency(HttpServletRequest request, HttpServletResponse response) {
		return "currency";
	}

	@RequestMapping(value = "/config", method = { RequestMethod.GET })
	public String config(HttpServletRequest request, HttpServletResponse response) {
		return "config";
	}

	@RequestMapping(value = "/badAccount", method = { RequestMethod.GET })
	public String badAccount(HttpServletRequest request, HttpServletResponse response) {
		return "badAccount";
	}

	@RequestMapping(value = "/userInfo", method = { RequestMethod.GET })
	public String userInfo(HttpServletRequest request, HttpServletResponse response) {
		return "userInfo";
	}

	@RequestMapping(value = "/recharge", method = { RequestMethod.GET })
	public String recharge(HttpServletRequest request, HttpServletResponse response) {
		return "recharge";
	}

	@RequestMapping(value = "/campaign", method = { RequestMethod.GET })
	public String campaign(HttpServletRequest request, HttpServletResponse response) {
		return "campaign";
	}

	@RequestMapping(value = "/registration", method = { RequestMethod.GET })
	public String registration(HttpServletRequest request, HttpServletResponse response) {
		return "registration";
	}

	@RequestMapping(value = "/feeTemplate", method = { RequestMethod.GET })
	public String feeTemplate(HttpServletRequest request, HttpServletResponse response) {
		return "feeTemplate";
	}

	@RequestMapping(value = "/withdraw", method = { RequestMethod.GET })
	public String withdraw(HttpServletRequest request, HttpServletResponse response) {
		return "withdraw";
	}
}
