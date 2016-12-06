/**
 * 
 */
package com.yuyutechnology.exchange.server.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.form.UserInfo;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.server.controller.request.ForgetPasswordRequest;
import com.yuyutechnology.exchange.server.controller.request.GetRegistrationCodeRequest;
import com.yuyutechnology.exchange.server.controller.request.GetVerificationCodeRequest;
import com.yuyutechnology.exchange.server.controller.request.LoginRequest;
import com.yuyutechnology.exchange.server.controller.request.RegisterRequest;
import com.yuyutechnology.exchange.server.controller.request.TestCodeRequest;
import com.yuyutechnology.exchange.server.controller.response.BaseResponse;
import com.yuyutechnology.exchange.server.controller.response.LoginResponse;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionDataHolder;
import com.yuyutechnology.exchange.session.SessionManager;

/**
 * @author suzan.wu
 *
 */
@Controller
@RequestMapping
public class UserController {
	public static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserManager userManager;
	@Autowired
	ExchangeManager exchangeManager;
	@Autowired
	SessionManager sessionManager;

	// sign in 登录
	@ResponseBody
	@ApiOperation(value = "登录", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletRequest request,
			HttpServletResponse response) {
		

		LoginResponse rep = new LoginResponse();
		Integer userId = userManager.login(loginRequest.getUserPhone(), loginRequest.getUserPassword());
		if (userId == null) {
			rep.setRetCode(ServerConsts.RET_CODE_FAILUE);
			rep.setMessage("");
		} else {
			//生成session  Token
			SessionData sessionData = SessionDataHolder.getSessionData();
			sessionData.setUserId(userId);
			sessionData.setBrowserLanguage(request.getParameter("language"));
			sessionData.setLogin(true);
			sessionManager.saveSessionData(sessionData);
			rep.setToken(sessionData.getSessionId());
			
			//获取用户信息
			UserInfo user = userManager.getUserInfo(userId);
			rep.setUser(user);
			
			//获取钱包信息
			List<Wallet> wallets = exchangeManager.getWalletsByUserId(userId);
			rep.setWallets(wallets);
			
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage("");
		}
		return rep;
	}

	// register 注册
	@ResponseBody
	@ApiOperation(value = "注册", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public LoginResponse register(@RequestBody RegisterRequest registerRequest, HttpServletRequest request,
			HttpServletResponse response) {
		LoginResponse rep = new LoginResponse();
		// 校验验证码
		if (userManager.testPinCode(registerRequest.getUserPhone(), registerRequest.getRegistrationCode())) {
			Integer userId = userManager.register(registerRequest.getUserPhone(), registerRequest.getUserName(),
					registerRequest.getUserPassword());
			if (userId == null) {
				rep.setRetCode(ServerConsts.RET_CODE_FAILUE);
				rep.setMessage("");
			} else {
				//生成session  Token
				SessionData sessionData = SessionDataHolder.getSessionData();
				sessionData.setUserId(userId);
				sessionData.setBrowserLanguage(request.getParameter("language"));
				sessionData.setLogin(true);
				sessionManager.saveSessionData(sessionData);
				rep.setToken(sessionData.getSessionId());

				//获取用户信息
				UserInfo user = userManager.getUserInfo(userId);
				rep.setUser(user);
				
				//获取钱包信息
				List<Wallet> wallets = exchangeManager.getWalletsByUserId(userId);
				rep.setWallets(wallets);
				
				rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
				rep.setMessage("");
			}
		} else {
			rep.setRetCode(ServerConsts.PHONE_AND_CODE_NOT_MATCH);
			rep.setMessage("");
		}
		return rep;
	}

	// Get registration code 获取注册验证码
	@ResponseBody
	@ApiOperation(value = "获取注册验证码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/getRegistrationCode", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse getRegistrationCode(@RequestBody GetRegistrationCodeRequest getRegistrationCodeRequest,
			HttpServletRequest request, HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		// 检验手机号是否已注册
		if (userManager.isUser(getRegistrationCodeRequest.getUserPhone())) {
			rep.setRetCode(ServerConsts.PHONE_IS_REGISTERED);
			rep.setMessage("");
		} else {
			userManager.getPinCode(getRegistrationCodeRequest.getUserPhone());
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage("");
		}
		return rep;
	}

	// Get Verification code 获取验证码(针对已注册用户)
	@ResponseBody
	@ApiOperation(value = "获取验证码(针对已注册用户)", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/getVerificationCode", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse getVerificationCode(@RequestBody GetVerificationCodeRequest getVerificationCodeRequest,
			HttpServletRequest request, HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		// 检验手机号是否存在
		if (userManager.isUser(getVerificationCodeRequest.getUserPhone())) {
			userManager.getPinCode(getVerificationCodeRequest.getUserPhone());
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage("");
		} else {
			rep.setRetCode(ServerConsts.PHONE_NOT_EXIST);
			rep.setMessage("");
		}
		return rep;
	}

	// test code 测试验证码
	@ResponseBody
	@ApiOperation(value = "测试验证码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/testCode", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse testCode(@RequestBody TestCodeRequest testRequest, HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		if (userManager.testPinCode(testRequest.getUserPhone(), testRequest.getVerificationCode())) {
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage("");
		} else {
			rep.setRetCode(ServerConsts.PHONE_AND_CODE_NOT_MATCH);
			rep.setMessage("");
		}
		return rep;
	}

	// forget password 忘记密码
	@ResponseBody
	@ApiOperation(value = "忘记密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/forgetPassword", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse forgetPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest,
			HttpServletRequest request, HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		if (userManager.testPinCode(forgetPasswordRequest.getUserPhone(), forgetPasswordRequest.getVerificationCode())) {
			// 修改密码
			userManager.resetPassword(forgetPasswordRequest.getUserPhone(),forgetPasswordRequest.getNewPassword());
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage("");
		} else {
			rep.setRetCode(ServerConsts.PHONE_AND_CODE_NOT_MATCH);
			rep.setMessage("");
		}
		return null;
	}
}
