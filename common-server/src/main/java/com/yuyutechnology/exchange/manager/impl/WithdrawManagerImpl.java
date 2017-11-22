package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CrmAlarmDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WithdrawDAO;
import com.yuyutechnology.exchange.dto.FeeResult;
import com.yuyutechnology.exchange.dto.WithdrawCalResult;
import com.yuyutechnology.exchange.enums.FeePurpose;
import com.yuyutechnology.exchange.mail.MailManager;
import com.yuyutechnology.exchange.manager.CheckManager;
import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.FeeManager;
import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;
import com.yuyutechnology.exchange.manager.TransDetailsManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.manager.WithdrawManager;
import com.yuyutechnology.exchange.pojo.CrmAlarm;
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
	CrmAlarmDAO crmAlarmDAO;
	@Autowired
	CrmAlarmManager crmAlarmManager;
	@Autowired
	CheckManager checkManager;

	// 计算N根金条需要的GDQ和手续费
	@Override
	public WithdrawCalResult withdrawCalculate(Integer userId, int goldBullion) {
		WithdrawCalResult result = new WithdrawCalResult();
		BigDecimal goldpayAmount = goldbullion2gold.multiply(gold2goldpay).multiply(new BigDecimal(goldBullion));

		FeeResult feeResult;
		if (userManager.isHappyLivesVIP(userId)) {
			feeResult = feeManager.figureOutFee(FeePurpose.PayPal_Purchase_GoldBullion_VIP, goldpayAmount);
		} else {
			feeResult = feeManager.figureOutFee(FeePurpose.PayPal_Purchase_GoldBullion_Ordinary, goldpayAmount);
		}

		if (checkManager.isInsufficientBalance(userId, ServerConsts.CURRENCY_OF_GOLDPAY,
				goldpayAmount.add(feeResult.getFee()))) {

		}
		if (checkManager.isInsufficientBalance(userId, ServerConsts.CURRENCY_OF_GOLDPAY,
				goldpayAmount.add(feeResult.getFee()))) {
			result.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			result.setGoldpay(goldpayAmount);
			result.setFee(feeResult.getFee());
			result.setFormula(feeResult.getFormula());
		} else {
			result.setRetCode(RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
		}
		return result;
	}

	@Override
	public String applyConfirm(Integer userId, int goldBullion) {
		WithdrawCalResult result= withdrawCalculate(userId, goldBullion);
		if (result.getRetCode().equals(RetCodeConsts.RET_CODE_SUCCESS)) {
			User frozenUser = userDAO.getFrozenUser();

			Withdraw withdraw = new Withdraw(userId, goldBullion, result.getGoldpay(),
					result.getFee(), new Date());
			/* 把Goldpay转到冻结账户 */
			withdraw.setGoldTransferA(transfer4Withdraw(userId, frozenUser.getUserId(), result.getGoldpay(),
					ServerConsts.TRANSFER_TYPE_IN_WITHDRAW));
			/* 把手续费转到冻结账户 */
			withdraw.setFeeTransferA(transfer4Withdraw(userId, frozenUser.getUserId(),result.getFee(),
					ServerConsts.TRANSFER_TYPE_IN_FEE));
			withdrawDAO.saveWithdraw(withdraw);
			/* 通知管理员 */
			notifyWithdraw(userId, goldBullion);
			return RetCodeConsts.RET_CODE_SUCCESS;
		}else {
			return result.getRetCode();
		}

	}

	@Override
	public String cancelWithdraw(Integer withdrawId, String adminName) {
		Withdraw withdraw = withdrawDAO.getWithdraw(withdrawId);
		if (withdraw.getHandleResult() == ServerConsts.WITHDRAW_HANDLE_RESULT_DEFAULT) {
			User frozenUser = userDAO.getFrozenUser();
			
			/* 把Goldpay退回给用户 */
			withdraw.setGoldTransferB(transfer4Withdraw(frozenUser.getUserId(), withdraw.getUserId(),
					withdraw.getGoldpay(), ServerConsts.TRANSFER_TYPE_IN_WITHDRAW_REFUND));
			/* 把手续费退回给用户 */
			withdraw.setFeeTransferB(transfer4Withdraw(frozenUser.getUserId(), withdraw.getUserId(), withdraw.getFee(),
					ServerConsts.TRANSFER_TYPE_IN_WITHDRAW_REFUND));
			/* 操作记录 */
			withdraw.setHandleResult(ServerConsts.WITHDRAW_HANDLE_RESULT_CANCEL);
			withdraw.setHandler(adminName);
			withdraw.setHandleTime(new Date());
			withdrawDAO.updateWithdraw(withdraw);
			return RetCodeConsts.RET_CODE_SUCCESS;
		} else {
			logger.error("withdraw {} is handled");
			return RetCodeConsts.RET_CODE_FAILUE;
		}

	}

	@Override
	public String finishWithdraw(Integer withdrawId, String adminName) {
		Withdraw withdraw = withdrawDAO.getWithdraw(withdrawId);
		if (withdraw.getHandleResult() == ServerConsts.WITHDRAW_HANDLE_RESULT_DEFAULT) {
			User frozenUser = userDAO.getFrozenUser();
			User feeUser = userDAO.getFeeUser();
			User recovery = userDAO.getRecoveryUser();
			
			/* 把Goldpay转到回收账户 */
			withdraw.setGoldTransferB(transfer4Withdraw(frozenUser.getUserId(), recovery.getUserId(),
					withdraw.getGoldpay(), ServerConsts.TRANSFER_TYPE_IN_WITHDRAW_REFUND));
			/* 把手续费转到手续费账户 */
			withdraw.setFeeTransferB(transfer4Withdraw(frozenUser.getUserId(), feeUser.getUserId(), withdraw.getFee(),
					ServerConsts.TRANSFER_TYPE_IN_WITHDRAW_REFUND));
			/* 操作记录 */
			withdraw.setHandleResult(ServerConsts.WITHDRAW_HANDLE_RESULT_FINISHT);
			withdraw.setHandler(adminName);
			withdraw.setHandleTime(new Date());
			withdrawDAO.updateWithdraw(withdraw);
			return RetCodeConsts.RET_CODE_SUCCESS;
		} else {
			logger.error("withdraw {} is handled");
			return RetCodeConsts.RET_CODE_FAILUE;
		}
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
		transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
		transfer.setUserFrom(from);
		transfer.setUserTo(to);
		transfer.setGoldpayOrderId(goldpayOrderId);
		transferDAO.addTransfer(transfer);
		transDetailsManager.addTransDetails(transferId, from, to, "", "", "", ServerConsts.CURRENCY_OF_GOLDPAY, amount,
				BigDecimal.ZERO, null, "", type);
		goldpayTrans4MergeManager.updateWallet4GoldpayTrans(transferId);
		return transferId;

	}

	// TODO
	public void notifyWithdraw(Integer userId, int goldBullion) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		List<CrmAlarm> list = crmAlarmDAO.getConfigListByTypeAndStatus(ServerConsts.ALARM_TYPE_WITHDRAW, 1);
		if (list != null && !list.isEmpty()) {
			logger.info("notifyWithdraw listSize: {}", list.size());
			for (int i = 0; i < list.size(); i++) {
				CrmAlarm crmAlarm = list.get(i);
				logger.info("notifyWithdraw : {}", crmAlarm.getSupervisorIdArr());
				crmAlarmManager.alarmNotice(crmAlarm.getSupervisorIdArr(), "registrationAlarm", crmAlarm.getAlarmMode(),
						params);
			}
		}
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

}
