/**
 * 
 */
package com.yuyutechnology.exchange.server.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.server.controller.request.LoginRequest;
import com.yuyutechnology.exchange.server.controller.response.LoginResponse;

/**
 * @author silent.sun
 *
 */
@Controller
public class UserController {
	
	//sign in 登录
	@ResponseBody
	@ApiOperation(value = "登录", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public void login(@RequestBody LoginRequest pushToAll, HttpServletRequest request,
			HttpServletResponse response) {
		
	}
	//register 注册
	
	
	//Get registration code 获取注册验证码
	
}
