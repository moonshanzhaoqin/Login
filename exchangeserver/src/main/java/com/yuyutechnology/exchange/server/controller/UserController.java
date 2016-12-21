/**
 * 
 */
package com.yuyutechnology.exchange.server.controller;

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
import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.AppVersion;
import com.yuyutechnology.exchange.server.controller.request.AppVersionRequest;
import com.yuyutechnology.exchange.server.controller.request.ForgetPasswordRequest;
import com.yuyutechnology.exchange.server.controller.request.GetVerificationCodeRequest;
import com.yuyutechnology.exchange.server.controller.request.LoginRequest;
import com.yuyutechnology.exchange.server.controller.request.RegisterRequest;
import com.yuyutechnology.exchange.server.controller.request.TestCodeRequest;
import com.yuyutechnology.exchange.server.controller.response.AppVersionResponse;
import com.yuyutechnology.exchange.server.controller.response.ForgetPasswordResponse;
import com.yuyutechnology.exchange.server.controller.response.GetCurrencyResponse;
import com.yuyutechnology.exchange.server.controller.response.GetVerificationCodeResponse;
import com.yuyutechnology.exchange.server.controller.response.LoginResponse;
import com.yuyutechnology.exchange.server.controller.response.RegisterResponse;
import com.yuyutechnology.exchange.server.controller.response.TestCodeResponse;
import com.yuyutechnology.exchange.server.session.SessionData;
import com.yuyutechnology.exchange.server.session.SessionManager;
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
	ExchangeManager exchangeManager;
	@Autowired
	SessionManager sessionManager;
	@Autowired
	UserManager userManager;
	@Autowired
	CommonManager commonManager;

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
	public ForgetPasswordResponse forgetPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("forgetPassword : {}", forgetPasswordRequest.getAreaCode() + forgetPasswordRequest.getUserPhone());
		ForgetPasswordResponse rep = new ForgetPasswordResponse();
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
				if (userId == null) {
					logger.info(MessageConsts.PHONE_NOT_EXIST);
					rep.setRetCode(ServerConsts.PHONE_NOT_EXIST);
					rep.setMessage(MessageConsts.PHONE_NOT_EXIST);
				} else if (userId == 0) {
					logger.info(MessageConsts.USER_BLOCKED);
					rep.setRetCode(ServerConsts.USER_BLOCKED);
					rep.setMessage(MessageConsts.USER_BLOCKED);
				} else {
					// 修改密码
					userManager.updatePassword(userId, forgetPasswordRequest.getNewPassword());
					sessionManager.delLoginToken(userId);
					logger.info(MessageConsts.RET_CODE_SUCCESS);
					rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
					rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
				}
				userManager.clearPinCode(ServerConsts.PIN_FUNC_FORGETPASSWORD, forgetPasswordRequest.getAreaCode(),
						forgetPasswordRequest.getUserPhone());
			} else {
				logger.info(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
				rep.setRetCode(ServerConsts.PHONE_AND_CODE_NOT_MATCH);
				rep.setMessage(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
			}
		}
		return rep;
	}

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
	public GetVerificationCodeResponse getVerificationCode(
			@RequestBody GetVerificationCodeRequest getVerificationCodeRequest, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("getVerificationCode : {}", getVerificationCodeRequest.toString());
		GetVerificationCodeResponse rep = new GetVerificationCodeResponse();
		if (getVerificationCodeRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			// 检验手机号是否存在
			Integer userId = userManager.getUserId(getVerificationCodeRequest.getAreaCode(),
					getVerificationCodeRequest.getUserPhone());
			if (getVerificationCodeRequest.getPurpose().equals(ServerConsts.PIN_FUNC_REGISTER)
					|| getVerificationCodeRequest.getPurpose().equals(ServerConsts.PIN_FUNC_CHANGEPHONE)) {
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
				if (userId == null) {
					logger.info(MessageConsts.PHONE_NOT_EXIST);
					rep.setRetCode(ServerConsts.PHONE_NOT_EXIST);
					rep.setMessage(MessageConsts.PHONE_NOT_EXIST);
				} else if (userId == 0) {
					logger.info(MessageConsts.USER_BLOCKED);
					rep.setRetCode(ServerConsts.USER_BLOCKED);
					rep.setMessage(MessageConsts.USER_BLOCKED);
				} else {
					userManager.getPinCode(getVerificationCodeRequest.getPurpose(),
							getVerificationCodeRequest.getAreaCode(), getVerificationCodeRequest.getUserPhone());
					logger.info(MessageConsts.RET_CODE_SUCCESS);
					rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
					rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
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
	@ApiOperation(value = "登录", httpMethod = "POST", notes = "userPassword  loginToken  二选一")
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("============login==========");
		LoginResponse rep = new LoginResponse();
		switch (loginRequest.isEmpty()) {
		case 1:
			// loginToken
			Integer userId = sessionManager.validateLoginToken(loginRequest.getLoginToken());
			if (userId == 0) {
				logger.info(MessageConsts.TOKEN_NOT_MATCH);
				rep.setRetCode(ServerConsts.TOKEN_NOT_MATCH);
				rep.setMessage(MessageConsts.TOKEN_NOT_MATCH);
			} else {
				// 记录登录信息
				userManager.updateUser(userId, HttpTookit.getIp(request), loginRequest.getPushId(),
						loginRequest.getLanguage());
				// 更新钱包
				userManager.updateWallet(userId);
				// 生成session Token
				SessionData sessionData = new SessionData(userId, UidUtils.genUid());
				sessionManager.saveSessionData(sessionData);
				rep.setSessionToken(sessionData.getSessionId());
				rep.setLoginToken(sessionManager.createLoginToken(userId));
				// 获取用户信息
				rep.setUser(userManager.getUserInfo(userId));

				logger.info(MessageConsts.RET_CODE_SUCCESS);
				rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			}
			break;
		case 2:
			// password
			userId = userManager.getUserId(loginRequest.getAreaCode(), loginRequest.getUserPhone());
			if (userId == null) {
				logger.info(MessageConsts.PHONE_NOT_EXIST);
				rep.setRetCode(ServerConsts.PHONE_NOT_EXIST);
				rep.setMessage(MessageConsts.PHONE_NOT_EXIST);
			} else if (userId == 0) {
				logger.info(MessageConsts.USER_BLOCKED);
				rep.setRetCode(ServerConsts.USER_BLOCKED);
				rep.setMessage(MessageConsts.USER_BLOCKED);
			} else if (userManager.checkUserPassword(userId, loginRequest.getUserPassword())) {
				// 记录登录信息
				userManager.updateUser(userId, HttpTookit.getIp(request), loginRequest.getPushId(),
						loginRequest.getLanguage());
				// 更新钱包
				userManager.updateWallet(userId);
				// 生成session Token
				SessionData sessionData = new SessionData(userId, UidUtils.genUid());
				sessionManager.saveSessionData(sessionData);
				rep.setSessionToken(sessionData.getSessionId());
				rep.setLoginToken(sessionManager.createLoginToken(userId));
				// 获取用户信息
				rep.setUser(userManager.getUserInfo(userId));

				logger.info(MessageConsts.RET_CODE_SUCCESS);
				rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			} else {
				logger.info(MessageConsts.PASSWORD_NOT_MATCH);
				rep.setRetCode(ServerConsts.PASSWORD_NOT_MATCH);
				rep.setMessage(MessageConsts.PASSWORD_NOT_MATCH);
			}
			break;
		default:
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
			break;
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
	public RegisterResponse register(@RequestBody RegisterRequest registerRequest, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("register : {}", registerRequest.getAreaCode() + registerRequest.getUserPhone());
		RegisterResponse rep = new RegisterResponse();
		if (registerRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			// 判断用户是否已注册
			if (userManager.getUserId(registerRequest.getAreaCode(), registerRequest.getUserPhone()) == null) {
				// 校验验证码
				if (userManager.testPinCode(ServerConsts.PIN_FUNC_REGISTER, registerRequest.getAreaCode(),
						registerRequest.getUserPhone(), registerRequest.getRegistrationCode())) {
					Integer userId = userManager.register(registerRequest.getAreaCode(), registerRequest.getUserPhone(),
							registerRequest.getUserName(), registerRequest.getUserPassword(), HttpTookit.getIp(request),
							registerRequest.getPushId(), registerRequest.getLanguage());

					logger.info("userId==={}", userId);
					if (userId == null) {
						logger.info(MessageConsts.RET_CODE_FAILUE);
						rep.setRetCode(ServerConsts.RET_CODE_FAILUE);
						rep.setMessage(MessageConsts.RET_CODE_FAILUE);
					} else {
						// 记录登录信息
						userManager.updateUser(userId, HttpTookit.getIp(request), registerRequest.getPushId(),
								registerRequest.getLanguage());
						// 生成session Token
						SessionData sessionData = new SessionData(userId, UidUtils.genUid());
						sessionManager.saveSessionData(sessionData);
						rep.setSessionToken(sessionData.getSessionId());
						rep.setLoginToken(sessionManager.createLoginToken(userId));
						// 获取用户信息
						rep.setUser(userManager.getUserInfo(userId));

						logger.info(MessageConsts.RET_CODE_SUCCESS);
						rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
						rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
					}

					userManager.clearPinCode(ServerConsts.PIN_FUNC_REGISTER, registerRequest.getAreaCode(),
							registerRequest.getUserPhone());
				} else {
					logger.info(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
					rep.setRetCode(ServerConsts.PHONE_AND_CODE_NOT_MATCH);
					rep.setMessage(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
				}
			} else {
				logger.info(MessageConsts.PHONE_IS_REGISTERED);
				rep.setRetCode(ServerConsts.PHONE_IS_REGISTERED);
				rep.setMessage(MessageConsts.PHONE_IS_REGISTERED);
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
	public TestCodeResponse testCode(@RequestBody TestCodeRequest testRequest, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("testCode : {}", testRequest.getAreaCode() + testRequest.getUserPhone());
		TestCodeResponse rep = new TestCodeResponse();
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

	/**
	 * getCurrency 获取货币列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "获取货币列表", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/getCurrency", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public GetCurrencyResponse getCurrency(HttpServletRequest request, HttpServletResponse response) {
		logger.info("getCurrency : ");
		GetCurrencyResponse rep = new GetCurrencyResponse();
		rep.setCurrencyInfos(userManager.getCurrency());
		logger.info(MessageConsts.RET_CODE_SUCCESS);
		rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		return rep;
	}

	/**
	 * appVersion 版本获取
	 * 
	 * @param appVersionRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "版本获取", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/appVersion", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public AppVersionResponse appVersion(@RequestBody AppVersionRequest appVersionRequest, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("appVersion : ");
		AppVersionResponse rep = new AppVersionResponse();
		if (appVersionRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			AppVersion appVersion = commonManager.getAppVersion(appVersionRequest.getPlatformType(),
					appVersionRequest.getUpdateWay());
			if (appVersion == null) {
				logger.info(MessageConsts.RET_CODE_FAILUE);
				rep.setRetCode(ServerConsts.RET_CODE_FAILUE);
				rep.setMessage(MessageConsts.RET_CODE_FAILUE);
			} else if (appVersion.getAppVersionNum().equals(appVersionRequest.getAppVersionNum())) {
				logger.info(MessageConsts.VERSION_NUM_IS_LATEST);
				rep.setRetCode(ServerConsts.VERSION_NUM_IS_LATEST);
				rep.setMessage(MessageConsts.VERSION_NUM_IS_LATEST);
			} else {
				rep.setAppVersion(appVersion);
				logger.info(MessageConsts.RET_CODE_SUCCESS);
				rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			}
		}
		return rep;
	}
}
