/**
 * 
 */
package com.yuyutechnology.exchange.server.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.server.controller.request.GetRegistrationCodeRequest;
import com.yuyutechnology.exchange.server.controller.request.GetVerificationCodeRequest;
import com.yuyutechnology.exchange.server.controller.request.LoginRequest;
import com.yuyutechnology.exchange.server.controller.request.RegisterRequest;
import com.yuyutechnology.exchange.server.controller.request.ResetPasswordRequest;
import com.yuyutechnology.exchange.server.controller.request.TestCodeRequest;
import com.yuyutechnology.exchange.server.controller.response.BaseResponse;
import com.yuyutechnology.exchange.server.controller.response.LoginResponse;

/**
 * @author suzan.wu
 *
 */
@Controller
public class UserController {
	public static Logger logger = LoggerFactory.getLogger(UserController.class);

	// sign in 登录
	@ResponseBody
	@ApiOperation(value = "登录", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletRequest request,
			HttpServletResponse response) {

		return null;
	}

	// register 注册
	@ResponseBody
	@ApiOperation(value = "注册", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public LoginResponse register(@RequestBody RegisterRequest registerRequest, HttpServletRequest request,
			HttpServletResponse response) {

		return null;
	}

	// Get registration code 获取注册验证码
	@ResponseBody
	@ApiOperation(value = "获取注册验证码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/getRegistrationCode", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse getRegistrationCode(@RequestBody GetRegistrationCodeRequest getRegistrationCodeRequest,
			HttpServletRequest request, HttpServletResponse response) {
		// 检验手机号是否已注册
		// 生成验证码
		// 存验证码
		// 发送验证码

		return null;
	}
	// Get Verification code 获取验证码
		@ResponseBody
		@ApiOperation(value = "获取验证码", httpMethod = "POST", notes = "")
		@RequestMapping(value = "/getVerificationCode", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
		public BaseResponse getVerificationCode(@RequestBody GetVerificationCodeRequest getVerificationCodeRequest,
				HttpServletRequest request, HttpServletResponse response) {
			// 检验手机号是否已注册
			// 生成验证码
			// 存验证码
			// 发送验证码

			return null;
		}
	// test code 测试验证码
	@ResponseBody
	@ApiOperation(value = "测试验证码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/testCode", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse testCode(@RequestBody TestCodeRequest testRequest, HttpServletRequest request,
			HttpServletResponse response) {

		return null;
	}

	// reset password 重置密码
	@ResponseBody
	@ApiOperation(value = "重置密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest,
			HttpServletRequest request, HttpServletResponse response) {

		return null;
	}
}
