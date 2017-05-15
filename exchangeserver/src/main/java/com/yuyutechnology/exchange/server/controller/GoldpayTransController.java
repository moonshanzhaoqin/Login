package com.yuyutechnology.exchange.server.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.GoldpayTransManager;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.server.controller.dto.WithdrawDTO;
import com.yuyutechnology.exchange.server.controller.request.GetWithdrawRecordRequest;
import com.yuyutechnology.exchange.server.controller.request.GoldpayPurchaseRequest;
import com.yuyutechnology.exchange.server.controller.request.GoldpayTransConfirmRequest;
import com.yuyutechnology.exchange.server.controller.request.GoldpayWithdrawRequest;
import com.yuyutechnology.exchange.server.controller.request.RequestPinRequest;
import com.yuyutechnology.exchange.server.controller.request.WithdrawConfirmRequest;
import com.yuyutechnology.exchange.server.controller.response.GetWithdrawRecordResponse;
import com.yuyutechnology.exchange.server.controller.response.GoldpayPurchaseResponse;
import com.yuyutechnology.exchange.server.controller.response.GoldpayTransConfirmResponse;
import com.yuyutechnology.exchange.server.controller.response.GoldpayWithdrawResponse;
import com.yuyutechnology.exchange.server.controller.response.RequestPinResponse;
import com.yuyutechnology.exchange.server.controller.response.WithdrawConfirmResponse;
import com.yuyutechnology.exchange.server.security.annotation.RequestDecryptBody;
import com.yuyutechnology.exchange.server.security.annotation.ResponseEncryptBody;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionDataHolder;
import com.yuyutechnology.exchange.util.page.PageBean;

@Controller
public class GoldpayTransController {

	@Autowired
	GoldpayTransManager goldpayTransManager;
	@Autowired
	ConfigManager configManager;
	@Autowired
	CommonManager commonManager;

	public static Logger logger = LogManager.getLogger(GoldpayTransController.class);

	@ApiOperation(value = "goldpay 买入")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/goldpayTrans/goldpayPurchase")
	public @ResponseEncryptBody GoldpayPurchaseResponse goldpayPurchase(@PathVariable String token,
			@RequestDecryptBody GoldpayPurchaseRequest reqMsg) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		GoldpayPurchaseResponse rep = new GoldpayPurchaseResponse();

		if (reqMsg.getAmount() == 0 || reqMsg.getAmount() % 1 > 0) {
			logger.warn("The input amount is less than the minimum amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
			rep.setMessage("The input amount is less than the minimum amount");
			return rep;
		} else if (reqMsg.getAmount() > configManager.getConfigLongValue(ConfigKeyEnum.ENTERMAXIMUMAMOUNT,
				1000000000L)) {
			logger.warn("Fill out the allowable amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_FILL_OUT_THE_ALLOWABLE_AMOUNT);
			rep.setMessage("Fill out the allowable amount");
			return rep;
		}

		HashMap<String, String> map = goldpayTransManager.goldpayPurchase(sessionData.getUserId(),
				new BigDecimal(reqMsg.getAmount()));

		if (map != null && map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)) {
			rep.setTransferId(map.get("transferId"));
		}

		rep.setRetCode(map.get("retCode"));
		rep.setMessage(map.get("msg"));

		return rep;

	}

	@ApiOperation(value = "goldpay 重新发送Pin")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/goldpayTrans/requestPin")
	public @ResponseEncryptBody RequestPinResponse requestPin(@PathVariable String token,
			@RequestDecryptBody RequestPinRequest reqMsg) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		RequestPinResponse rep = new RequestPinResponse();

		HashMap<String, String> map = goldpayTransManager.requestPin(sessionData.getUserId(), reqMsg.getTransferId());
		if (map != null && map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)) {
			rep.setTransferId(map.get("transferId"));
		}

		rep.setRetCode(map.get("retCode"));
		rep.setMessage(map.get("msg"));

		return rep;
	}

	@ApiOperation(value = "goldpay 交易确认")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/goldpayTrans/goldpayTransConfirm")
	public @ResponseEncryptBody GoldpayTransConfirmResponse goldpayTransConfirm(@PathVariable String token,
			@RequestDecryptBody GoldpayTransConfirmRequest reqMsg) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		GoldpayTransConfirmResponse rep = new GoldpayTransConfirmResponse();
		HashMap<String, String> map = goldpayTransManager.goldpayTransConfirm(sessionData.getUserId(), reqMsg.getPin(),
				reqMsg.getTransferId());

		rep.setRetCode(map.get("retCode"));
		rep.setMessage(map.get("msg"));

		return rep;
	}

	@ApiOperation(value = "goldpay 提现")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/goldpayTrans/goldpayWithdraw")
	public @ResponseEncryptBody GoldpayWithdrawResponse goldpayWithdraw(@PathVariable String token,
			@RequestDecryptBody GoldpayWithdrawRequest reqMsg) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		GoldpayWithdrawResponse rep = new GoldpayWithdrawResponse();

		if (reqMsg.getAmount() == 0 || reqMsg.getAmount() % 1 > 0) {
			logger.warn("The input amount is less than the minimum amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
			rep.setMessage("The input amount is less than the minimum amount");
			return rep;
		} else if (reqMsg.getAmount() > configManager.getConfigLongValue(ConfigKeyEnum.ENTERMAXIMUMAMOUNT,
				1000000000L)) {
			logger.warn("Fill out the allowable amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_FILL_OUT_THE_ALLOWABLE_AMOUNT);
			rep.setMessage("Fill out the allowable amount");
			return rep;
		}

		HashMap<String, String> map = goldpayTransManager.goldpayWithdraw(sessionData.getUserId(), reqMsg.getAmount());

		if (map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)) {
			rep.setTransferId(map.get("transferId"));
			rep.setFee((Long.valueOf(map.get("charge")).doubleValue()));
		}

		rep.setRetCode(map.get("retCode"));
		rep.setMessage(map.get("msg"));

		return rep;
	}

	@ApiOperation(value = "goldpay 提现确认")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/goldpayTrans/withdrawConfirm")
	public @ResponseEncryptBody WithdrawConfirmResponse withdrawConfirm(@PathVariable String token,
			@RequestDecryptBody WithdrawConfirmRequest reqMsg) {
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		WithdrawConfirmResponse rep = new WithdrawConfirmResponse();
		if (StringUtils.isEmpty(reqMsg.getPayPwd())) {
			rep.setRetCode(RetCodeConsts.TRANSFER_PAYMENTPWD_INCORRECT);
			rep.setMessage("The payment password is incorrect");
			return rep;
		}
		HashMap<String, String> map = goldpayTransManager.withdrawConfirm1(sessionData.getUserId(), reqMsg.getPayPwd(),
				reqMsg.getTransferId());
		// if(map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)){
		// HashMap<String, String> map2 =
		// goldpayTransManager.withdrawConfirm2(sessionData.getUserId(),
		// reqMsg.getTransferId());
		// rep.setRetCode(map2.get("retCode"));
		// rep.setMessage(map2.get("msg"));
		// return rep;
		// }
		rep.setRetCode(map.get("retCode"));
		rep.setMessage(map.get("msg"));
		rep.setOpts(new String[]{map.get("msg")});
		return rep;
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "goldpay 提现进度列表")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/goldpayTrans/getWithdrawRecord")
	public @ResponseEncryptBody GetWithdrawRecordResponse getWithdrawRecord(@PathVariable String token,
			@RequestDecryptBody GetWithdrawRecordRequest getWithdrawRecordRequest) {
		GetWithdrawRecordResponse rep = new GetWithdrawRecordResponse();

		SessionData sessionData = SessionDataHolder.getSessionData();
		PageBean pageBean = goldpayTransManager.getWithdrawRecordByPage(sessionData.getUserId(),
				getWithdrawRecordRequest.getCurrentPage(), getWithdrawRecordRequest.getPageSize());

		List<WithdrawDTO> dtos = new ArrayList<>();

		if (pageBean.getRows().isEmpty()) {
			rep.setList(dtos);
			rep.setRetCode(RetCodeConsts.TRANSFER_HISTORY_NOT_ACQUIRED);
			rep.setMessage("Withdraw Records not acquired");
		} else {

			List<Transfer> list = (List<Transfer>) pageBean.getRows();
			for (Transfer transfer : list) {
				WithdrawDTO withdrawDTO = new WithdrawDTO();
				withdrawDTO.setCurrency(ServerConsts.CURRENCY_OF_GOLDPAY);
				withdrawDTO.setAmount(transfer.getTransferAmount());
				withdrawDTO.setCreateTime(transfer.getCreateTime());
				if (transfer.getTransferStatus() == ServerConsts.TRANSFER_STATUS_OF_PROCESSING
						|| transfer.getTransferStatus() == ServerConsts.TRANSFER_STATUS_OF_AUTOREVIEW_SUCCESS
						|| transfer.getTransferStatus() == ServerConsts.TRANSFER_STATUS_OF_AUTOREVIEW_FAIL
//						||transfer.getTransferStatus()==ServerConsts.TRANSFER_STATUS_OF_MANUALREVIEW_FAIL
//						||transfer.getTransferStatus()==ServerConsts.TRANSFER_STATUS_OF_MANUALREVIEW_SUCCESS
						||transfer.getTransferStatus()==ServerConsts.TRANSFER_STATUS_OF_GOLDPAYREMIT_FAIL) {
					withdrawDTO.setWithdrawStatus(0);
				} else if (transfer.getTransferStatus() == ServerConsts.TRANSFER_STATUS_OF_REFUND) {
					withdrawDTO.setWithdrawStatus(1);
				}
				withdrawDTO.setCurrencyUnit(commonManager.getCurreny(transfer.getCurrency()).getCurrencyUnit());
				dtos.add(withdrawDTO);
			}
			rep.setList(dtos);
			rep.setCurrentPage(pageBean.getCurrentPage());
			rep.setPageTotal(pageBean.getPageTotal());
			rep.setPageSize(pageBean.getPageSize());
			rep.setTotal((int) pageBean.getTotal());

			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		}

		return rep;
	}
}
