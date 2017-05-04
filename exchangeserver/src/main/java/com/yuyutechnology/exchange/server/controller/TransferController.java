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
import com.yuyutechnology.exchange.dto.UserInfo;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.push.PushManager;
import com.yuyutechnology.exchange.server.controller.dto.NotificationDTO;
import com.yuyutechnology.exchange.server.controller.dto.TransferDTO;
import com.yuyutechnology.exchange.server.controller.request.GetNotificationRecordsRequest;
import com.yuyutechnology.exchange.server.controller.request.GetTransDetailsRequest;
import com.yuyutechnology.exchange.server.controller.request.GetTransactionRecordRequest;
import com.yuyutechnology.exchange.server.controller.request.MakeRequestRequest;
import com.yuyutechnology.exchange.server.controller.request.RegenerateQRCodeRequest;
import com.yuyutechnology.exchange.server.controller.request.ResendTransferPinRequest;
import com.yuyutechnology.exchange.server.controller.request.Respond2RequestRequest;
import com.yuyutechnology.exchange.server.controller.request.TransPwdConfirmRequest;
import com.yuyutechnology.exchange.server.controller.request.TransferConfirmRequest;
import com.yuyutechnology.exchange.server.controller.request.TransferInitiateRequest;
import com.yuyutechnology.exchange.server.controller.response.GetNotificationRecordsResponse;
import com.yuyutechnology.exchange.server.controller.response.GetTransDetailsResponse;
import com.yuyutechnology.exchange.server.controller.response.GetTransactionRecordResponse;
import com.yuyutechnology.exchange.server.controller.response.MakeRequestResponse;
import com.yuyutechnology.exchange.server.controller.response.RegenerateQRCodeResponse;
import com.yuyutechnology.exchange.server.controller.response.ResendTransferPinResponse;
import com.yuyutechnology.exchange.server.controller.response.Respond2RequestResponse;
import com.yuyutechnology.exchange.server.controller.response.TransPwdConfirmResponse;
import com.yuyutechnology.exchange.server.controller.response.TransferConfirmResponse;
import com.yuyutechnology.exchange.server.controller.response.TransferInitiateResponse;
import com.yuyutechnology.exchange.server.security.annotation.RequestDecryptBody;
import com.yuyutechnology.exchange.server.security.annotation.ResponseEncryptBody;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionDataHolder;
import com.yuyutechnology.exchange.util.MathUtils;

@Controller
public class TransferController {
	
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

	@ApiOperation(value = "交易初始化")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/transferInitiate")
	public @ResponseEncryptBody
	TransferInitiateResponse transferInitiate(@PathVariable String token,
			@RequestDecryptBody TransferInitiateRequest reqMsg){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		TransferInitiateResponse rep = new TransferInitiateResponse();
		
		if(StringUtils.isEmpty(reqMsg.getAreaCode()) || StringUtils.isEmpty(reqMsg.getUserPhone())){
			logger.warn("Phone number is empty");
			rep.setRetCode(RetCodeConsts.TRANSFER_PHONE_NUMBER_IS_EMPTY);
			rep.setMessage("Phone number is empty");
			return rep;
		}
		//装张金额上限
		if(!reqMsg.getCurrency().equals(ServerConsts.CURRENCY_OF_GOLDPAY) && reqMsg.getAmount() < 0.0001){
			logger.warn("The input amount is less than the minimum amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
			rep.setMessage("The input amount is less than the minimum amount");
			return rep;
		}else if((reqMsg.getCurrency()).equals(ServerConsts.CURRENCY_OF_GOLDPAY) &&( reqMsg.getAmount()%1 > 0 || reqMsg.getAmount()==0)){
			logger.warn("The GDQ must be an integer value");
			rep.setRetCode(RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
			rep.setMessage("The GDQ must be an integer value");
			return rep;
		}else if(reqMsg.getAmount() > configManager.getConfigLongValue(ConfigKeyEnum.ENTERMAXIMUMAMOUNT, 1000000000L)){
			logger.warn("Fill out the allowable amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_FILL_OUT_THE_ALLOWABLE_AMOUNT);
			rep.setMessage("Fill out the allowable amount");
			return rep;
		}
		
		HashMap<String, String> map = transferManager.transferInitiate(sessionData.getUserId(), reqMsg.getAreaCode(),
				reqMsg.getUserPhone(),reqMsg.getCurrency(), new BigDecimal(Double.toString(reqMsg.getAmount())), 
				reqMsg.getTransferComment(),0);
		
		if(map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)){
			rep.setTransferId(map.get("transferId"));
		}else if(map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_DAILY_PAY)){
			rep.setOpts(new String[]{map.get("msg")+" "+map.get("unit"),map.get("thawTime")});
		}else if(map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_EACH_TIME)){
			rep.setOpts(new String[]{map.get("msg")+" "+map.get("unit")});
		}else if(map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_NUM_OF_PAY_PER_DAY)){
			rep.setOpts(new String[]{map.get("msg"),map.get("thawTime")});
		}
		
		rep.setRetCode(map.get("retCode"));
		rep.setMessage(map.get("msg"));

		return rep;
		
	}
	
	@ApiOperation(value = "验证支付密码")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/transPwdConfirm")
	public @ResponseEncryptBody
	TransPwdConfirmResponse transPwdConfirm(@PathVariable String token,@RequestDecryptBody TransPwdConfirmRequest reqMsg){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		TransPwdConfirmResponse rep = new TransPwdConfirmResponse();
		HashMap<String,String> result = transferManager.payPwdConfirm(sessionData.getUserId(), 
				reqMsg.getTransferId(), reqMsg.getUserPayPwd());
		
		if(result.get("retCode").equals(RetCodeConsts.TRANSFER_REQUIRES_PHONE_VERIFICATION)){
			//发PIN码
			UserInfo user = userManager.getUserInfo(sessionData.getUserId());
			userManager.getPinCode(reqMsg.getTransferId(),user.getAreaCode(), user.getPhone());
			rep.setRetCode(result.get("retCode"));
			rep.setMessage("Need to send pin code verification");
		}else{
			rep.setRetCode(result.get("retCode"));
			rep.setMessage(result.get("msg"));
		}

		return rep;
	}
	
	@ApiOperation(value = "重新发送pin")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/resendPhonePin")
	public @ResponseEncryptBody
	ResendTransferPinResponse resendTransferPin(@PathVariable String token,@RequestDecryptBody ResendTransferPinRequest reqMsg){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		ResendTransferPinResponse rep = new ResendTransferPinResponse();
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		try {
			UserInfo user = userManager.getUserInfo(sessionData.getUserId());
			userManager.getPinCode(reqMsg.getTransferId(),user.getAreaCode(), user.getPhone());
		} catch (Exception e) {
			e.printStackTrace();
			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
			rep.setMessage("Re-sending the phone pin failed");
		}
		return rep;
	}
	
	
	@ApiOperation(value = "交易确认")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/transferConfirm")
	public  @ResponseEncryptBody
	TransferConfirmResponse transferConfirm(@PathVariable String token,@RequestDecryptBody TransferConfirmRequest reqMsg){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		
		UserInfo user = userManager.getUserInfo(sessionData.getUserId());
		TransferConfirmResponse rep = new TransferConfirmResponse();
		//判断PinCode是否正确
		if(userManager.testPinCode(reqMsg.getTransferId(), user.getAreaCode(), user.getPhone(),reqMsg.getPinCode())){
			String result = transferManager.transferConfirm(sessionData.getUserId(),reqMsg.getTransferId());
			
			if(result.equals(RetCodeConsts.RET_CODE_SUCCESS)){
				rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
				rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			}else{
				rep.setRetCode(result);
				rep.setMessage("Current balance is insufficient");
			}

		}else{
			rep.setRetCode(RetCodeConsts.PIN_CODE_INCORRECT);
			rep.setMessage("The pin code is incorrect");
		}
		return rep;
	}
	
	@ApiOperation(value = "发起转账请求")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/makeRequest")
	public  @ResponseEncryptBody
	MakeRequestResponse makeRequest(@PathVariable String token,@RequestDecryptBody MakeRequestRequest reqMsg){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		MakeRequestResponse rep = new MakeRequestResponse();		

		HashMap<String,Object> result = transferManager.makeRequest(sessionData.getUserId(), 
				reqMsg.getAreaCode(), reqMsg.getPhone(),
				reqMsg.getCurrency(), new BigDecimal(reqMsg.getAmount()));
		
		rep.setRetCode((String) result.get("retCode"));
		rep.setMessage((String) result.get("msg"));
		
		if(result.get("transferLimitPerPay") != null){
			rep.setOpts(new String[]{(String)result.get("transferLimitPerPay")+" "+result.get("unit")});
		}
		
		return rep;
	}
	
	@ApiOperation(value = "重新生成二维码")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/regenerateQRCode")
	public  @ResponseEncryptBody 
	RegenerateQRCodeResponse regenerateQRCode(@PathVariable String token,@RequestDecryptBody RegenerateQRCodeRequest reqMsg){
		RegenerateQRCodeResponse rep = new RegenerateQRCodeResponse();
		if(reqMsg == null || StringUtils.isEmpty(reqMsg.getCurrency())){
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			return rep;
		}
		HashMap<String, String> result = transferManager.regenerateQRCode(reqMsg.getCurrency(), reqMsg.getAmount());
		rep.setRetCode(result.get("retCode"));
		rep.setMessage(result.get("msg"));
		if(result.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_EACH_TIME)){
			rep.setOpts(new String[]{result.get("msg")+" "+result.get("unit")});
		}
		return rep;
	}
	
	
	@ApiOperation(value = "请求转账回应")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/requestATransfer")
	public @ResponseEncryptBody
	Respond2RequestResponse respond2Request(@PathVariable String token,@RequestDecryptBody Respond2RequestRequest reqMsg){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		Respond2RequestResponse rep = new Respond2RequestResponse();
		
		//装张金额上限
		if(reqMsg.getCurrency() != ServerConsts.CURRENCY_OF_GOLDPAY && reqMsg.getAmount() < 0.01){
			logger.warn("The input amount is less than the minimum amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
			rep.setMessage("The input amount is less than the minimum amount");
			return rep;
		}else if (reqMsg.getCurrency() == ServerConsts.CURRENCY_OF_GOLDPAY && reqMsg.getAmount() < 1){
			logger.warn("The input amount is less than the minimum amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
			rep.setMessage("The input amount is less than the minimum amount");
			return rep;
		}
		
		HashMap<String, String> map = transferManager.respond2Request(sessionData.getUserId(), reqMsg.getAreaCode(),
				reqMsg.getUserPhone(),reqMsg.getCurrency(), new BigDecimal(Double.toString(reqMsg.getAmount())), 
				null,reqMsg.getNoticeId());
		
		if(map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)){
			rep.setTransferId(map.get("transferId"));
		}else if(map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_DAILY_PAY)){
			rep.setOpts(new String[]{map.get("msg")+" "+map.get("unit"),map.get("thawTime")});
		}else if(map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_EACH_TIME)){
			rep.setOpts(new String[]{map.get("msg")+" "+map.get("unit")});
		}else if(map.get("retCode").equals(RetCodeConsts.TRANSFER_LIMIT_NUM_OF_PAY_PER_DAY)){
			rep.setOpts(new String[]{map.get("msg"),map.get("thawTime")});
		}
		
		rep.setRetCode(map.get("retCode"));
		rep.setMessage(map.get("msg"));

		return rep;

	}

	@ApiOperation(value = "获取交易明细")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/getTransactionRecord")
	public @ResponseEncryptBody
	GetTransactionRecordResponse getTransactionRecord(@PathVariable String token,@RequestDecryptBody GetTransactionRecordRequest reqMsq){
		
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		GetTransactionRecordResponse rep = new GetTransactionRecordResponse();
		HashMap<String,Object> map = transferManager.getTransactionRecordByPage(reqMsq.getPeriod(),reqMsq.getType() ,sessionData.getUserId(),
				reqMsq.getCurrentPage(), reqMsq.getPageSize());

		User systemUser = userManager.getSystemUser();
		ArrayList<TransferDTO> dtos = new ArrayList<>();
		rep.setList(dtos);
		if(((ArrayList<?>)map.get("list")).isEmpty()){
			rep.setRetCode(RetCodeConsts.TRANSFER_HISTORY_NOT_ACQUIRED);
			rep.setMessage("Transaction history not acquired");
		}else{
			List<?> list = (ArrayList<?>)map.get("list");
			for (Object object : list) {
				Object[] obj = (Object[]) object;
				
				TransferDTO dto = new TransferDTO();
				dto.setCurrency((String) obj[1]);
				dto.setCurrencyUnit(commonManager.getCurreny(dto.getCurrency()).getCurrencyUnit());
				
				if((int) obj[7] == ServerConsts.TRANSFER_TYPE_TRANSACTION){
					if(sessionData.getUserId() == (int) obj[0]){
						dto.setAmount(new BigDecimal("-"+obj[2]+"") );
						dto.setTransferType(0);
						dto.setPhoneNum((String) obj[3]);
					}else{
						dto.setAmount(new BigDecimal("+"+obj[2]+"") );
						dto.setTransferType(1);
						
						if((systemUser.getAreaCode()+systemUser.getUserPhone()).equals((String) obj[4])){
							dto.setPhoneNum((String) obj[3]);
						}else{
							dto.setPhoneNum((String) obj[4]);
						}
					}
				}else if ((int) obj[7] == ServerConsts.TRANSFER_TYPE_OUT_INVITE) {
					dto.setAmount(new BigDecimal("-"+obj[2]+"") );
					dto.setTransferType((int) obj[7]);
					dto.setPhoneNum((String) obj[3]);
				}else if ((int) obj[7] == ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND) {
					dto.setAmount(new BigDecimal("+"+obj[2]+"") );
					dto.setTransferType((int) obj[7]);
					dto.setPhoneNum((String) obj[3]);
				}else if ((int) obj[7] == ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW) {
					dto.setAmount(new BigDecimal("-"+obj[2]+"") );
					dto.setTransferType((int) obj[7]);
					dto.setPhoneNum((String) obj[3]);
				}else if ((int) obj[7] == ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE) {
					dto.setAmount(new BigDecimal("+"+obj[2]+"") );
					dto.setTransferType((int) obj[7]);
					dto.setPhoneNum((String) obj[4]);
				}
				dto.setComments("");
				dto.setFinishAt((Date) obj[6]);
				dto.setTransferId((String) obj[8]);

				dtos.add(dto);
			}
			
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			rep.setCurrentPage((int) map.get("currentPage"));
			rep.setPageSize((int) map.get("pageSize"));
			rep.setPageTotal((int) map.get("pageTotal"));
			rep.setTotal(Integer.parseInt(map.get("total")+""));
		}
		
		return rep;
		
	}
	
	@ApiOperation(value = "交易通知列表")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/getNotificationRecords")
	public @ResponseEncryptBody 
	GetNotificationRecordsResponse getNotificationRecords(@PathVariable String token,
			@RequestDecryptBody GetNotificationRecordsRequest reqMsg){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		GetNotificationRecordsResponse rep = new GetNotificationRecordsResponse();
		
		HashMap<String, Object> map = transferManager.getNotificationRecordsByPage(
				sessionData.getUserId(),reqMsg.getCurrentPage(), reqMsg.getPageSize());
		
		ArrayList<NotificationDTO> dtos = new ArrayList<>();
		rep.setList(dtos);
		if(((ArrayList<?>)map.get("list")).isEmpty()){
			rep.setRetCode(RetCodeConsts.TRANSFER_HISTORY_NOT_ACQUIRED);
			rep.setMessage("Notification Records not acquired");
		}else{
			
			List<?> list = (ArrayList<?>)map.get("list");
			for (Object object : list) {
				Object[] obj = (Object[]) object;
				
				NotificationDTO dto = new NotificationDTO();
				
				dto.setNoticeId((int) obj[0]);
				dto.setAreaCode((String) obj[1]);
				dto.setSponsorPhone((String) obj[2]);
				dto.setCurrency((String) obj[3]);
				dto.setAmount((new BigDecimal(obj[4]+"")).doubleValue());
				dto.setCreateAt((Date) obj[5]);
				dto.setTradingStatus((int) obj[6]);
				Currency currency = commonManager.getCurreny(dto.getCurrency());
				if (currency != null) {
					dto.setCurrencyUnit(currency.getCurrencyUnit());
				}else{
					dto.setCurrencyUnit("");
				}
				dtos.add(dto);
			}
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			rep.setCurrentPage((int) map.get("currentPage"));
			rep.setPageSize((int) map.get("pageSize"));
			rep.setPageTotal((int) map.get("pageTotal"));
			rep.setTotal(Integer.parseInt(map.get("total")+""));
		}
		return rep;
	}
	
	@ApiOperation(value = "获取交易详情")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/getTransDetails")
	public @ResponseEncryptBody 
	GetTransDetailsResponse getTransDetails(@PathVariable String token,
			@RequestDecryptBody GetTransDetailsRequest reqMsg){
		
		GetTransDetailsResponse rep = new GetTransDetailsResponse();
		
		SessionData sessionData = SessionDataHolder.getSessionData();
		HashMap<String, Object> result = transferManager.getTransDetails(reqMsg.getTransferId(), sessionData.getUserId());
		if(result.isEmpty()){
			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
			rep.setMessage("something wrong!");
		}else{
			
			User user = (User) result.get("user");
			Transfer transfer = (Transfer) result.get("transfer");
			
			if(user!= null){
				rep.setTrader(user.getUserName());
				rep.setRegiste(true);
			}else{
				rep.setTrader(transfer.getPhone());
				rep.setRegiste(false);
			}

			rep.setAreaCode((String)(result.get("areaCode")));
			rep.setPhone((String)(result.get("phone")));
			rep.setCurrency(transfer.getCurrency());	
			rep.setUnit((String)(result.get("unit")));
			
			rep.setAmount(transfer.getTransferAmount());
			rep.setTransferType(transfer.getTransferType());
			
			if(!(boolean)result.get("isPlus")){
				rep.setAmount(transfer.getTransferAmount().negate());
			}else if(transfer.getTransferType() == 0){
				rep.setTransferType(1);	
			}
			rep.setGoldpayName(MathUtils.hideString(transfer.getGoldpayName()));
			rep.setCreateTime(transfer.getCreateTime());
			rep.setFinishTime(transfer.getFinishTime());
			rep.setTransferId(transfer.getTransferId());
			rep.setFriend((boolean) result.get("isFriend"));
			
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);

		}
		
		return rep;
		
	}
	
	
}
