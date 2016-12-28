/**
 * 
 */
package com.yuyutechnology.exchange.server.controller;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dto.MsgFlagInfo;
import com.yuyutechnology.exchange.mail.MailManager;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.server.controller.request.BindGoldpayRequest;
import com.yuyutechnology.exchange.server.controller.request.ChangePhoneRequest;
import com.yuyutechnology.exchange.server.controller.request.CheckGoldpayPwdRequest;
import com.yuyutechnology.exchange.server.controller.request.CheckPasswordRequest;
import com.yuyutechnology.exchange.server.controller.request.CheckPayPwdRequest;
import com.yuyutechnology.exchange.server.controller.request.ContactUsRequest;
import com.yuyutechnology.exchange.server.controller.request.LogoutRequest;
import com.yuyutechnology.exchange.server.controller.request.ModifyPasswordRequest;
import com.yuyutechnology.exchange.server.controller.request.ModifyPayPwdByGoldpayRequest;
import com.yuyutechnology.exchange.server.controller.request.ModifyPayPwdByOldRequest;
import com.yuyutechnology.exchange.server.controller.request.ModifyUserNameRequest;
import com.yuyutechnology.exchange.server.controller.request.SetUserPayPwdRequest;
import com.yuyutechnology.exchange.server.controller.request.SwitchLanguageRequest;
import com.yuyutechnology.exchange.server.controller.response.BindGoldpayResponse;
import com.yuyutechnology.exchange.server.controller.response.ChangePhoneResponse;
import com.yuyutechnology.exchange.server.controller.response.CheckChangePhoneResponse;
import com.yuyutechnology.exchange.server.controller.response.CheckGoldpayPwdResponse;
import com.yuyutechnology.exchange.server.controller.response.CheckPasswordResponse;
import com.yuyutechnology.exchange.server.controller.response.CheckPayPwdResponse;
import com.yuyutechnology.exchange.server.controller.response.ContactUsResponse;
import com.yuyutechnology.exchange.server.controller.response.GetMsgFlagResponse;
import com.yuyutechnology.exchange.server.controller.response.LogoutResponse;
import com.yuyutechnology.exchange.server.controller.response.ModifyPasswordResponse;
import com.yuyutechnology.exchange.server.controller.response.ModifyPayPwdByGoldpayResponse;
import com.yuyutechnology.exchange.server.controller.response.ModifyPayPwdByOldResponse;
import com.yuyutechnology.exchange.server.controller.response.ModifyUserNameResponse;
import com.yuyutechnology.exchange.server.controller.response.SetUserPayPwdResponse;
import com.yuyutechnology.exchange.server.controller.response.SwitchLanguageResponse;
import com.yuyutechnology.exchange.server.session.SessionData;
import com.yuyutechnology.exchange.server.session.SessionDataHolder;
import com.yuyutechnology.exchange.server.session.SessionManager;

/**
 * @author suzan.wu
 */
@Controller
public class LoggedInUserController {
	public static Logger logger = LoggerFactory.getLogger(LoggedInUserController.class);

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
	@ResponseBody
	@ApiOperation(value = "绑定goldpay", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/bindGoldpay", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BindGoldpayResponse bindGoldpay(@PathVariable String token,
			@RequestBody BindGoldpayRequest bindGoldpayRequest) {
		logger.info("========bindGoldpay : {}============", token);
		BindGoldpayResponse rep = new BindGoldpayResponse();
		if (bindGoldpayRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			String retCode = userManager.bindGoldpay(sessionData.getUserId(), bindGoldpayRequest.getGoldpayToken());
			switch (retCode) {
			case ServerConsts.RET_CODE_SUCCESS:
				// 获取用户信息
				rep.setUser(userManager.getUserInfo(sessionData.getUserId()));
				logger.info("********Operation succeeded********");
				rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
				break;
			case ServerConsts.GOLDPAY_PHONE_IS_NOT_EXIST:
				logger.info(MessageConsts.GOLDPAY_PHONE_IS_NOT_EXIST);
				rep.setRetCode(ServerConsts.GOLDPAY_PHONE_IS_NOT_EXIST);
				rep.setMessage(MessageConsts.GOLDPAY_PHONE_IS_NOT_EXIST);
				break;
			default:
				logger.info(MessageConsts.RET_CODE_FAILUE);
				rep.setRetCode(ServerConsts.RET_CODE_FAILUE);
				rep.setMessage(MessageConsts.RET_CODE_FAILUE);
				break;
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
	@ResponseBody
	@ApiOperation(value = "换绑手机", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/changePhone", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ChangePhoneResponse changePhone(@PathVariable String token,
			@RequestBody ChangePhoneRequest changePhoneRequest) throws ParseException {
		logger.info("========changePhone : {}============", token);
		ChangePhoneResponse rep = new ChangePhoneResponse();
		if (changePhoneRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			long time = userManager.checkChangePhoneTime(sessionData.getUserId());
			if (time <= new Date().getTime()) {
				// 检验支付密码
				if (userManager.checkUserPayPwd(sessionData.getUserId(), changePhoneRequest.getUserPayPwd())) {
					// 校验手机验证码
					if (userManager.testPinCode(ServerConsts.PIN_FUNC_CHANGEPHONE, changePhoneRequest.getAreaCode(),
							changePhoneRequest.getUserPhone(), changePhoneRequest.getVerificationCode())) {
						userManager.changePhone(sessionData.getUserId(), changePhoneRequest.getAreaCode(),
								changePhoneRequest.getUserPhone());
						sessionManager.logout(sessionData.getSessionId());
						sessionManager.delLoginToken(sessionData.getUserId());
						logger.info("********Operation succeeded********");
						rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
						rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
						userManager.clearPinCode(ServerConsts.PIN_FUNC_CHANGEPHONE, changePhoneRequest.getAreaCode(),
								changePhoneRequest.getUserPhone());
					} else {
						logger.info(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
						rep.setRetCode(ServerConsts.PHONE_AND_CODE_NOT_MATCH);
						rep.setMessage(MessageConsts.PHONE_AND_CODE_NOT_MATCH);
					}
				} else {
					logger.info(MessageConsts.PASSWORD_NOT_MATCH);
					rep.setRetCode(ServerConsts.PASSWORD_NOT_MATCH);
					rep.setMessage(MessageConsts.PASSWORD_NOT_MATCH);
				}
			} else {
				logger.info(MessageConsts.TIME_NOT_ARRIVED);
				rep.setRetCode(ServerConsts.TIME_NOT_ARRIVED);
				rep.setMessage(MessageConsts.TIME_NOT_ARRIVED);
			}
			rep.setTime(time);
		}
		return rep;
	}

	// TODO checkChangePhone 校验换绑手机时间
	@ResponseBody
	@ApiOperation(value = "校验换绑手机", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/checkChangePhone", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public CheckChangePhoneResponse checkChangePhone(@PathVariable String token) throws ParseException {
		logger.info("========checkChangePhone : {}============", token);
		CheckChangePhoneResponse rep = new CheckChangePhoneResponse();
		SessionData sessionData = SessionDataHolder.getSessionData();
		long time = userManager.checkChangePhoneTime(sessionData.getUserId());
		if (time <= new Date().getTime()) {
			logger.info("********Operation succeeded********");
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		} else {
			logger.info(MessageConsts.TIME_NOT_ARRIVED);
			rep.setRetCode(ServerConsts.TIME_NOT_ARRIVED);
			rep.setMessage(MessageConsts.TIME_NOT_ARRIVED);
		}
		rep.setTime(time);
		return rep;
	}

	/**
	 * checkPassword 校验登录密码
	 * 
	 * @param token
	 * @param checkPasswordRequest
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "校验登录密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/checkPassword", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public CheckPasswordResponse checkPassword(@PathVariable String token,
			@RequestBody CheckPasswordRequest checkPasswordRequest) {
		logger.info("========checkPassword : {}============", token);
		CheckPasswordResponse rep = new CheckPasswordResponse();
		SessionData sessionData = SessionDataHolder.getSessionData();
		if (userManager.checkUserPassword(sessionData.getUserId(), checkPasswordRequest.getUserPassword())) {
			logger.info("********Operation succeeded********");
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		} else {
			logger.info(MessageConsts.PASSWORD_NOT_MATCH);
			rep.setRetCode(ServerConsts.PASSWORD_NOT_MATCH);
			rep.setMessage(MessageConsts.PASSWORD_NOT_MATCH);
		}
		return rep;
	}

	/**
	 * checkPayPwd 校验支付密码
	 * 
	 * @param token
	 * @param checkPayPwdRequest
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "校验支付密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/checkPayPwd", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public CheckPayPwdResponse checkPayPwd(@PathVariable String token,
			@RequestBody CheckPayPwdRequest checkPayPwdRequest) {
		logger.info("========checkPassword : {}============", token);
		CheckPayPwdResponse rep = new CheckPayPwdResponse();

		SessionData sessionData = SessionDataHolder.getSessionData();
		if (userManager.checkUserPayPwd(sessionData.getUserId(), checkPayPwdRequest.getUserPayPwd())) {
			logger.info("********Operation succeeded********");
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		} else {
			logger.info(MessageConsts.PAY_PWD_NOT_MATCH);
			rep.setRetCode(ServerConsts.PAY_PWD_NOT_MATCH);
			rep.setMessage(MessageConsts.PAY_PWD_NOT_MATCH);
		}
		return rep;
	}

	/**
	 * checkGoldpayPwd 校验Goldpay密码
	 * 
	 * @param token
	 * @param checkPayPwdRequest
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "校验Goldpay密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/checkGoldpayPwd", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public CheckGoldpayPwdResponse checkGoldpayPwd(@PathVariable String token,
			@RequestBody CheckGoldpayPwdRequest checkPayGoldpayRequest) {
		logger.info("========checkPassword : {}============", token);
		CheckGoldpayPwdResponse rep = new CheckGoldpayPwdResponse();
		if (checkPayGoldpayRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			if (userManager.checkUserPayPwd(sessionData.getUserId(), checkPayGoldpayRequest.getGoldpayPwd())) {
				logger.info("********Operation succeeded********");
				rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			} else {
				logger.info(MessageConsts.GOLDPAY_PASSWORD_NOT_MATCH);
				rep.setRetCode(ServerConsts.GOLDPAY_PASSWORD_NOT_MATCH);
				rep.setMessage(MessageConsts.GOLDPAY_PASSWORD_NOT_MATCH);
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
	@ResponseBody
	@ApiOperation(value = "修改用户密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/modifyPassword", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ModifyPasswordResponse modifyPassword(@PathVariable String token,
			@RequestBody ModifyPasswordRequest modifyPasswordRequest) {
		logger.info("========modifyPassword : {}============", token);
		ModifyPasswordResponse rep = new ModifyPasswordResponse();
		if (modifyPasswordRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			if (userManager.checkUserPassword(sessionData.getUserId(), modifyPasswordRequest.getOldPassword())) {
				if (modifyPasswordRequest.getOldPassword().equals(modifyPasswordRequest.getNewPassword())) {
					logger.info(MessageConsts.NEW_PWD_EQUALS_OLD);
					rep.setRetCode(ServerConsts.NEW_PWD_EQUALS_OLD);
					rep.setMessage(MessageConsts.NEW_PWD_EQUALS_OLD);
				} else {
					userManager.updatePassword(sessionData.getUserId(), modifyPasswordRequest.getNewPassword());
					sessionManager.logout(sessionData.getSessionId());
					sessionManager.delLoginToken(sessionData.getUserId());
					logger.info("********Operation succeeded********");
					rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
					rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
				}
			} else {
				logger.info(MessageConsts.PASSWORD_NOT_MATCH);
				rep.setRetCode(ServerConsts.PASSWORD_NOT_MATCH);
				rep.setMessage(MessageConsts.PASSWORD_NOT_MATCH);
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
	@ResponseBody
	@ApiOperation(value = "修改用户名", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/modifyUserName", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ModifyUserNameResponse modifyUserName(@PathVariable String token,
			@RequestBody ModifyUserNameRequest modifyUserNameRequest) {
		logger.info("========modifyUserName : {}============", token);
		ModifyUserNameResponse rep = new ModifyUserNameResponse();

		if (modifyUserNameRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			userManager.updateUserName(sessionData.getUserId(), modifyUserNameRequest.getNewUserName());
			logger.info("********Operation succeeded********");
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
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
	@ResponseBody
	@ApiOperation(value = "设置支付密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/setUserPayPwd", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public SetUserPayPwdResponse setUserPayPwd(@PathVariable String token,
			@RequestBody SetUserPayPwdRequest setUserPayPwdRequest) {
		logger.info("========setUserPayPwd : {}============", token);
		SetUserPayPwdResponse rep = new SetUserPayPwdResponse();
		if (setUserPayPwdRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			// PayPwd 6位数字
			if (setUserPayPwdRequest.getUserPayPwd().length() == 6
					&& StringUtils.isNumeric(setUserPayPwdRequest.getUserPayPwd())) {
				userManager.updateUserPayPwd(sessionData.getUserId(), setUserPayPwdRequest.getUserPayPwd());
				logger.info("********Operation succeeded********");
				rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			} else {
				logger.info(MessageConsts.PAY_PASSWORD_IS_ILLEGAL);
				rep.setRetCode(ServerConsts.PAY_PASSWORD_IS_ILLEGAL);
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
	@ResponseBody
	@ApiOperation(value = "通过原支付密码更换支付密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/modifyPayPwdByOld", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ModifyPayPwdByOldResponse modifyPayPwdByOld(@PathVariable String token,
			@RequestBody ModifyPayPwdByOldRequest modifyPayPwdByOldRequest) {
		logger.info("========modifyPayPwdByOld : {}============", token);
		ModifyPayPwdByOldResponse rep = new ModifyPayPwdByOldResponse();
		if (modifyPayPwdByOldRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			if (userManager.checkUserPayPwd(sessionData.getUserId(), modifyPayPwdByOldRequest.getOldUserPayPwd())) {
				// PayPwd 6位数字
				if (modifyPayPwdByOldRequest.getNewUserPayPwd().length() == 6
						&& StringUtils.isNumeric(modifyPayPwdByOldRequest.getNewUserPayPwd())) {
					if (modifyPayPwdByOldRequest.getNewUserPayPwd()
							.equals(modifyPayPwdByOldRequest.getOldUserPayPwd())) {
						logger.info(MessageConsts.NEW_PWD_EQUALS_OLD);
						rep.setRetCode(ServerConsts.NEW_PWD_EQUALS_OLD);
						rep.setMessage(MessageConsts.NEW_PWD_EQUALS_OLD);
					} else {
						userManager.updateUserPayPwd(sessionData.getUserId(),
								modifyPayPwdByOldRequest.getNewUserPayPwd());
						logger.info("********Operation succeeded********");
						rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
						rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
					}

				} else {
					logger.info(MessageConsts.PAY_PASSWORD_IS_ILLEGAL);
					rep.setRetCode(ServerConsts.PAY_PASSWORD_IS_ILLEGAL);
					rep.setMessage(MessageConsts.PAY_PASSWORD_IS_ILLEGAL);
				}

			} else {
				logger.info(MessageConsts.PAY_PWD_NOT_MATCH);
				rep.setRetCode(ServerConsts.PAY_PWD_NOT_MATCH);
				rep.setMessage(MessageConsts.PAY_PWD_NOT_MATCH);
			}

		}
		return rep;
	}

	/**
	 * modifyPayPwdByGoldpay 通过Goldpay密码更换支付密码
	 * 
	 * @param token
	 * @param modifyPayPwdByGoldRequest
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "通过Goldpay密码更换支付密码", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/modifyPayPwdByPIN", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ModifyPayPwdByGoldpayResponse modifyPayPwdByGoldpay(@PathVariable String token,
			@RequestBody ModifyPayPwdByGoldpayRequest modifyPayPwdByGoldRequest) {
		logger.info("========modifyPayPwdByOld : {}============", token);
		ModifyPayPwdByGoldpayResponse rep = new ModifyPayPwdByGoldpayResponse();
		if (modifyPayPwdByGoldRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			if (userManager.checkGoldpayPwd(sessionData.getUserId(), modifyPayPwdByGoldRequest.getGoldpayPwd())) {
				// PayPwd 6位数字
				if (modifyPayPwdByGoldRequest.getNewUserPayPwd().length() == 6
						&& StringUtils.isNumeric(modifyPayPwdByGoldRequest.getNewUserPayPwd())) {
					userManager.updateUserPayPwd(sessionData.getUserId(), modifyPayPwdByGoldRequest.getNewUserPayPwd());
					logger.info("********Operation succeeded********");
					rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
					rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
				} else {
					logger.info(MessageConsts.PAY_PASSWORD_IS_ILLEGAL);
					rep.setRetCode(ServerConsts.PAY_PASSWORD_IS_ILLEGAL);
					rep.setMessage(MessageConsts.PAY_PASSWORD_IS_ILLEGAL);
				}
			} else {
				logger.info(MessageConsts.GOLDPAY_PASSWORD_NOT_MATCH);
				rep.setRetCode(ServerConsts.GOLDPAY_PASSWORD_NOT_MATCH);
				rep.setMessage(MessageConsts.GOLDPAY_PASSWORD_NOT_MATCH);
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
	@ResponseBody
	@ApiOperation(value = "切换语言", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/switchLanguage", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public SwitchLanguageResponse switchLanguage(@PathVariable String token,
			@RequestBody SwitchLanguageRequest switchLanguageRequest) {
		logger.info("========switchLanguage : {}============", token);
		SwitchLanguageResponse rep = new SwitchLanguageResponse();

		if (switchLanguageRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			SessionData sessionData = SessionDataHolder.getSessionData();
			userManager.switchLanguage(sessionData.getUserId(), switchLanguageRequest.getLanguage());
			logger.info("********Operation succeeded********");
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
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
	@ResponseBody
	@ApiOperation(value = "退出账号", httpMethod = "POST", notes = "")
	@RequestMapping(value = "/token/{token}/user/logout", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public LogoutResponse logout(@PathVariable String token, @RequestBody LogoutRequest logoutRequest) {
		logger.info("========logout : {}============", token);
		LogoutResponse rep = new LogoutResponse();
		SessionData sessionData = SessionDataHolder.getSessionData();
		int userId = 0;
		if (sessionData == null) {
			userId = logoutRequest.getUserId();
		} else {
			userId = sessionData.getUserId();
			// 清session
			sessionManager.logout(sessionData.getSessionId());
		}
		// 清logintoken
		sessionManager.delLoginToken(userId);
		// 清pushId,解绑Tag
		userManager.logout(userId);
		logger.info("********Operation succeeded********");
		rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
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
	@RequestMapping(value = "/token/{token}/user/contactUs", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ContactUsResponse contactUs(@PathVariable String token, @RequestBody ContactUsRequest contactUsRequest) {
		logger.info("========contactUs : {}============", token);
		ContactUsResponse rep = new ContactUsResponse();
		if (contactUsRequest.isEmpty()) {
			logger.info(MessageConsts.PARAMETER_IS_EMPTY);
			rep.setRetCode(ServerConsts.PARAMETER_IS_EMPTY);
			rep.setMessage(MessageConsts.PARAMETER_IS_EMPTY);
		} else {
			mailManager.mail4contact(contactUsRequest.getName(), contactUsRequest.getEmail(),
					contactUsRequest.getCategory(), contactUsRequest.getEnquiry());
			logger.info("********Operation succeeded********");
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		}
		return rep;
	}

	@ApiOperation(value = "获取消息红点标志位")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/user/getMsgFlag")
	public @ResponseBody GetMsgFlagResponse getMsgFlag(@PathVariable String token) {
		SessionData sessionData = SessionDataHolder.getSessionData();
		GetMsgFlagResponse rep = new GetMsgFlagResponse();
		MsgFlagInfo msgFlagInfo = commonManager.getMsgFlag(sessionData.getUserId());
		rep.setNewRequestTrans(msgFlagInfo.isNewRequestTrans());
		rep.setNewTrans(msgFlagInfo.isNewTrans());
		rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		return rep;
	}

}
