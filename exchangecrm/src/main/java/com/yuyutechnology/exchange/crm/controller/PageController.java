package com.yuyutechnology.exchange.crm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {
	private static Logger logger = LogManager.getLogger(PageController.class);

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
}
