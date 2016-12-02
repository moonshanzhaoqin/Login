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
import com.yuyutechnology.exchange.server.controller.request.GetRegistrationCodeRequest;
import com.yuyutechnology.exchange.server.controller.request.LoginRequest;
import com.yuyutechnology.exchange.server.controller.request.RegisterRequest;
import com.yuyutechnology.exchange.server.controller.response.GetRegistrationCodeResponse;
import com.yuyutechnology.exchange.server.controller.response.LoginResponse;
import com.yuyutechnology.exchange.server.controller.response.RegisterResponse;

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
	public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletRequest request,
			HttpServletResponse response) {
				
		
		return null;
	}
	//register 注册
	@ResponseBody
	@ApiOperation(value = "注册", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public RegisterResponse register(@RequestBody RegisterRequest registerRequest, HttpServletRequest request,
			HttpServletResponse response) {
				
		
		return null;
	}
	
	//Get registration code 获取注册验证码
	@ResponseBody
	@ApiOperation(value = "获取注册验证码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/get_registration_code", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public GetRegistrationCodeResponse getRegistrationCode(@RequestBody GetRegistrationCodeRequest getRegistrationCodeRequest, HttpServletRequest request,
			HttpServletResponse response) {
				
		
		return null;
	}
}
