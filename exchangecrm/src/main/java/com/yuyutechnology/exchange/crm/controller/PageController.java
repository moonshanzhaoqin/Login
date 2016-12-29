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
	
	
}
