package com.yuyutechnology.exchange.crm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {
	private static Logger logger = LoggerFactory.getLogger(PageController.class);

	ModelAndView mav;

	@RequestMapping(value = "/login", method = { RequestMethod.GET })
	public String login(HttpServletRequest request, HttpServletResponse response) {
		return "login";
	}

	@RequestMapping(value = "/home", method = { RequestMethod.GET })
	public String home(HttpServletRequest request, HttpServletResponse response) {
		return "home";
	}

	@RequestMapping(value = "/userAssets", method = { RequestMethod.GET })
	public String userAssets(HttpServletRequest request, HttpServletResponse response) {
		return "userAssets";
	}

	@RequestMapping(value = "/warn", method = { RequestMethod.GET })
	public String warn(HttpServletRequest request, HttpServletResponse response) {
		return "warn";
	}

	@RequestMapping(value = "/currency", method = { RequestMethod.GET })
	public String currency(HttpServletRequest request, HttpServletResponse response) {
		return "currency";
	}

	@RequestMapping(value = "/config", method = { RequestMethod.GET })
	public String config(HttpServletRequest request, HttpServletResponse response) {
		return "config";
	}

	@RequestMapping(value = "/setting", method = { RequestMethod.GET })
	public String settings(HttpServletRequest request, HttpServletResponse response) {
		return "setting";
	}

}
