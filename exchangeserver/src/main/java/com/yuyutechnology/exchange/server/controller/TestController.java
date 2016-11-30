/**
 * 
 */
package com.yuyutechnology.exchange.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author silent.sun
 *
 */
@Controller
public class TestController {

	
	@ResponseBody
	@RequestMapping(value = "/test.do")
	public String getTrans() {
		return "Hello test";
	}
}
