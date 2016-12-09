/**
 * 
 */
package com.yuyutechnology.exchange.server.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.form.UserInfo;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.User;
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
import com.yuyutechnology.exchange.session.SessionManager;
import com.yuyutechnology.exchange.utils.HttpTookit;
import com.yuyutechnology.exchange.utils.UidUtils;

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

	/**
	 * forget password 忘记密码
	 * 
	 * @param forgetPasswordRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "忘记密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/forgetPassword", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse forgetPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("forgetPassword : {}", forgetPasswordRequest.getAreaCode() + forgetPasswordRequest.getUserPhone());
		BaseResponse rep = new BaseResponse();
		if (forgetPasswordRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			// 验证码校验
			if (userManager.testPinCode(ServerConsts.PIN_FUNC_FORGETPASSWORD, forgetPasswordRequest.getAreaCode(),
					forgetPasswordRequest.getUserPhone(), forgetPasswordRequest.getVerificationCode())) {
				Integer userId = userManager.getUserId(forgetPasswordRequest.getAreaCode(),
						forgetPasswordRequest.getUserPhone());
				if (userId != null) {
					// 修改密码
					userManager.updatePassword(userId, forgetPasswordRequest.getNewPassword());
					sessionManager.delLoginToken(userId);
					logger.info(MessageConsts.RET_CODE_SUCCESS);
					rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
					rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
				} else {
					logger.info(MessageConsts.PHONE_NOT_EXIST);
					rep.setRetCode(ServerConsts.PHONE_NOT_EXIST);
					rep.setMessage(MessageConsts.PHONE_NOT_EXIST);
				}
			} else {
				logger.info(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
				rep.setRetCode(ServerConsts.PHONE_AND_CODE_NOT_MATCH);
				rep.setMessage(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
			}
		}
		return rep;
	}

	/**
	 * Get registration code 获取注册验证码
	 * 
	 * @param getRegistrationCodeRequest
	 * @param request
	 * @param response
	 * @return
	 */
	// @ResponseBody
	// @ApiOperation(value = "获取注册验证码", httpMethod = "POST", notes = "")
	// @RequestMapping(value = "/getRegistrationCode", method =
	// RequestMethod.POST, produces = "application/json; charset=utf-8")
	// public BaseResponse getRegistrationCode(@RequestBody
	// GetRegistrationCodeRequest getRegistrationCodeRequest,
	// HttpServletRequest request, HttpServletResponse response) {
	// logger.info("========getRegistrationCode : {}============",
	// getRegistrationCodeRequest.getAreaCode() +
	// getRegistrationCodeRequest.getUserPhone());
	// BaseResponse rep = new BaseResponse();
	// if (getRegistrationCodeRequest.isEmpty()) {
	// logger.info("PARAMETER_IS_EMPTY");
	// rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
	// rep.setMessage("");
	// } else {
	// // 检验手机号是否已注册
	// if (userManager.getUserId(getRegistrationCodeRequest.getAreaCode(),
	// getRegistrationCodeRequest.getUserPhone()) != null) {
	// logger.info("PHONE_IS_REGISTERED");
	// rep.setRetCode(ServerConsts.PHONE_IS_REGISTERED);
	// rep.setMessage("");
	// } else {
	// userManager.getPinCode(getRegistrationCodeRequest.getAreaCode(),
	// getRegistrationCodeRequest.getUserPhone());
	// logger.info("********Operation succeeded********");
	// rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
	// rep.setMessage("");
	// }
	// }
	// return rep;
	// }

	/**
	 * Get Verification code 获取验证码
	 * 
	 * @param getVerificationCodeRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "获取验证码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/getVerificationCode", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse getVerificationCode(@RequestBody GetVerificationCodeRequest getVerificationCodeRequest,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("getVerificationCode : {}", getVerificationCodeRequest.toString());
		BaseResponse rep = new BaseResponse();
		if (getVerificationCodeRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			// 检验手机号是否存在
			Integer userId = userManager.getUserId(getVerificationCodeRequest.getAreaCode(),
					getVerificationCodeRequest.getUserPhone());
			if (getVerificationCodeRequest.getPurpose().equals(ServerConsts.PIN_FUNC_REGISTER)) {
				if (userId != null) {
					logger.info(MessageConsts.PHONE_IS_REGISTERED);
					rep.setRetCode(ServerConsts.PHONE_IS_REGISTERED);
					rep.setMessage(MessageConsts.PHONE_IS_REGISTERED);
				} else {
					userManager.getPinCode(getVerificationCodeRequest.getPurpose(),
							getVerificationCodeRequest.getAreaCode(), getVerificationCodeRequest.getUserPhone());
					logger.info(MessageConsts.RET_CODE_SUCCESS);
					rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
					rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
				}
			} else {
				if (userId != null) {
					userManager.getPinCode(getVerificationCodeRequest.getPurpose(),
							getVerificationCodeRequest.getAreaCode(), getVerificationCodeRequest.getUserPhone());
					logger.info(MessageConsts.RET_CODE_SUCCESS);
					rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
					rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
				} else {
					logger.info(MessageConsts.PHONE_NOT_EXIST);
					rep.setRetCode(ServerConsts.PHONE_NOT_EXIST);
					rep.setMessage(MessageConsts.PHONE_NOT_EXIST);
				}
			}
		}
		return rep;
	}

	/**
	 * sign in 登录
	 * 
	 * @param loginRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "登录", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("login : {}", loginRequest.getAreaCode() + loginRequest.getUserPhone());
		LoginResponse rep = new LoginResponse();
		if (loginRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			Integer userId = userManager.getUserId(loginRequest.getAreaCode(), loginRequest.getUserPhone());
			if (userId == null) {
				logger.info(MessageConsts.PHONE_NOT_EXIST);
				rep.setRetCode(ServerConsts.PHONE_NOT_EXIST);
				rep.setMessage(MessageConsts.PHONE_NOT_EXIST);
				// 有logintoken则跳过password验证
			} else if (sessionManager.validateLoginToken(userId, loginRequest.getLoginToken())
					|| userManager.checkUserPassword(userId, loginRequest.getUserPassword())) {
				// 生成session Token
				SessionData sessionData = new SessionData(userId, UidUtils.genUid());
				sessionManager.saveSessionData(sessionData);
				rep.setSessionToken(sessionData.getSessionId());
				rep.setLoginToken(sessionManager.createLoginToken(userId));
				// 获取用户信息
				UserInfo user = userManager.getUserInfo(userId);
				rep.setUser(user);

				// 获取钱包信息
				List<Wallet> wallets = exchangeManager.getWalletsByUserId(userId);
				rep.setWallets(wallets);

				logger.info(MessageConsts.RET_CODE_SUCCESS);
				rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			}
		}
		return rep;
	}

	/**
	 * register 注册
	 * 
	 * @param registerRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "注册", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public LoginResponse register(@RequestBody RegisterRequest registerRequest, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("register : {}", registerRequest.getAreaCode() + registerRequest.getUserPhone());
		LoginResponse rep = new LoginResponse();
		if (registerRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			// 校验验证码
			if (userManager.testPinCode(ServerConsts.PIN_FUNC_REGISTER, registerRequest.getAreaCode(),
					registerRequest.getUserPhone(), registerRequest.getRegistrationCode())) {
				Integer userId = userManager.register(registerRequest.getAreaCode(), registerRequest.getUserPhone(),
						registerRequest.getUserName(), registerRequest.getUserPassword());
				logger.info("userId==={}", userId);
				if (userId == null) {
					logger.info(MessageConsts.RET_CODE_FAILUE);
					rep.setRetCode(ServerConsts.RET_CODE_FAILUE);
					rep.setMessage(MessageConsts.RET_CODE_FAILUE);
				} else {
					// 生成session Token
					SessionData sessionData = new SessionData(userId, UidUtils.genUid());
					sessionManager.saveSessionData(sessionData);
					rep.setSessionToken(sessionData.getSessionId());
					rep.setLoginToken(sessionManager.createLoginToken(userId));
					// 获取用户信息
					UserInfo user = userManager.getUserInfo(userId);
					rep.setUser(user);

					// 获取钱包信息
					List<Wallet> wallets = exchangeManager.getWalletsByUserId(userId);
					rep.setWallets(wallets);

					logger.info(MessageConsts.RET_CODE_SUCCESS);
					rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
					rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
				}
			} else {
				logger.info(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
				rep.setRetCode(ServerConsts.PHONE_AND_CODE_NOT_MATCH);
				rep.setMessage(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
			}
		}
		return rep;
	}

	/**
	 * test code 测试验证码
	 * 
	 * @param testRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "测试验证码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/testCode", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse testCode(@RequestBody TestCodeRequest testRequest, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("testCode : {}", testRequest.getAreaCode() + testRequest.getUserPhone());
		BaseResponse rep = new BaseResponse();
		if (testRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			// 校验验证码
			if (userManager.testPinCode(testRequest.getPurpose(), testRequest.getAreaCode(), testRequest.getUserPhone(),
					testRequest.getVerificationCode())) {
				logger.info(MessageConsts.RET_CODE_SUCCESS);
				rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			} else {
				logger.info(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
				rep.setRetCode(ServerConsts.PHONE_AND_CODE_NOT_MATCH);
				rep.setMessage(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
			}
		}
		return rep;
	}
}
