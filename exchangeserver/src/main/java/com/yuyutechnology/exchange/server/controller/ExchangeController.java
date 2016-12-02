package com.yuyutechnology.exchange.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/exchange")
public class ExchangeController {
	
	public static Logger logger = LoggerFactory.getLogger(ExchangeController.class);
	
	
}
