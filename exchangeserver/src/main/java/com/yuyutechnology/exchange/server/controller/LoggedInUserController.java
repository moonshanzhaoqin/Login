/**
 * 
 */
package com.yuyutechnology.exchange.server.controller;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.yuyutechnology.exchange.dto.MsgFlagInfo;
import com.yuyutechnology.exchange.enums.UserConfigKeyEnum;
import com.yuyutechnology.exchange.mail.MailManager;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.server.controller.request.BindGoldpayRequest;
import com.yuyutechnology.exchange.server.controller.request.ChangeGoldpayRequest;
import com.yuyutechnology.exchange.server.controller.request.ChangePhoneRequest;
import com.yuyutechnology.exchange.server.controller.request.CheckGoldpayRequest;
import com.yuyutechnology.exchange.server.controller.request.CheckPayPwdRequest;
import com.yuyutechnology.exchange.server.controller.request.ContactUsRequest;
import com.yuyutechnology.exchange.server.controller.request.GetUserConfigRequest;
import com.yuyutechnology.exchange.server.controller.request.LogoutRequest;
import com.yuyutechnology.exchange.server.controller.request.ModifyPasswordRequest;
import com.yuyutechnology.exchange.server.controller.request.ModifyPayPwdByOldRequest;
import com.yuyutechnology.exchange.server.controller.request.ModifyUserNameRequest;
import com.yuyutechnology.exchange.server.controller.request.ResetPayPwdRequest;
import com.yuyutechnology.exchange.server.controller.request.SetUserPayPwdRequest;
import com.yuyutechnology.exchange.server.controller.request.SwitchLanguageRequest;
import com.yuyutechnology.exchange.server.controller.response.BindGoldpayResponse;
import com.yuyutechnology.exchange.server.controller.response.ChangeGoldpayResponse;
import com.yuyutechnology.exchange.server.controller.response.ChangePhoneResponse;
import com.yuyutechnology.exchange.server.controller.response.CheckChangePhoneResponse;
import com.yuyutechnology.exchange.server.controller.response.CheckGoldpayResponse;
import com.yuyutechnology.exchange.server.controller.response.CheckPayPwdResponse;
import com.yuyutechnology.exchange.server.controller.response.ContactUsResponse;
import com.yuyutechnology.exchange.server.controller.response.GetMsgFlagResponse;
import com.yuyutechnology.exchange.server.controller.response.GetUserConfigResponse;
import com.yuyutechnology.exchange.server.controller.response.LogoutResponse;
import com.yuyutechnology.exchange.server.controller.response.ModifyPasswordResponse;
import com.yuyutechnology.exchange.server.controller.response.ModifyPayPwdByOldResponse;
import com.yuyutechnology.exchange.server.controller.response.ModifyUserNameResponse;
import com.yuyutechnology.exchange.server.controller.response.ResetPayPwdResponse;
import com.yuyutechnology.exchange.server.controller.response.SetUserPayPwdResponse;
import com.yuyutechnology.exchange.server.controller.response.SwitchLanguageResponse;
import com.yuyutechnology.exchange.server.security.annotation.RequestDecryptBody;
import com.yuyutechnology.exchange.server.security.annotation.ResponseEncryptBody;
import com.yuyutechnology.exchange.server.session.SessionData;
import com.yuyutechnology.exchange.server.session.SessionDataHolder;
import com.yuyutechnology.exchange.server.session.SessionManager;

/**
 * @author suzan.wu
 */
@Controller
public class LoggedInUserController {
	public static Logger logger = LogManager.getLogger(LoggedInUserController.class);

	@Autowired
	UserManager userManager;
	@Autowired
	ExchangeManager exchangeManager;
	@Autowired
	SessionManager sessionManager;
	@Autowired
	MailManager mailManager;
	@Autowired
	CommonManager commonManager;

	/**
	 * 绑定goldpay
	 * 
	 * @param token
	 * @param bindGoldpayRequest
	 * @return
	 */
	@ResponseEncryptBody
	@ApiOperation(value = "绑定goldpay", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/bindGoldpay", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BindGoldpayResponse bindGoldpay(@PathVariable String token,
			@RequestDecryptBody BindGoldpayRequest bindGoldpayRequest) {
		logger.info("========bindGoldpay : {}============", token);
		BindGoldpayResponse rep = new BindGoldpayResponse();
		if (bindGoldpayRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			String retCode = userManager.bindGoldpay(sessionData.getUserId(), bindGoldpayRequest.getGoldpayToken());
			switch (retCode) {
			case RetCodeConsts.RET_CODE_SUCCESS:
				// 获取用户信息
				rep.setUser(userManager.getUserInfo(sessionData.getUserId()));
				logger.info("********Operation succeeded********");
				rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
				break;
			case RetCodeConsts.GOLDPAY_PHONE_IS_NOT_EXIST:
				logger.info(MessageConsts.GOLDPAY_PHONE_IS_NOT_EXIST);
				rep.setRetCode(RetCodeConsts.GOLDPAY_PHONE_IS_NOT_EXIST);
				rep.setMessage(MessageConsts.GOLDPAY_PHONE_IS_NOT_EXIST);
				break;
			default:
				logger.info(MessageConsts.RET_CODE_FAILUE);
				rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
				rep.setMessage(MessageConsts.RET_CODE_FAILUE);
				break;
			}
		}
		return rep;
	}

	/**
	 * 换绑goldpay
	 * 
	 * @param token
	 * @param bindGoldpayRequest
	 * @return
	 */
	@ResponseEncryptBody
	@ApiOperation(value = "换绑goldpay", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/changeGoldpay", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ChangeGoldpayResponse changeGoldpay(@PathVariable String token,
			@RequestDecryptBody ChangeGoldpayRequest changeGoldpayRequest) {
		logger.info("========changeGoldpay : {}============", token);
		ChangeGoldpayResponse rep = new ChangeGoldpayResponse();
		if (changeGoldpayRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			if (sessionManager.validateCheckToken(sessionData.getUserId(), ServerConsts.PAYPWD_CHANGEGOLDPAY,
					changeGoldpayRequest.getCheckToken())) {
				String retCode = userManager.bindGoldpay(sessionData.getUserId(),
						changeGoldpayRequest.getGoldpayToken());
				switch (retCode) {
				case RetCodeConsts.RET_CODE_SUCCESS:
					// 获取用户信息
					rep.setUser(userManager.getUserInfo(sessionData.getUserId()));
					logger.info("********Operation succeeded********");
					rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
					rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
					sessionManager.delCheckToken(sessionData.getUserId(), ServerConsts.PAYPWD_CHANGEGOLDPAY);
					break;
				case RetCodeConsts.GOLDPAY_PHONE_IS_NOT_EXIST:
					logger.info(MessageConsts.GOLDPAY_PHONE_IS_NOT_EXIST);
					rep.setRetCode(RetCodeConsts.GOLDPAY_PHONE_IS_NOT_EXIST);
					rep.setMessage(MessageConsts.GOLDPAY_PHONE_IS_NOT_EXIST);
					break;
				default:
					logger.info(MessageConsts.RET_CODE_FAILUE);
					rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
					rep.setMessage(MessageConsts.RET_CODE_FAILUE);
					break;
				}
			} else {
				logger.info("***checkToken is wrong!***");
				rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
				rep.setMessage(MessageConsts.RET_CODE_FAILUE);
			}
		}
		return rep;
	}

	/**
	 * changePhone 换绑手机
	 * 
	 * @param token
	 * @param changePhoneRequest
	 * @return
	 * @throws ParseException
	 */
	@ResponseEncryptBody
	@ApiOperation(value = "换绑手机", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/changePhone", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ChangePhoneResponse changePhone(@PathVariable String token,
			@RequestDecryptBody ChangePhoneRequest changePhoneRequest) throws ParseException {
		logger.info("========changePhone : {}============", token);
		ChangePhoneResponse rep = new ChangePhoneResponse();
		if (changePhoneRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			long time = userManager.checkChangePhoneTime(sessionData.getUserId());
			if (time > new Date().getTime()) {
				logger.info(MessageConsts.TIME_NOT_ARRIVED);
				rep.setRetCode(RetCodeConsts.TIME_NOT_ARRIVED);
				rep.setMessage(MessageConsts.TIME_NOT_ARRIVED);
				rep.setTime(time);
//				rep.setOpts(new String(time));
			} else if (sessionManager.validateCheckToken(sessionData.getUserId(), ServerConsts.PAYPWD_CHANGEPHONE,
					changePhoneRequest.getCheckToken())) {
				// 校验手机验证码
				if (userManager.testPinCode(ServerConsts.PIN_FUNC_CHANGEPHONE, changePhoneRequest.getAreaCode(),
						changePhoneRequest.getUserPhone(), changePhoneRequest.getVerificationCode())) {
					userManager.changePhone(sessionData.getUserId(), changePhoneRequest.getAreaCode(),
							changePhoneRequest.getUserPhone());
					sessionManager.cleanSession(sessionData.getSessionId());
					sessionManager.delLoginToken(sessionData.getUserId());
					logger.info("********Operation succeeded********");
					rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
					rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
					userManager.clearPinCode(ServerConsts.PIN_FUNC_CHANGEPHONE, changePhoneRequest.getAreaCode(),
							changePhoneRequest.getUserPhone());
					sessionManager.delCheckToken(sessionData.getUserId(), ServerConsts.PAYPWD_CHANGEPHONE);
				} else {
					logger.info(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
					rep.setRetCode(RetCodeConsts.PHONE_AND_CODE_NOT_MATCH);
					rep.setMessage(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
				}
			} else {
				logger.info("***checkToken is wrong!***");
				rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
				rep.setMessage(MessageConsts.RET_CODE_FAILUE);
			}
		}
		return rep;
	}

	/**
	 * checkChangePhone 校验换绑手机时间
	 * 
	 * @param token
	 * @return
	 * @throws ParseException
	 */
	@ResponseEncryptBody
	@ApiOperation(value = "校验换绑手机", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/checkChangePhone", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public CheckChangePhoneResponse checkChangePhone(@PathVariable String token) throws ParseException {
		logger.info("========checkChangePhone : {}============", token);
		CheckChangePhoneResponse rep = new CheckChangePhoneResponse();
		SessionData sessionData = SessionDataHolder.getSessionData();
		long time = userManager.checkChangePhoneTime(sessionData.getUserId());
		if (time > new Date().getTime()) {
			logger.info(MessageConsts.TIME_NOT_ARRIVED);
			rep.setRetCode(RetCodeConsts.TIME_NOT_ARRIVED);
			rep.setMessage(MessageConsts.TIME_NOT_ARRIVED);
			rep.setTime(time);
		} else {
			logger.info("********Operation succeeded********");
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		}
		return rep;
	}

	/**
	 * checkPassword 校验登录密码
	 * 
	 * @param token
	 * @param checkPasswordRequest
	 * @return
	 */
	// @ResponseEncryptBody
	// @ApiOperation(value = "校验登录密码", httpMethod = "POST", notes = "")
	// @RequestMapping(value = "/token/{token}/user/checkPassword", method =
	// RequestMethod.POST, produces = "application/json; charset=utf-8")
	// public CheckPasswordResponse checkPassword(@PathVariable String token,
	// @RequestDecryptBody CheckPasswordRequest checkPasswordRequest) {
	// logger.info("========checkPassword : {}============", token);
	// CheckPasswordResponse rep = new CheckPasswordResponse();
	// SessionData sessionData = SessionDataHolder.getSessionData();
	// if (userManager.checkUserPassword(sessionData.getUserId(),
	// checkPasswordRequest.getUserPassword())) {
	// logger.info("********Operation succeeded********");
	// rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
	// rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
	// } else {
	// logger.info(MessageConsts.PASSWORD_NOT_MATCH);
	// rep.setRetCode(RetCodeConsts.PASSWORD_NOT_MATCH);
	// rep.setMessage(MessageConsts.PASSWORD_NOT_MATCH);
	// }
	// return rep;
	// }

	/**
	 * checkPayPwd 校验支付密码
	 * 
	 * @param token
	 * @param checkPayPwdRequest
	 * @return
	 */
	@ResponseEncryptBody
	@ApiOperation(value = "校验支付密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/checkPayPwd", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public CheckPayPwdResponse checkPayPwd(@PathVariable String token,
			@RequestDecryptBody CheckPayPwdRequest checkPayPwdRequest) {
		logger.info("========checkPayPwd : {}============", token);
		CheckPayPwdResponse rep = new CheckPayPwdResponse();
		if (checkPayPwdRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			CheckPwdResult result = userManager.checkPayPassword(sessionData.getUserId(),
					checkPayPwdRequest.getUserPayPwd());
			switch (result.getStatus()) {
			case ServerConsts.CHECKPWD_STATUS_FREEZE:
				logger.info(MessageConsts.PAY_FREEZE);
				rep.setRetCode(RetCodeConsts.PAY_FREEZE);
				rep.setMessage(String.valueOf(result.getInfo()));
				break;

			case ServerConsts.CHECKPWD_STATUS_INCORRECT:
				logger.info(MessageConsts.PAY_PWD_NOT_MATCH);
				rep.setRetCode(RetCodeConsts.PAY_PWD_NOT_MATCH);
				rep.setMessage(String.valueOf(result.getInfo()));
				break;

			case ServerConsts.CHECKPWD_STATUS_CORRECT:
				String checkToken = sessionManager.createCheckToken(sessionData.getUserId(),
						checkPayPwdRequest.getPurpose());
				logger.info("********Operation succeeded********");
				rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
				rep.setMessage(checkToken);
				break;
			}
		}
		return rep;
	}

	/**
	 * checkGoldpayPwd 校验Goldpay
	 * 
	 * @param token
	 * @param checkPayPwdRequest
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "校验Goldpay", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/checkGoldpay", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public CheckGoldpayResponse checkGoldpay(@PathVariable String token,
			@RequestBody CheckGoldpayRequest checkPayGoldpayRequest) {
		logger.info("========checkPassword : {}============", token);
		CheckGoldpayResponse rep = new CheckGoldpayResponse();
		if (checkPayGoldpayRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			String retCode = userManager.checkGoldpay(sessionData.getUserId(), checkPayGoldpayRequest.getGoldpayName(),
					checkPayGoldpayRequest.getGoldpayPwd());
			switch (retCode) {
			case RetCodeConsts.RET_CODE_SUCCESS:
				String checkToken = sessionManager.createCheckToken(sessionData.getUserId(), ServerConsts.RESETPAYPWD);
				logger.info("********Operation succeeded********");
				rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
				rep.setMessage(checkToken);
				break;
			case RetCodeConsts.GOLDPAY_IS_INCORRECT:
				logger.info(MessageConsts.GOLDPAY_IS_INCORRECT);
				rep.setRetCode(RetCodeConsts.GOLDPAY_IS_INCORRECT);
				rep.setMessage(MessageConsts.GOLDPAY_IS_INCORRECT);
				break;
			case RetCodeConsts.GOLDPAY_NOT_BIND:
				logger.info(MessageConsts.GOLDPAY_NOT_BIND);
				rep.setRetCode(RetCodeConsts.GOLDPAY_NOT_BIND);
				rep.setMessage(MessageConsts.GOLDPAY_NOT_BIND);
				break;
			case RetCodeConsts.GOLDPAY_NOT_MATCH_BIND:
				logger.info(MessageConsts.GOLDPAY_NOT_MATCH_BIND);
				rep.setRetCode(RetCodeConsts.GOLDPAY_NOT_MATCH_BIND);
				rep.setMessage(MessageConsts.GOLDPAY_NOT_MATCH_BIND);
				break;
			}
		}
		return rep;
	}

	/**
	 * Modify password 修改用户密码
	 * 
	 * @param token
	 * @param modifyPasswordRequest
	 * @return
	 */
	@ResponseEncryptBody
	@ApiOperation(value = "修改用户密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/modifyPassword", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ModifyPasswordResponse modifyPassword(@PathVariable String token,
			@RequestDecryptBody ModifyPasswordRequest modifyPasswordRequest) {
		logger.info("========modifyPassword : {}============", token);
		ModifyPasswordResponse rep = new ModifyPasswordResponse();
		if (modifyPasswordRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			CheckPwdResult result = userManager.checkLoginPassword(sessionData.getUserId(),
					modifyPasswordRequest.getOldPassword());
			switch (result.getStatus()) {
			case ServerConsts.CHECKPWD_STATUS_FREEZE:
				logger.info(MessageConsts.LOGIN_FREEZE);
				rep.setRetCode(RetCodeConsts.LOGIN_FREEZE);
				rep.setMessage(String.valueOf(result.getInfo()));
				break;
			case ServerConsts.CHECKPWD_STATUS_INCORRECT:
				logger.info(MessageConsts.PASSWORD_NOT_MATCH);
				rep.setRetCode(RetCodeConsts.PASSWORD_NOT_MATCH);
				rep.setMessage(String.valueOf(result.getInfo()));
				break;
			case ServerConsts.CHECKPWD_STATUS_CORRECT:
				if (modifyPasswordRequest.getOldPassword().equals(modifyPasswordRequest.getNewPassword())) {
					logger.info(MessageConsts.NEW_PWD_EQUALS_OLD);
					rep.setRetCode(RetCodeConsts.NEW_PWD_EQUALS_OLD);
					rep.setMessage(MessageConsts.NEW_PWD_EQUALS_OLD);
				} else {
					userManager.updatePassword(sessionData.getUserId(), modifyPasswordRequest.getNewPassword());
					sessionManager.cleanSession(sessionData.getSessionId());
					sessionManager.delLoginToken(sessionData.getUserId());
					logger.info("********Operation succeeded********");
					rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
					rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
				}
				break;
			}
		}
		return rep;
	}

	/**
	 * 修改用户名 Modify userName
	 * 
	 * @param token
	 * @param modifyUserNameRequest
	 * @return
	 */
	@ResponseEncryptBody
	@ApiOperation(value = "修改用户名", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/modifyUserName", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ModifyUserNameResponse modifyUserName(@PathVariable String token,
			@RequestDecryptBody ModifyUserNameRequest modifyUserNameRequest) {
		logger.info("========modifyUserName : {}============", token);
		ModifyUserNameResponse rep = new ModifyUserNameResponse();

		if (modifyUserNameRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			userManager.updateUserName(sessionData.getUserId(), modifyUserNameRequest.getNewUserName());
			logger.info("********Operation succeeded********");
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		}
		return rep;
	}

	/**
	 * 设置支付密码
	 * 
	 * @param token
	 * @param setUserPayPwdRequest
	 * @return
	 */
	@ResponseEncryptBody
	@ApiOperation(value = "设置支付密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/setUserPayPwd", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public SetUserPayPwdResponse setUserPayPwd(@PathVariable String token,
			@RequestDecryptBody SetUserPayPwdRequest setUserPayPwdRequest) {
		logger.info("========setUserPayPwd : {}============", token);
		SetUserPayPwdResponse rep = new SetUserPayPwdResponse();
		if (setUserPayPwdRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			// PayPwd 6位数字
			if (setUserPayPwdRequest.getUserPayPwd().length() == 6
					&& StringUtils.isNumeric(setUserPayPwdRequest.getUserPayPwd())) {
				userManager.updateUserPayPwd(sessionData.getUserId(), setUserPayPwdRequest.getUserPayPwd());
				logger.info("********Operation succeeded********");
				rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			} else {
				logger.info(MessageConsts.PAY_PASSWORD_IS_ILLEGAL);
				rep.setRetCode(RetCodeConsts.PAY_PASSWORD_IS_ILLEGAL);
				rep.setMessage(MessageConsts.PAY_PASSWORD_IS_ILLEGAL);
			}
		}
		return rep;
	}

	/**
	 * 通过原支付密码更换支付密码
	 * 
	 * @param token
	 * @param modifyPayPwdByOldRequest
	 * @return
	 */
	@ResponseEncryptBody
	@ApiOperation(value = "通过原支付密码更换支付密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/modifyPayPwdByOld", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ModifyPayPwdByOldResponse modifyPayPwdByOld(@PathVariable String token,
			@RequestDecryptBody ModifyPayPwdByOldRequest modifyPayPwdByOldRequest) {
		logger.info("========modifyPayPwdByOld : {}============", token);
		ModifyPayPwdByOldResponse rep = new ModifyPayPwdByOldResponse();
		if (modifyPayPwdByOldRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			if (sessionManager.validateCheckToken(sessionData.getUserId(), ServerConsts.PAYPWD_MODIFYPAYPWD,
					modifyPayPwdByOldRequest.getCheckToken())) {
				// PayPwd 6位数字
				if (modifyPayPwdByOldRequest.getNewUserPayPwd().length() == 6
						&& StringUtils.isNumeric(modifyPayPwdByOldRequest.getNewUserPayPwd())) {
					if (userManager.isUserPayPwdEqualsOld(sessionData.getUserId(),
							modifyPayPwdByOldRequest.getNewUserPayPwd())) {
						logger.info(MessageConsts.NEW_PWD_EQUALS_OLD);
						rep.setRetCode(RetCodeConsts.NEW_PWD_EQUALS_OLD);
						rep.setMessage(MessageConsts.NEW_PWD_EQUALS_OLD);
					} else {
						userManager.updateUserPayPwd(sessionData.getUserId(),
								modifyPayPwdByOldRequest.getNewUserPayPwd());
						logger.info("********Operation succeeded********");
						rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
						rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
						sessionManager.delCheckToken(sessionData.getUserId(), ServerConsts.PAYPWD_MODIFYPAYPWD);
					}
				} else {
					logger.info(MessageConsts.PAY_PASSWORD_IS_ILLEGAL);
					rep.setRetCode(RetCodeConsts.PAY_PASSWORD_IS_ILLEGAL);
					rep.setMessage(MessageConsts.PAY_PASSWORD_IS_ILLEGAL);
				}
			} else {
				logger.info("***checkToken is wrong!***");
				rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
				rep.setMessage(MessageConsts.RET_CODE_FAILUE);
			}

		}
		return rep;
	}

	/**
	 * ResetPayPwd 通过第三方重置支付密码
	 * 
	 * @param token
	 * @param modifyPayPwdByGoldRequest
	 * @return
	 */
	@ResponseEncryptBody
	@ApiOperation(value = "通过第三方重置支付密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/resetPayPwd", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ResetPayPwdResponse resetPayPwd(@PathVariable String token,
			@RequestDecryptBody ResetPayPwdRequest resetPayPwdRequest) {
		logger.info("========modifyPayPwdByOld : {}============", token);
		ResetPayPwdResponse rep = new ResetPayPwdResponse();
		if (resetPayPwdRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			if (sessionManager.validateCheckToken(sessionData.getUserId(), ServerConsts.RESETPAYPWD,
					resetPayPwdRequest.getCheckToken())) {
				// PayPwd 6位数字
				if (resetPayPwdRequest.getNewUserPayPwd().length() == 6
						&& StringUtils.isNumeric(resetPayPwdRequest.getNewUserPayPwd())) {
					userManager.updateUserPayPwd(sessionData.getUserId(), resetPayPwdRequest.getNewUserPayPwd());
					logger.info("********Operation succeeded********");
					rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
					rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
					sessionManager.delCheckToken(sessionData.getUserId(), ServerConsts.RESETPAYPWD);
				} else {
					logger.info(MessageConsts.PAY_PASSWORD_IS_ILLEGAL);
					rep.setRetCode(RetCodeConsts.PAY_PASSWORD_IS_ILLEGAL);
					rep.setMessage(MessageConsts.PAY_PASSWORD_IS_ILLEGAL);
				}
			} else {
				logger.info("***checkToken is wrong!***");
				rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
				rep.setMessage(MessageConsts.RET_CODE_FAILUE);
			}
		}
		return rep;
	}

	/**
	 * switchLanguage切换语言
	 * 
	 * @param token
	 * @param switchLanguageRequest
	 * @return
	 */
	@ResponseEncryptBody
	@ApiOperation(value = "切换语言", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/switchLanguage", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public SwitchLanguageResponse switchLanguage(@PathVariable String token,
			@RequestDecryptBody SwitchLanguageRequest switchLanguageRequest) {
		logger.info("========switchLanguage : {}============", token);
		SwitchLanguageResponse rep = new SwitchLanguageResponse();

		if (switchLanguageRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			userManager.switchLanguage(sessionData.getUserId(), switchLanguageRequest.getLanguage());
			logger.info("********Operation succeeded********");
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		}

		return rep;
	}

	/**
	 * 退出账号 Logout
	 * 
	 * @param token
	 * @return
	 */
	@ResponseEncryptBody
	@ApiOperation(value = "退出账号", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/logout", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public LogoutResponse logout(@PathVariable String token, @RequestDecryptBody LogoutRequest logoutRequest) {
		logger.info("========logout : {}============", token);
		LogoutResponse rep = new LogoutResponse();
		SessionData sessionData = SessionDataHolder.getSessionData();
		int userId = 0;
		SessionData activeSessionData = sessionManager.getByUserid(userId);
		if (sessionData == null && activeSessionData == null) {
			userId = logoutRequest.getUserId();
		} else if (sessionData != null) {
			userId = sessionData.getUserId();
			sessionManager.cleanSession(sessionData.getSessionId());
		}
		sessionManager.delLoginToken(userId);
		userManager.logout(userId);
		logger.info("********Operation succeeded********");
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		return rep;
	}

	

	@ApiOperation(value = "获取消息红点标志位")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/user/getMsgFlag")
	public @ResponseEncryptBody GetMsgFlagResponse getMsgFlag(@PathVariable String token) {
		SessionData sessionData = SessionDataHolder.getSessionData();
		GetMsgFlagResponse rep = new GetMsgFlagResponse();
		MsgFlagInfo msgFlagInfo = commonManager.getMsgFlag(sessionData.getUserId());
		rep.setNewRequestTrans(msgFlagInfo.isNewRequestTrans());
		rep.setNewTrans(msgFlagInfo.isNewTrans());
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		return rep;
	}

	@ResponseEncryptBody
	@ApiOperation(value = "获取用户配置参数", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/getUserConfig", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public GetUserConfigResponse getUserConfig(@PathVariable String token,
			@RequestDecryptBody GetUserConfigRequest getUserConfigRequest) {
		GetUserConfigResponse rep = new GetUserConfigResponse();
		if (getUserConfigRequest == null || StringUtils.isBlank(getUserConfigRequest.getKey())) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			UserConfigKeyEnum key = null;
			try {
				key = UserConfigKeyEnum.valueOf(getUserConfigRequest.getKey());
			} catch (Exception e) {
			}
			if (key == null) {
				logger.warn("No enum constant com.yuyutechnology.exchange.enums.UserConfigKeyEnum."
						+ getUserConfigRequest.getKey());
				rep.setRetCode(RetCodeConsts.PARAMETER_IS_EMPTY);
				rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
				return rep;
			}
			String value = userManager.getUserConfigAndUpdate(sessionData.getUserId(),
					UserConfigKeyEnum.valueOf(getUserConfigRequest.getKey()), getUserConfigRequest.getValue());
			rep.setKey(getUserConfigRequest.getKey());
			rep.setValue(value);
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
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
	@ResponseEncryptBody
	@ApiOperation(value = "联系我们", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/contactUs", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ContactUsResponse contactUs(@PathVariable String token,@RequestDecryptBody ContactUsRequest contactUsRequest) {
		logger.info("========contactUs : {}============",token);
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
