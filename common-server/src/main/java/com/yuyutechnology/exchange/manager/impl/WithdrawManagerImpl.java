package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WithdrawDAO;
import com.yuyutechnology.exchange.dto.FeeResult;
import com.yuyutechnology.exchange.dto.NotifyWithdrawDTO;
import com.yuyutechnology.exchange.dto.WithdrawCalResult;
import com.yuyutechnology.exchange.dto.WithdrawDTO;
import com.yuyutechnology.exchange.dto.WithdrawDetailDTO;
import com.yuyutechnology.exchange.enums.FeePurpose;
import com.yuyutechnology.exchange.mail.MailManager;
import com.yuyutechnology.exchange.manager.CheckManager;
import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.FeeManager;
import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;
import com.yuyutechnology.exchange.manager.TransDetailsManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.manager.WithdrawManager;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Withdraw;
import com.yuyutechnology.exchange.util.DateFormatUtils;
import com.yuyutechnology.exchange.util.ResourceUtils;
import com.yuyutechnology.exchange.util.page.PageBean;

@Service
public class WithdrawManagerImpl implements WithdrawManager {
	private static Logger logger = LogManager.getLogger(WithdrawManagerImpl.class);

	private static BigDecimal gold2goldpay = new BigDecimal("10000");
	private static BigDecimal goldbullion2gold = new BigDecimal(
			ResourceUtils.getBundleValue4String("gold.bullion.g", "187"));

	@Autowired
	FeeManager feeManager;
	@Autowired
	UserManager userManager;
	@Autowired
	TransferDAO transferDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	WithdrawDAO withdrawDAO;
	@Autowired
	GoldpayTrans4MergeManager goldpayTrans4MergeManager;
	@Autowired
	TransDetailsManager transDetailsManager;
	@Autowired
	MailManager mailManager;
	@Autowired
	CrmAlarmManager crmAlarmManager;
	@Autowired
	CheckManager checkManager;

	@Override
	public WithdrawCalResult withdrawCalculate(Integer userId, int goldBullion) {
		logger.info("calculate {} gold bullion --> ", goldBullion);
		WithdrawCalResult result = new WithdrawCalResult();

		BigDecimal goldpayAmount = goldbullion2gold.multiply(gold2goldpay).multiply(new BigDecimal(goldBullion));
		logger.info("goldpayAmount is {}", goldpayAmount);

		FeeResult feeResult;
		if (userManager.isHappyLivesVIP(userId)) {
			feeResult = feeManager.figureOutFee(FeePurpose.Withdraw_GoldBullion_VIP, goldpayAmount);
		} else {
			feeResult = feeManager.figureOutFee(FeePurpose.Withdraw_GoldBullion_Ordinary, goldpayAmount);
		}

		if (checkManager.isInsufficientBalance(userId, ServerConsts.CURRENCY_OF_GOLDPAY,
				goldpayAmount.add(feeResult.getFee()))) {
			result.setRetCode(RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
		} else {
			result.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			result.setGoldpay(goldpayAmount);
			result.setFee(feeResult.getFee());
			result.setFormula(feeResult.getFormula());
		}
		return result;
	}

	@Override
	public String applyConfirm(Integer userId, int goldBullion, String userEmail) {
		WithdrawCalResult result = withdrawCalculate(userId, goldBullion);
		if (result.getRetCode().equals(RetCodeConsts.RET_CODE_SUCCESS)) {
			User frozenUser = userDAO.getFrozenUser();

			String withdrawId = withdrawDAO.createWithdrawId();
			logger.info("create withdrawId : {}", withdrawId);
			Withdraw withdraw = new Withdraw(withdrawId, userId, userEmail, goldBullion, result.getGoldpay(),
					result.getFee(), new Date(), ServerConsts.WITHDRAW_RESULT_DEFAULT);

			/* 把Goldpay转到冻结账户 */
			String goldTransferA = transfer4Withdraw(userId, frozenUser.getUserId(), withdraw.getGoldpay(),
					ServerConsts.TRANSFER_TYPE_IN_WITHDRAW);
			withdraw.setGoldTransferA(goldTransferA);
			logger.info("transfer goldpay to frozenUser, tansferId : {} ", goldTransferA);

			/* 把手续费转到冻结账户 */
			if (withdraw.getFee().compareTo(BigDecimal.ZERO) > 0) {
				String feeTransferA = transfer4Withdraw(userId, frozenUser.getUserId(), withdraw.getFee(),
						ServerConsts.TRANSFER_TYPE_IN_FEE);
				withdraw.setFeeTransferA(feeTransferA);
				logger.info("transfer fee to frozenUser, tansferId : {} ", feeTransferA);
			}

			withdrawDAO.saveWithdraw(withdraw);
			return withdrawId;
		} else {
			return null;
		}
	}

	@Override
	public String goldpayTrans4Apply(String withdrawId) {
		Withdraw withdraw = withdrawDAO.getWithdraw(withdrawId);
		HashMap<String, String> result = goldpayTrans4MergeManager.updateWallet4FeeTrans(withdraw.getGoldTransferA(),
				withdraw.getFeeTransferA());
		if (result.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)) {
			logger.info("***goldpayTrans4Apply success ***");
			User user = userDAO.getUser(withdraw.getUserId());
			/* 通知管理员 */
			crmAlarmManager
					.notifyWithdraw(new NotifyWithdrawDTO(withdraw.getUserId(), user.getAreaCode(), user.getUserPhone(),
							user.getUserName(), withdraw.getUserEmail(), withdraw.getQuantity(), new Date()));

			withdraw.setHandleResult(ServerConsts.WITHDRAW_RESULT_APPLY_SUCCESS);
			withdrawDAO.updateWithdraw(withdraw);
		} else {
			logger.info("*** fail ***");
			withdraw.setHandleResult(ServerConsts.WITHDRAW_RESULT_APPLY_FAIL);
			withdrawDAO.updateWithdraw(withdraw);
		}
		return result.get("retCode");
	}

	@Override
	public String cancelWithdraw(String withdrawId) {
		Withdraw withdraw = withdrawDAO.getWithdraw(withdrawId);
		if (withdraw.getHandleResult() == ServerConsts.WITHDRAW_RESULT_APPLY_SUCCESS) {
			User frozenUser = userDAO.getFrozenUser();

			/* 把Goldpay退回给用户 */
			if (withdraw.getGoldTransferB() == null) {
				String goldTransferB = transfer4Withdraw(frozenUser.getUserId(), withdraw.getUserId(),
						withdraw.getGoldpay(), ServerConsts.TRANSFER_TYPE_IN_WITHDRAW_REFUND);
				withdraw.setGoldTransferB(goldTransferB);
				logger.info("transfer goldpay from frozenUser to user, tansferId : {} ", goldTransferB);
			}

			/* 把手续费退回给用户 */
			if (withdraw.getFee().compareTo(BigDecimal.ZERO) > 0 && withdraw.getFeeTransferB() == null) {

				String feeTransferB = transfer4Withdraw(frozenUser.getUserId(), withdraw.getUserId(), withdraw.getFee(),
						ServerConsts.TRANSFER_TYPE_IN_WITHDRAW_REFUND);
				withdraw.setFeeTransferB(feeTransferB);
				logger.info("transfer fee from frozenUser to user, tansferId : {} ", feeTransferB);
			}

			withdrawDAO.updateWithdraw(withdraw);
			return RetCodeConsts.RET_CODE_SUCCESS;
		} else {
			logger.error("withdraw {} is handled");
			return RetCodeConsts.RET_CODE_FAILUE;
		}

	}

	@Override
	public String goldpayTrans4cancel(String withdrawId, String adminName) {
		Withdraw withdraw = withdrawDAO.getWithdraw(withdrawId);
		HashMap<String, String> result = goldpayTrans4MergeManager.updateWallet4FeeTrans(withdraw.getGoldTransferB(),
				withdraw.getFeeTransferB());
		if (result.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)) {
			logger.info("*** goldpayTrans4cancel success ***");
			/* 操作记录 */
			withdraw.setHandleResult(ServerConsts.WITHDRAW_RESULT_CANCEL);
			withdraw.setHandler(adminName);
			withdraw.setHandleTime(new Date());
			withdrawDAO.updateWithdraw(withdraw);
		}
		return result.get("retCode");
	}

	@Override
	public String finishWithdraw(String withdrawId) {
		Withdraw withdraw = withdrawDAO.getWithdraw(withdrawId);
		if (withdraw.getHandleResult() == ServerConsts.WITHDRAW_RESULT_APPLY_SUCCESS) {
			User frozenUser = userDAO.getFrozenUser();
			User feeUser = userDAO.getFeeUser();
			User recoveryUser = userDAO.getRecoveryUser();
			if (withdraw.getGoldTransferB() == null) {
				/* 把Goldpay转到回收账户 */
				String goldTransferB = transfer4Withdraw(frozenUser.getUserId(), recoveryUser.getUserId(),
						withdraw.getGoldpay(), ServerConsts.TRANSFER_TYPE_IN_WITHDRAW_REFUND);
				withdraw.setGoldTransferB(goldTransferB);
				logger.info("transfer goldpay from frozenUser to recoveryUser, tansferId : {} ", goldTransferB);
			}
			if (withdraw.getFee().compareTo(BigDecimal.ZERO) > 0 && withdraw.getFeeTransferB() == null) {
				/* 把手续费转到手续费账户 */
				String feeTransferB = transfer4Withdraw(frozenUser.getUserId(), feeUser.getUserId(), withdraw.getFee(),
						ServerConsts.TRANSFER_TYPE_IN_WITHDRAW_REFUND);
				withdraw.setFeeTransferB(feeTransferB);
				logger.info("transfer goldpay from frozenUser to feeUser, tansferId : {} ", feeTransferB);
			}
			withdrawDAO.updateWithdraw(withdraw);
			return RetCodeConsts.RET_CODE_SUCCESS;
		} else {
			logger.error("withdraw {} is handled");
			return RetCodeConsts.RET_CODE_FAILUE;
		}
	}

	@Override
	public String goldpayTrans4finish(String withdrawId, String adminName) {
		Withdraw withdraw = withdrawDAO.getWithdraw(withdrawId);
		HashMap<String, String> result = goldpayTrans4MergeManager.updateWallet4FeeTrans(withdraw.getGoldTransferB(),
				withdraw.getFeeTransferB());
		if (result.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)) {
			logger.info("*** goldpayTrans4finish success ***");
			/* 操作记录 */
			withdraw.setHandleResult(ServerConsts.WITHDRAW_RESULT_FINISHT);
			withdraw.setHandler(adminName);
			withdraw.setHandleTime(new Date());
			withdrawDAO.updateWithdraw(withdraw);
		}
		return result.get("retCode");
	}

	private String transfer4Withdraw(Integer from, Integer to, BigDecimal amount, int type) {
		String goldpayOrderId = goldpayTrans4MergeManager.getGoldpayOrderId();
		/* 生成TransId */
		String transferId = transferDAO.createTransId(type);
		Transfer transfer = new Transfer();
		transfer.setTransferId(transferId);
		transfer.setCreateTime(new Date());
		transfer.setCurrency(ServerConsts.CURRENCY_OF_GOLDPAY);
		transfer.setTransferAmount(amount);
		transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		transfer.setUserFrom(from);
		transfer.setUserTo(to);
		transfer.setGoldpayOrderId(goldpayOrderId);
		transferDAO.addTransfer(transfer);

		transDetailsManager.addTransDetails(transferId, from, to, "", "", "", ServerConsts.CURRENCY_OF_GOLDPAY, amount,
				BigDecimal.ZERO, null, "", type);

		return transferId;

	}

	@Override
	public PageBean getWithdrawByPage(int currentPage, String userPhone, String userName, String startTime,
			String endTime) {
		logger.info("currentPage={},userPhone={},userName={}  {}->{}", currentPage, userPhone, userName, startTime,
				endTime);

		List<Object> values = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from Withdraw w,User u where w.userId = u.userId");
		if (StringUtils.isNotBlank(userPhone)) {
			hql.append("and u.userPhone = ?");
			values.add(userPhone);
		}
		if (StringUtils.isNotBlank(userName)) {
			hql.append("and u.userName like ?");
			values.add("%" + userName + "%");
		}
		if (StringUtils.isNotBlank(startTime)) {
			hql.append("and w.applyTime >   ?");
			values.add(DateFormatUtils.getStartTime(startTime));
		}
		if (StringUtils.isNotBlank(endTime)) {
			hql.append("and  w.applyTime < ?");
			values.add(DateFormatUtils.getEndTime(endTime));
		}
		hql.append(" order by w.applyTime desc");
		return withdrawDAO.getWithdrawByPage(hql.toString(), values, currentPage, 10);

	}

	@Override
	public List<WithdrawDTO> getWithdrawRecord(Integer userId) {
		List<WithdrawDTO> list = new ArrayList<>();
		List<Withdraw> withdraws = withdrawDAO.listWithdrawByUserId(userId);

		for (Withdraw withdraw : withdraws) {
			WithdrawDTO withdrawDTO = new WithdrawDTO();
			withdrawDTO.setWithdrawId(withdraw.getWithdrawId());
			withdrawDTO.setQuantity(withdraw.getQuantity());
			withdrawDTO.setHandleResult(withdraw.getHandleResult());
			withdrawDTO.setApplyTime(withdraw.getApplyTime());
			list.add(withdrawDTO);
		}

		return list;
	}

	@Override
	public WithdrawDetailDTO getWithdrawDetail(String withdrawId) {
		Withdraw withdraw=withdrawDAO.getWithdraw(withdrawId);
		WithdrawDetailDTO withdrawDetailDTO=new WithdrawDetailDTO();
		withdrawDetailDTO.setWithdrawId(withdrawId);
		withdrawDetailDTO.setApplyTime(withdraw.getApplyTime());
		withdrawDetailDTO.setFee(withdraw.getFee());
		withdrawDetailDTO.setGoldpay(withdraw.getGoldpay());
		withdrawDetailDTO.setHandleResult(withdraw.getHandleResult());
		withdrawDetailDTO.setQuantity(withdraw.getQuantity());
		withdrawDetailDTO.setHandleTime(withdraw.getHandleTime());
		return withdrawDetailDTO;
	}

}
