/**
 * 
 */
package com.yuyutechnology.exchange.server.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dto.CheckPwdResult;
import com.yuyutechnology.exchange.mail.MailManager;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.server.controller.request.ContactUsRequest;
import com.yuyutechnology.exchange.server.controller.request.ForgetPasswordRequest;
import com.yuyutechnology.exchange.server.controller.request.GetVerificationCodeRequest;
import com.yuyutechnology.exchange.server.controller.request.LoginRequest;
import com.yuyutechnology.exchange.server.controller.request.LoginValidateRequest;
import com.yuyutechnology.exchange.server.controller.request.RegisterRequest;
import com.yuyutechnology.exchange.server.controller.request.TestCodeRequest;
import com.yuyutechnology.exchange.server.controller.response.ContactUsResponse;
import com.yuyutechnology.exchange.server.controller.response.ForgetPasswordResponse;
import com.yuyutechnology.exchange.server.controller.response.GetVerificationCodeResponse;
import com.yuyutechnology.exchange.server.controller.response.LoginResponse;
import com.yuyutechnology.exchange.server.controller.response.LoginValidateResponse;
import com.yuyutechnology.exchange.server.controller.response.RegisterResponse;
import com.yuyutechnology.exchange.server.controller.response.TestCodeResponse;
import com.yuyutechnology.exchange.server.security.annotation.RequestDecryptBody;
import com.yuyutechnology.exchange.server.security.annotation.ResponseEncryptBody;
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
	public static Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	ExchangeManager exchangeManager;
	@Autowired
	SessionManager sessionManager;
	@Autowired
	UserManager userManager;
	@Autowired
	CommonManager commonManager;
	@Autowired
	MailManager mailManager;

	/**
	 * forget password 忘记密码
	 * 
	 * @param forgetPasswordRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseEncryptBody
	@ApiOperation(value = "忘记密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/forgetPassword", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ForgetPasswordResponse forgetPassword(@RequestDecryptBody ForgetPasswordRequest forgetPasswordRequest,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("forgetPassword : {}", forgetPasswordRequest.getAreaCode() + forgetPasswordRequest.getUserPhone());
		ForgetPasswordResponse rep = new ForgetPasswordResponse();
		if (forgetPasswordRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			// 验证码校验
			if (userManager.testPinCode(ServerConsts.PIN_FUNC_FORGETPASSWORD, forgetPasswordRequest.getAreaCode(),
					forgetPasswordRequest.getUserPhone(), forgetPasswordRequest.getVerificationCode())) {
				Integer userId = userManager.getUserId(forgetPasswordRequest.getAreaCode(),
						forgetPasswordRequest.getUserPhone());
				if (userId == null) {
					logger.info(MessageConsts.PHONE_NOT_EXIST);
					rep.setRetCode(RetCodeConsts.PHONE_NOT_EXIST);
					rep.setMessage(MessageConsts.PHONE_NOT_EXIST);
				} else if (userId == 0) {
					logger.info(MessageConsts.USER_BLOCKED);
					rep.setRetCode(RetCodeConsts.USER_BLOCKED);
					rep.setMessage(MessageConsts.USER_BLOCKED);
				} else {
					// 修改密码
					userManager.updatePassword(userId, forgetPasswordRequest.getNewPassword());
					sessionManager.delLoginToken(userId);
					logger.info(MessageConsts.RET_CODE_SUCCESS);
					rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
					rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
				}
				userManager.clearPinCode(ServerConsts.PIN_FUNC_FORGETPASSWORD, forgetPasswordRequest.getAreaCode(),
						forgetPasswordRequest.getUserPhone());
			} else {
				logger.info(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
				rep.setRetCode(RetCodeConsts.PHONE_AND_CODE_NOT_MATCH);
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
	@ResponseEncryptBody
	@ApiOperation(value = "获取验证码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/getVerificationCode", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public GetVerificationCodeResponse getVerificationCode(
			@RequestDecryptBody GetVerificationCodeRequest getVerificationCodeRequest, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("getVerificationCode : {}", getVerificationCodeRequest.toString());
		GetVerificationCodeResponse rep = new GetVerificationCodeResponse();
		if (getVerificationCodeRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			// 检验手机号是否存在
			Integer userId = userManager.getUserId(getVerificationCodeRequest.getAreaCode(),
					getVerificationCodeRequest.getUserPhone());
			if (getVerificationCodeRequest.getPurpose().equals(ServerConsts.PIN_FUNC_REGISTER)
					|| getVerificationCodeRequest.getPurpose().equals(ServerConsts.PIN_FUNC_CHANGEPHONE)) {
				if (userId != null) {
					logger.info(MessageConsts.PHONE_IS_REGISTERED);
					rep.setRetCode(RetCodeConsts.PHONE_IS_REGISTERED);
					rep.setMessage(MessageConsts.PHONE_IS_REGISTERED);
				} else {
					userManager.getPinCode(getVerificationCodeRequest.getPurpose(),
							getVerificationCodeRequest.getAreaCode(), getVerificationCodeRequest.getUserPhone());
					logger.info(MessageConsts.RET_CODE_SUCCESS);
					rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
					rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
				}
			} else {
				if (userId == null) {
					logger.info(MessageConsts.PHONE_NOT_EXIST);
					rep.setRetCode(RetCodeConsts.PHONE_NOT_EXIST);
					rep.setMessage(MessageConsts.PHONE_NOT_EXIST);
				} else if (userId == 0) {
					logger.info(MessageConsts.USER_BLOCKED);
					rep.setRetCode(RetCodeConsts.USER_BLOCKED);
					rep.setMessage(MessageConsts.USER_BLOCKED);
				} else {
					userManager.getPinCode(getVerificationCodeRequest.getPurpose(),
							getVerificationCodeRequest.getAreaCode(), getVerificationCodeRequest.getUserPhone());
					logger.info(MessageConsts.RET_CODE_SUCCESS);
					rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
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
	@ResponseEncryptBody
	@ApiOperation(value = "登录", httpMethod = "POST", notes = "userPassword  loginToken  二选一")
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public LoginResponse login(@RequestDecryptBody LoginRequest loginRequest, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("============login==========");
		LoginResponse rep = new LoginResponse();
		switch (loginRequest.isEmpty()) {
		case 1:
			// loginToken
			Integer userId = sessionManager.validateLoginToken(loginRequest.getLoginToken());
			if (userId == 0) {
				logger.info(MessageConsts.TOKEN_NOT_MATCH);
				rep.setRetCode(RetCodeConsts.TOKEN_NOT_MATCH);
				rep.setMessage(MessageConsts.TOKEN_NOT_MATCH);
			}else{
				// 生成session Token
				SessionData sessionData = new SessionData(userId, UidUtils.genUid());
				sessionManager.saveSessionData(sessionData);
				rep.setSessionToken(sessionData.getSessionId());
				rep.setLoginToken(sessionManager.createLoginToken(userId));
				// 记录登录信息
				userManager.updateUser(userId, HttpTookit.getIp(request), loginRequest.getPushId(),
						loginRequest.getLanguage());
				// 更新钱包
				userManager.updateWallet(userId);
				// 获取用户信息
				rep.setUser(userManager.getUserInfo(userId));

				logger.info(MessageConsts.RET_CODE_SUCCESS);
				rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);

			}
			break;
		case 2:
			// password
			userId = userManager.getUserId(loginRequest.getAreaCode(), loginRequest.getUserPhone());
			if (userId == null) {
				logger.info(MessageConsts.PHONE_NOT_EXIST);
				rep.setRetCode(RetCodeConsts.PHONE_NOT_EXIST);
				rep.setMessage(MessageConsts.PHONE_NOT_EXIST);
			} else if (userId == 0) {
				logger.info(MessageConsts.USER_BLOCKED);
				rep.setRetCode(RetCodeConsts.USER_BLOCKED);
				rep.setMessage(MessageConsts.USER_BLOCKED);
			} else {
				CheckPwdResult result = userManager.checkLoginPassword(userId, loginRequest.getUserPassword());
				switch (result.getStatus()) {
				case ServerConsts.CHECKPWD_STATUS_CORRECT:
					if (userManager.isNewDevice(userId, loginRequest.getDeviceId())){
						// 新设备，需要手机验证
//						userManager.getPinCode(ServerConsts.PIN_FUNC_NEWDEVICE, loginRequest.getAreaCode(), loginRequest.getUserPhone());
						logger.info(MessageConsts.NEW_DEVICE);
						rep.setRetCode(RetCodeConsts.NEW_DEVICE);
						rep.setMessage(MessageConsts.NEW_DEVICE);
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
						rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
						rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
					}
					break;
				case ServerConsts.CHECKPWD_STATUS_INCORRECT:
					logger.info(MessageConsts.PASSWORD_NOT_MATCH);
					rep.setRetCode(RetCodeConsts.PASSWORD_NOT_MATCH);
					rep.setMessage(String.valueOf(result.getInfo()));
					break;
				case ServerConsts.CHECKPWD_STATUS_FREEZE:
					logger.info(MessageConsts.LOGIN_FREEZE);
					rep.setRetCode(RetCodeConsts.LOGIN_FREEZE);
					rep.setMessage(String.valueOf(result.getInfo()));
					break;
				default:
					break;
				}
			}
			break;
		default:
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
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
	@ResponseEncryptBody
	@ApiOperation(value = "注册", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public RegisterResponse register(@RequestDecryptBody RegisterRequest registerRequest, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("register : {}", registerRequest.getAreaCode() + registerRequest.getUserPhone());
		RegisterResponse rep = new RegisterResponse();
		if (registerRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
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
						rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
						rep.setMessage(MessageConsts.RET_CODE_FAILUE);
					} else {
						// 设备登记
						userManager.addDevice(userId, registerRequest.getDeviceId(), registerRequest.getDeviceName());
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
						rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
						rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
					}

					userManager.clearPinCode(ServerConsts.PIN_FUNC_REGISTER, registerRequest.getAreaCode(),
							registerRequest.getUserPhone());
				} else {
					logger.info(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
					rep.setRetCode(RetCodeConsts.PHONE_AND_CODE_NOT_MATCH);
					rep.setMessage(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
				}
			} else {
				logger.info(MessageConsts.PHONE_IS_REGISTERED);
				rep.setRetCode(RetCodeConsts.PHONE_IS_REGISTERED);
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
	@ResponseEncryptBody
	@ApiOperation(value = "测试验证码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/testCode", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public TestCodeResponse testCode(@RequestDecryptBody TestCodeRequest testRequest, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("testCode : {}", testRequest.getAreaCode() + testRequest.getUserPhone());
		TestCodeResponse rep = new TestCodeResponse();
		if (testRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			// 校验验证码
			if (userManager.testPinCode(testRequest.getPurpose(), testRequest.getAreaCode(), testRequest.getUserPhone(),
					testRequest.getVerificationCode())) {
				logger.info(MessageConsts.RET_CODE_SUCCESS);
				rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			} else {
				logger.info(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
				rep.setRetCode(RetCodeConsts.PHONE_AND_CODE_NOT_MATCH);
				rep.setMessage(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
			}
		}
		return rep;
	}

	// 登录验证 loginValidate
	@ResponseEncryptBody
	@ApiOperation(value = "登录验证", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/loginValidate", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public LoginValidateResponse loginValidate(@RequestDecryptBody LoginValidateRequest loginValidateRequest,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("loginValidate : ");
		LoginValidateResponse rep = new LoginValidateResponse();
		if (loginValidateRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			Integer userId = userManager.getUserId(loginValidateRequest.getAreaCode(),
					loginValidateRequest.getUserPhone());
			if (userId == null) {
				logger.info(MessageConsts.PHONE_NOT_EXIST);
				rep.setRetCode(RetCodeConsts.PHONE_NOT_EXIST);
				rep.setMessage(MessageConsts.PHONE_NOT_EXIST);
			} else if (userId == 0) {
				logger.info(MessageConsts.USER_BLOCKED);
				rep.setRetCode(RetCodeConsts.USER_BLOCKED);
				rep.setMessage(MessageConsts.USER_BLOCKED);
			} else {
				CheckPwdResult result = userManager.checkLoginPassword(userId, loginValidateRequest.getUserPassword());
				switch (result.getStatus()) {
				case ServerConsts.CHECKPWD_STATUS_CORRECT:
					// 验证短信验证码
					if (userManager.testPinCode(ServerConsts.PIN_FUNC_NEWDEVICE, loginValidateRequest.getAreaCode(),
							loginValidateRequest.getUserPhone(), loginValidateRequest.getVerificationCode())) {
						// 设备登记
						userManager.addDevice(userId, loginValidateRequest.getDeviceId(),
								loginValidateRequest.getDeviceName());
						// 记录登录信息
						userManager.updateUser(userId, HttpTookit.getIp(request), loginValidateRequest.getPushId(),
								loginValidateRequest.getLanguage());
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
						rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
						rep.setMessage(MessageConsts.RET_CODE_SUCCESS);

						userManager.clearPinCode(ServerConsts.PIN_FUNC_REGISTER, loginValidateRequest.getAreaCode(),
								loginValidateRequest.getUserPhone());
					} else {
						logger.info(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
						rep.setRetCode(RetCodeConsts.PHONE_AND_CODE_NOT_MATCH);
						rep.setMessage(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
					}
					break;
				case ServerConsts.CHECKPWD_STATUS_INCORRECT:
					logger.info(MessageConsts.PASSWORD_NOT_MATCH);
					rep.setRetCode(RetCodeConsts.PASSWORD_NOT_MATCH);
					rep.setMessage(String.valueOf(result.getInfo()));
					break;
				case ServerConsts.CHECKPWD_STATUS_FREEZE:
					logger.info(MessageConsts.LOGIN_FREEZE);
					rep.setRetCode(RetCodeConsts.LOGIN_FREEZE);
					rep.setMessage(String.valueOf(result.getInfo()));
					break;
				default:
					break;
				}
			}
		}
		return rep;
	}
	/**
	 * contactUs 联系我们
	 * 
	 * @param token
	 * @param contactUsRequest
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "联系我们", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/contactUs4Web", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ContactUsResponse contactUs(@RequestBody ContactUsRequest contactUsRequest) {
		logger.info("========contactUs : {}============");
		ContactUsResponse rep = new ContactUsResponse();
		if (contactUsRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			mailManager.mail4contact(contactUsRequest.getName(), contactUsRequest.getEmail(),
					contactUsRequest.getCategory(), contactUsRequest.getEnquiry());
			logger.info("********Operation succeeded********");
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		}
		return rep;
	}
}
