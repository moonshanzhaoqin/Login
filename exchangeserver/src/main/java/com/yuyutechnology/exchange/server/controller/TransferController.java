package com.yuyutechnology.exchange.server.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dto.TransDetailsDTO;
import com.yuyutechnology.exchange.dto.TransferDTO;
import com.yuyutechnology.exchange.dto.UserInfo;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.push.PushManager;
import com.yuyutechnology.exchange.server.controller.dto.NotificationDTO;
import com.yuyutechnology.exchange.server.controller.request.GetNotificationRecordsRequest;
import com.yuyutechnology.exchange.server.controller.request.GetTransDetailsRequest;
import com.yuyutechnology.exchange.server.controller.request.GetTransactionRecordRequest;
import com.yuyutechnology.exchange.server.controller.request.MakeRequestRequest;
import com.yuyutechnology.exchange.server.controller.request.RegenerateQRCodeRequest;
import com.yuyutechnology.exchange.server.controller.request.ResendTransferPinRequest;
import com.yuyutechnology.exchange.server.controller.request.Respond2RequestRequest;
import com.yuyutechnology.exchange.server.controller.request.TransactionPreviewRequest;
import com.yuyutechnology.exchange.server.controller.request.TransferConfirmRequest;
import com.yuyutechnology.exchange.server.controller.request.TransferInitiateRequest;
import com.yuyutechnology.exchange.server.controller.response.GetNotificationRecordsResponse;
import com.yuyutechnology.exchange.server.controller.response.GetTransDetailsResponse;
import com.yuyutechnology.exchange.server.controller.response.GetTransactionRecordResponse;
import com.yuyutechnology.exchange.server.controller.response.MakeRequestResponse;
import com.yuyutechnology.exchange.server.controller.response.RegenerateQRCodeResponse;
import com.yuyutechnology.exchange.server.controller.response.ResendTransferPinResponse;
import com.yuyutechnology.exchange.server.controller.response.Respond2RequestResponse;
import com.yuyutechnology.exchange.server.controller.response.TransactionPreviewResponse;
import com.yuyutechnology.exchange.server.controller.response.TransferConfirmResponse;
import com.yuyutechnology.exchange.server.controller.response.TransferInitiateResponse;
import com.yuyutechnology.exchange.server.security.annotation.RequestDecryptBody;
import com.yuyutechnology.exchange.server.security.annotation.ResponseEncryptBody;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionDataHolder;

@Controller
public class TransferController{
	@Autowired
	UserManager userManager;
	@Autowired
	TransferManager transferManager;
	@Autowired
	PushManager pushManager;
	@Autowired
	ConfigManager configManager;
	@Autowired
	CommonManager commonManager;

	public static Logger logger = LogManager.getLogger(TransferController.class);
	
	
	@ApiOperation(value = "交易预览")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/transactionPreview")
	public @ResponseEncryptBody TransactionPreviewResponse transactionPreview(
			@PathVariable String token,@RequestDecryptBody TransactionPreviewRequest reqMsg){
		
		SessionData sessionData = SessionDataHolder.getSessionData();
		TransactionPreviewResponse rep = new TransactionPreviewResponse();
		
		if (StringUtils.isEmpty(reqMsg.getAreaCode()) || StringUtils.isEmpty(reqMsg.getUserPhone())) {
			logger.info("Phone number is empty");
			rep.setRetCode(RetCodeConsts.TRANSFER_PHONE_NUMBER_IS_EMPTY);
			rep.setMessage("Phone number is empty");
			return rep;
		}
		// 装张金额上限
		if (!reqMsg.getCurrency().equals(ServerConsts.CURRENCY_OF_GOLDPAY) && reqMsg.getTransAmount() < 0.0001) {
			logger.info("The input amount is less than the minimum amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
			rep.setMessage("The input amount is less than the minimum amount");
			return rep;
		} else if ((reqMsg.getCurrency()).equals(ServerConsts.CURRENCY_OF_GOLDPAY)
				&& (reqMsg.getTransAmount() % 1 > 0 || reqMsg.getTransAmount() == 0)) {
			logger.info("The GDQ must be an integer value");
			rep.setRetCode(RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
			rep.setMessage("The GDQ must be an integer value");
			return rep;
		} else if (reqMsg.getTransAmount() > configManager.getConfigLongValue(ConfigKeyEnum.ENTERMAXIMUMAMOUNT,
				1000000000L)) {
			logger.info("Fill out the allowable amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_FILL_OUT_THE_ALLOWABLE_AMOUNT);
			rep.setMessage("Fill out the allowable amount");
			return rep;
		}
		
		HashMap<String, String> map = transferManager.transactionPreview(sessionData.getUserId(),
				reqMsg.getAreaCode(), reqMsg.getUserPhone(), reqMsg.getCurrency(), BigDecimal.valueOf(reqMsg.getTransAmount()));
		
		if(RetCodeConsts.RET_CODE_SUCCESS.equals(map.get("retCode"))){
			rep.setUserAccount(map.get("userAccount"));
			rep.setUserName(map.get("userName"));
			rep.setCurrency(reqMsg.getCurrency());
			rep.setTransAmount(reqMsg.getTransAmount()+"");
			rep.setAvatarUrl(map.get("avatarUrl"));
		}
		
		rep.setRetCode(map.get("retCode"));
		rep.setMessage(map.get("msg"));
		
		return rep;
	}

	@ApiOperation(value = "交易初始化")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/transferInitiate")
	public @ResponseEncryptBody TransferInitiateResponse transferInitiate(@PathVariable String token,
			@RequestDecryptBody TransferInitiateRequest reqMsg) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		TransferInitiateResponse rep = new TransferInitiateResponse();

		if (StringUtils.isEmpty(reqMsg.getAreaCode()) || StringUtils.isEmpty(reqMsg.getUserPhone())) {
			logger.info("Phone number is empty");
			rep.setRetCode(RetCodeConsts.TRANSFER_PHONE_NUMBER_IS_EMPTY);
			rep.setMessage("Phone number is empty");
			return rep;
		}
		// 装张金额上限
		if (!reqMsg.getCurrency().equals(ServerConsts.CURRENCY_OF_GOLDPAY) && reqMsg.getAmount() < 0.0001) {
			logger.info("The input amount is less than the minimum amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
			rep.setMessage("The input amount is less than the minimum amount");
			return rep;
		} else if ((reqMsg.getCurrency()).equals(ServerConsts.CURRENCY_OF_GOLDPAY)
				&& (reqMsg.getAmount() % 1 > 0 || reqMsg.getAmount() == 0)) {
			logger.info("The GDQ must be an integer value");
			rep.setRetCode(RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
			rep.setMessage("The GDQ must be an integer value");
			return rep;
		} else if (reqMsg.getAmount() > configManager.getConfigLongValue(ConfigKeyEnum.ENTERMAXIMUMAMOUNT,
				1000000000L)) {
			logger.info("Fill out the allowable amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_FILL_OUT_THE_ALLOWABLE_AMOUNT);
			rep.setMessage("Fill out the allowable amount");
			return rep;
		}

		HashMap<String, String> map = transferManager.transferInitiate(sessionData.getUserId(), reqMsg.getAreaCode(),
				reqMsg.getUserPhone(), reqMsg.getCurrency(), new BigDecimal(Double.toString(reqMsg.getAmount())),
				reqMsg.getTransferComment(), 0);

		if (map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)) {
			rep.setTransferId(map.get("transferId"));
			rep.setCodeVerification(map.get("codeVerification"));
		} else if (map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_DAILY_PAY)) {
			rep.setOpts(new String[] { map.get("msg") + " " + map.get("unit"), map.get("thawTime") });
		} else if (map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_EACH_TIME)) {
			rep.setOpts(new String[] { map.get("msg") + " " + map.get("unit") });
		} else if (map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_NUM_OF_PAY_PER_DAY)) {
			rep.setOpts(new String[] { map.get("msg"), map.get("thawTime") });
		}

		rep.setRetCode(map.get("retCode"));
		rep.setMessage(map.get("msg"));

		return rep;

	}
	
	@ApiOperation(value = "交易确认")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/transferConfirm")
	public @ResponseEncryptBody TransferConfirmResponse transferConfirm(@PathVariable String token,
			@RequestDecryptBody TransferConfirmRequest reqMsg){
		
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		TransferConfirmResponse rep = new TransferConfirmResponse();
		
		HashMap<String, String> map = transferManager.transferConfirm(sessionData.getUserId(),
				reqMsg.getTransferId(), reqMsg.getUserPayPwd(), reqMsg.getPinCode());
		
		if (map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)) {
			rep.setMakeFriends(map.get("makeFriends"));
		} else if (map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_DAILY_PAY)) {
			rep.setOpts(new String[] { map.get("msg") + " " + map.get("unit"), map.get("thawTime") });
		} else if (map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_EACH_TIME)) {
			rep.setOpts(new String[] { map.get("msg") + " " + map.get("unit") });
		} else if (map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_NUM_OF_PAY_PER_DAY)) {
			rep.setOpts(new String[] { map.get("msg"), map.get("thawTime") });
		}

		rep.setRetCode(map.get("retCode"));
		rep.setMessage(map.get("msg"));

		return rep;
		
	}
	
	@ApiOperation(value = "重新发送pin")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/resendPhonePin")
	public @ResponseEncryptBody ResendTransferPinResponse resendTransferPin(@PathVariable String token,
			@RequestDecryptBody ResendTransferPinRequest reqMsg) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		ResendTransferPinResponse rep = new ResendTransferPinResponse();
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		try {
			UserInfo user = userManager.getUserInfo(sessionData.getUserId());
			userManager.getPinCode(reqMsg.getTransferId(), user.getAreaCode(), user.getPhone());
		} catch (Exception e) {
			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
			rep.setMessage("Re-sending the phone pin failed");
		}
		return rep;
	}

//	@ApiOperation(value = "交易确认")
//	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/transferConfirm")
//	public @ResponseEncryptBody TransferConfirmResponse transferConfirm(@PathVariable String token,
//			@RequestDecryptBody TransferConfirmRequest reqMsg) {
//		// 从Session中获取Id
//		SessionData sessionData = SessionDataHolder.getSessionData();
//
//		UserInfo user = userManager.getUserInfo(sessionData.getUserId());
//		TransferConfirmResponse rep = new TransferConfirmResponse();
//		// 判断PinCode是否正确
//		Boolean resultBool = userManager.testPinCode(reqMsg.getTransferId(), user.getAreaCode(), user.getPhone(),
//				reqMsg.getPinCode());
//		if (resultBool == null) {
//			logger.info(MessageConsts.NOT_GET_CODE);
//			rep.setRetCode(RetCodeConsts.NOT_GET_CODE);
//			rep.setMessage(MessageConsts.NOT_GET_CODE);
//		} else if (resultBool.booleanValue()) {
//			String result = transferManager.transferConfirm(sessionData.getUserId(), reqMsg.getTransferId());
//			if (result.equals(RetCodeConsts.RET_CODE_SUCCESS)) {
//				rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
//				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
//			} else {
//				rep.setRetCode(result);
//				rep.setMessage("Current balance is insufficient");
//			}
//			userManager.clearPinCode(reqMsg.getTransferId(), user.getAreaCode(), user.getPhone());
//		} else {
//			rep.setRetCode(RetCodeConsts.PIN_CODE_INCORRECT);
//			rep.setMessage("The pin code is incorrect");
//		}
//		return rep;
//	}

	@ApiOperation(value = "发起转账请求")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/makeRequest")
	public @ResponseEncryptBody MakeRequestResponse makeRequest(@PathVariable String token,
			@RequestDecryptBody MakeRequestRequest reqMsg) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		MakeRequestResponse rep = new MakeRequestResponse();

		HashMap<String, Object> result = transferManager.makeRequest(sessionData.getUserId(), reqMsg.getAreaCode(),
				reqMsg.getPhone(), reqMsg.getCurrency(), new BigDecimal(reqMsg.getAmount()));

		rep.setRetCode((String) result.get("retCode"));
		rep.setMessage((String) result.get("msg"));

		if (result.get("transferLimitPerPay") != null) {
			rep.setOpts(new String[] { (String) result.get("transferLimitPerPay") + " " + result.get("unit") });
		}

		return rep;
	}

	@ApiOperation(value = "重新生成二维码")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/regenerateQRCode")
	public @ResponseEncryptBody RegenerateQRCodeResponse regenerateQRCode(@PathVariable String token,
			@RequestDecryptBody RegenerateQRCodeRequest reqMsg) {
		RegenerateQRCodeResponse rep = new RegenerateQRCodeResponse();
		if (reqMsg == null || StringUtils.isEmpty(reqMsg.getCurrency())) {
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			return rep;
		}
		HashMap<String, String> result = transferManager.regenerateQRCode(reqMsg.getCurrency(), reqMsg.getAmount());
		rep.setRetCode(result.get("retCode"));
		rep.setMessage(result.get("msg"));
		if (result.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_EACH_TIME)) {
			rep.setOpts(new String[] { result.get("msg") + " " + result.get("unit") });
		}
		return rep;
	}

	@ApiOperation(value = "请求转账回应")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/requestATransfer")
	public @ResponseEncryptBody Respond2RequestResponse respond2Request(@PathVariable String token,
			@RequestDecryptBody Respond2RequestRequest reqMsg) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		Respond2RequestResponse rep = new Respond2RequestResponse();

		// 装张金额上限
		if (reqMsg.getCurrency() != ServerConsts.CURRENCY_OF_GOLDPAY && reqMsg.getAmount() < 0.01) {
			logger.info("The input amount is less than the minimum amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
			rep.setMessage("The input amount is less than the minimum amount");
			return rep;
		} else if (reqMsg.getCurrency() == ServerConsts.CURRENCY_OF_GOLDPAY && reqMsg.getAmount() < 1) {
			logger.info("The input amount is less than the minimum amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
			rep.setMessage("The input amount is less than the minimum amount");
			return rep;
		}

		HashMap<String, String> map = transferManager.respond2Request(sessionData.getUserId(), reqMsg.getAreaCode(),
				reqMsg.getUserPhone(), reqMsg.getCurrency(), new BigDecimal(Double.toString(reqMsg.getAmount())),
				reqMsg.getTransferComment(), reqMsg.getNoticeId());

		if (map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)) {
			rep.setTransferId(map.get("transferId"));
		} else if (map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_DAILY_PAY)) {
			rep.setOpts(new String[] { map.get("msg") + " " + map.get("unit"), map.get("thawTime") });
		} else if (map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_EACH_TIME)) {
			rep.setOpts(new String[] { map.get("msg") + " " + map.get("unit") });
		} else if (map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_NUM_OF_PAY_PER_DAY)) {
			rep.setOpts(new String[] { map.get("msg"), map.get("thawTime") });
		}else if(map.get("retCode").equals(RetCodeConsts.PAY_FREEZE) || map.get("retCode").equals(RetCodeConsts.PAY_PWD_NOT_MATCH)){
			rep.setOpts(new String[] { map.get("msg") });
		}

		rep.setRetCode(map.get("retCode"));
		rep.setMessage(map.get("msg"));

		return rep;

	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "获取交易列表")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/getTransactionRecordNew")
	public @ResponseEncryptBody GetTransactionRecordResponse getTransactionRecordNew(@PathVariable String token,
			@RequestDecryptBody GetTransactionRecordRequest reqMsq) {

		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		GetTransactionRecordResponse rep = new GetTransactionRecordResponse();
		HashMap<String, Object> map = transferManager.getTransRecordbyPage(reqMsq.getPeriod(), reqMsq.getType(),
				sessionData.getUserId(), reqMsq.getCurrentPage(), reqMsq.getPageSize());

		if (((ArrayList<?>) map.get("dtos")).isEmpty()) {
			rep.setRetCode(RetCodeConsts.TRANSFER_HISTORY_NOT_ACQUIRED);
			rep.setMessage("Transaction history not acquired");
		} else {
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			rep.setCurrentPage((int) map.get("currentPage"));
			rep.setPageSize((int) map.get("pageSize"));
			rep.setPageTotal((int) map.get("pageTotal"));
			rep.setTotal(Integer.parseInt(map.get("total") + ""));
			rep.setList((List<TransferDTO>) map.get("dtos"));
		}

		return rep;

	}

	@ApiOperation(value = "交易通知列表")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/getNotificationRecords")
	public @ResponseEncryptBody GetNotificationRecordsResponse getNotificationRecords(@PathVariable String token,
			@RequestDecryptBody GetNotificationRecordsRequest reqMsg) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		GetNotificationRecordsResponse rep = new GetNotificationRecordsResponse();

		HashMap<String, Object> map = transferManager.getNotificationRecordsByPage(sessionData.getUserId(),
				reqMsg.getCurrentPage(), reqMsg.getPageSize());

		ArrayList<NotificationDTO> dtos = new ArrayList<>();
		rep.setList(dtos);
		if (((ArrayList<?>) map.get("list")).isEmpty()) {
			rep.setRetCode(RetCodeConsts.TRANSFER_HISTORY_NOT_ACQUIRED);
			rep.setMessage("Notification Records not acquired");
		} else {

			List<?> list = (ArrayList<?>) map.get("list");
			for (Object object : list) {
				Object[] obj = (Object[]) object;

				NotificationDTO dto = new NotificationDTO();

				dto.setNoticeId((int) obj[0]);
				dto.setAreaCode((String) obj[1]);
				dto.setSponsorPhone((String) obj[2]);
				dto.setCurrency((String) obj[3]);
				dto.setAmount((new BigDecimal(obj[4] + "")).doubleValue());
				dto.setCreateAt((Date) obj[5]);
				dto.setTradingStatus((int) obj[6]);
				Currency currency = commonManager.getCurreny(dto.getCurrency());
				if (currency != null) {
					dto.setCurrencyUnit(currency.getCurrencyUnit());
				} else {
					dto.setCurrencyUnit("");
				}
				dtos.add(dto);
			}
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			rep.setCurrentPage((int) map.get("currentPage"));
			rep.setPageSize((int) map.get("pageSize"));
			rep.setPageTotal((int) map.get("pageTotal"));
			rep.setTotal(Integer.parseInt(map.get("total") + ""));
		}
		return rep;
	}

	@ApiOperation(value = "获取交易详情")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/getTransDetails")
	public @ResponseEncryptBody GetTransDetailsResponse getTransDetails(@PathVariable String token,
			@RequestDecryptBody GetTransDetailsRequest reqMsg) {
		SessionData sessionData = SessionDataHolder.getSessionData();
		TransDetailsDTO dto = transferManager.getTransDetails(reqMsg.getTransferId(), sessionData.getUserId());
		GetTransDetailsResponse rep = new GetTransDetailsResponse(dto);
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		return rep;
	}
	
//	@ApiOperation(value = "交易初始化")
//	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/transferInitiate")
//	public @ResponseEncryptBody TransferInitiateResponse transferInitiate(@PathVariable String token,
//			@RequestDecryptBody TransferInitiateRequest reqMsg) {
//		// 从Session中获取Id
//		SessionData sessionData = SessionDataHolder.getSessionData();
//		TransferInitiateResponse rep = new TransferInitiateResponse();
//
//		if (StringUtils.isEmpty(reqMsg.getAreaCode()) || StringUtils.isEmpty(reqMsg.getUserPhone())) {
//			logger.info("Phone number is empty");
//			rep.setRetCode(RetCodeConsts.TRANSFER_PHONE_NUMBER_IS_EMPTY);
//			rep.setMessage("Phone number is empty");
//			return rep;
//		}
//		// 装张金额上限
//		if (!reqMsg.getCurrency().equals(ServerConsts.CURRENCY_OF_GOLDPAY) && reqMsg.getAmount() < 0.0001) {
//			logger.info("The input amount is less than the minimum amount");
//			rep.setRetCode(RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
//			rep.setMessage("The input amount is less than the minimum amount");
//			return rep;
//		} else if ((reqMsg.getCurrency()).equals(ServerConsts.CURRENCY_OF_GOLDPAY)
//				&& (reqMsg.getAmount() % 1 > 0 || reqMsg.getAmount() == 0)) {
//			logger.info("The GDQ must be an integer value");
//			rep.setRetCode(RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
//			rep.setMessage("The GDQ must be an integer value");
//			return rep;
//		} else if (reqMsg.getAmount() > configManager.getConfigLongValue(ConfigKeyEnum.ENTERMAXIMUMAMOUNT,
//				1000000000L)) {
//			logger.info("Fill out the allowable amount");
//			rep.setRetCode(RetCodeConsts.TRANSFER_FILL_OUT_THE_ALLOWABLE_AMOUNT);
//			rep.setMessage("Fill out the allowable amount");
//			return rep;
//		}
//
//		HashMap<String, String> map = transferManager.transferInitiate(sessionData.getUserId(), reqMsg.getAreaCode(),
//				reqMsg.getUserPhone(), reqMsg.getCurrency(), new BigDecimal(Double.toString(reqMsg.getAmount())),
//				reqMsg.getTransferComment(), 0);
//
//		if (map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)) {
//			rep.setTransferId(map.get("transferId"));
//		} else if (map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_DAILY_PAY)) {
//			rep.setOpts(new String[] { map.get("msg") + " " + map.get("unit"), map.get("thawTime") });
//		} else if (map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_EACH_TIME)) {
//			rep.setOpts(new String[] { map.get("msg") + " " + map.get("unit") });
//		} else if (map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_NUM_OF_PAY_PER_DAY)) {
//			rep.setOpts(new String[] { map.get("msg"), map.get("thawTime") });
//		}
//
//		rep.setRetCode(map.get("retCode"));
//		rep.setMessage(map.get("msg"));
//
//		return rep;
//
//	}
//
//	@ApiOperation(value = "验证支付密码")
//	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/transPwdConfirm")
//	public @ResponseEncryptBody TransPwdConfirmResponse transPwdConfirm(@PathVariable String token,
//			@RequestDecryptBody TransPwdConfirmRequest reqMsg) {
//		// 从Session中获取Id
//		SessionData sessionData = SessionDataHolder.getSessionData();
//		TransPwdConfirmResponse rep = new TransPwdConfirmResponse();
//		
//		// 验证支付密码
//		CheckPwdResult checkPwdResult = userManager.checkPayPassword(
//				sessionData.getUserId(), reqMsg.getUserPayPwd());
//		
//		switch (checkPwdResult.getStatus()) {
//			case FREEZE:
//				rep.setRetCode(RetCodeConsts.PAY_FREEZE);
//				rep.setMessage(String.valueOf(checkPwdResult.getInfo()));
//				return rep;
//			case INCORRECT:
//				logger.warn("payPwd is wrong !");
//				rep.setRetCode(RetCodeConsts.PAY_PWD_NOT_MATCH);
//				rep.setMessage(String.valueOf(checkPwdResult.getInfo()));
//				return rep;
//			default:
//				break;
//		}
//		
//		HashMap<String, String> result = transferManager.whenPayPwdConfirmed(
//				sessionData.getUserId(), reqMsg.getTransferId(),reqMsg.getUserPayPwd());
//
//		if (result.get("retCode").equals(RetCodeConsts.TRANSFER_REQUIRES_PHONE_VERIFICATION)) {
//			// 发PIN码
//			UserInfo user = userManager.getUserInfo(sessionData.getUserId());
//			userManager.getPinCode(reqMsg.getTransferId(), user.getAreaCode(), user.getPhone());
//			rep.setRetCode(result.get("retCode"));
//			rep.setMessage("Need to send pin code verification");
//		} else {
//			rep.setRetCode(result.get("retCode"));
//			rep.setMessage(result.get("msg"));
//			rep.setOpts(new String[] { result.get("msg") });
//		}
//
//		return rep;
//	}
//
//	@ApiOperation(value = "重新发送pin")
//	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/resendPhonePin")
//	public @ResponseEncryptBody ResendTransferPinResponse resendTransferPin(@PathVariable String token,
//			@RequestDecryptBody ResendTransferPinRequest reqMsg) {
//		// 从Session中获取Id
//		SessionData sessionData = SessionDataHolder.getSessionData();
//		ResendTransferPinResponse rep = new ResendTransferPinResponse();
//		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
//		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
//		try {
//			UserInfo user = userManager.getUserInfo(sessionData.getUserId());
//			userManager.getPinCode(reqMsg.getTransferId(), user.getAreaCode(), user.getPhone());
//		} catch (Exception e) {
//			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
//			rep.setMessage("Re-sending the phone pin failed");
//		}
//		return rep;
//	}
//
//	@ApiOperation(value = "交易确认")
//	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/transferConfirm")
//	public @ResponseEncryptBody TransferConfirmResponse transferConfirm(@PathVariable String token,
//			@RequestDecryptBody TransferConfirmRequest reqMsg) {
//		// 从Session中获取Id
//		SessionData sessionData = SessionDataHolder.getSessionData();
//
//		UserInfo user = userManager.getUserInfo(sessionData.getUserId());
//		TransferConfirmResponse rep = new TransferConfirmResponse();
//		// 判断PinCode是否正确
//		Boolean resultBool = userManager.testPinCode(reqMsg.getTransferId(), user.getAreaCode(), user.getPhone(),
//				reqMsg.getPinCode());
//		if (resultBool == null) {
//			logger.info(MessageConsts.NOT_GET_CODE);
//			rep.setRetCode(RetCodeConsts.NOT_GET_CODE);
//			rep.setMessage(MessageConsts.NOT_GET_CODE);
//		} else if (resultBool.booleanValue()) {
//			String result = transferManager.transferConfirm(sessionData.getUserId(), reqMsg.getTransferId());
//			if (result.equals(RetCodeConsts.RET_CODE_SUCCESS)) {
//				rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
//				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
//			} else {
//				rep.setRetCode(result);
//				rep.setMessage("Current balance is insufficient");
//			}
//			userManager.clearPinCode(reqMsg.getTransferId(), user.getAreaCode(), user.getPhone());
//		} else {
//			rep.setRetCode(RetCodeConsts.PIN_CODE_INCORRECT);
//			rep.setMessage("The pin code is incorrect");
//		}
//		return rep;
//	}


}
