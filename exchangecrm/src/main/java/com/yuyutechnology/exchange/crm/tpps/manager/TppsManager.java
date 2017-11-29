package com.yuyutechnology.exchange.crm.tpps.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.crm.tpps.TppsConsts;
import com.yuyutechnology.exchange.crm.tpps.dao.GoldqPayClientDAO;
import com.yuyutechnology.exchange.crm.tpps.dao.GoldqPayFeeDAO;
import com.yuyutechnology.exchange.crm.tpps.pojo.GoldqPayClient;
import com.yuyutechnology.exchange.crm.tpps.pojo.GoldqPayFee;
import com.yuyutechnology.exchange.util.UidUtils;
import com.yuyutechnology.exchange.util.page.PageBean;

@Service
public class TppsManager {
	private static Logger logger = LogManager.getLogger(TppsManager.class);

	@Autowired
	GoldqPayClientDAO goldqPayClientDAO;
	@Autowired
	GoldqPayFeeDAO goldqPayFeeDAO;

	public void addGoldqPayClient(Integer exId) {
		/* 生成商户ClientId */
		GoldqPayClient goldqPayClient = new GoldqPayClient(exId, UidUtils.genUid(), UidUtils.genUid());
		goldqPayClientDAO.saveGoldqPayClient(goldqPayClient);
		/* 生成默认手续费模板 */
		goldqPayFeeDAO.saveGoldqPayFee(new GoldqPayFee(goldqPayClient.getClientId(), TppsConsts.PAY_ROLE_PAYER, BigDecimal.ZERO,  BigDecimal.ZERO,  BigDecimal.ZERO,  BigDecimal.ZERO, TppsConsts.PAY_ROLE_PAYEE));
		goldqPayFeeDAO.saveGoldqPayFee(new GoldqPayFee(goldqPayClient.getClientId(), TppsConsts.PAY_ROLE_PAYEE,  BigDecimal.ZERO,  BigDecimal.ZERO,  BigDecimal.ZERO,  BigDecimal.ZERO, TppsConsts.PAY_ROLE_PAYEE));
	}

	public void updateGoldqPayFee(GoldqPayFee goldqPayFee) {
		goldqPayFeeDAO.updateGoldqPayFee(goldqPayFee);

	}

	public List<GoldqPayFee> getGoldqPayFeeByClientId(String clientId) {
		List<GoldqPayFee> goldqPayFees=goldqPayFeeDAO.getGoldqPayFeeByClientId(clientId);
		if (goldqPayFees.isEmpty()) {
			/* 生成默认手续费模板 */
			goldqPayFeeDAO.saveGoldqPayFee(new GoldqPayFee(clientId, TppsConsts.PAY_ROLE_PAYER, BigDecimal.ZERO,  BigDecimal.ZERO,  BigDecimal.ZERO,  BigDecimal.ZERO, TppsConsts.PAY_ROLE_PAYEE));
			goldqPayFeeDAO.saveGoldqPayFee(new GoldqPayFee(clientId, TppsConsts.PAY_ROLE_PAYEE,  BigDecimal.ZERO,  BigDecimal.ZERO,  BigDecimal.ZERO,  BigDecimal.ZERO, TppsConsts.PAY_ROLE_PAYEE));
			goldqPayFees=goldqPayFeeDAO.getGoldqPayFeeByClientId(clientId);
		}
		return goldqPayFees;
	}

	public PageBean getGoldqPayClientByPage(int currentPage) {
		// logger.info("currentPage={},userPhone={},userName={} {}->{}", currentPage,
		// userPhone, userName, startTime,
		// endTime);
		logger.info("currentPage={}", currentPage);
		List<Object> values = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from GoldqPayClient where 1=1");
		// if (StringUtils.isNotBlank(userPhone)) {
		// hql.append("and u.userPhone = ?");
		// values.add(userPhone);
		// }
		// if (StringUtils.isNotBlank(userName)) {
		// hql.append("and u.userName like ?");
		// values.add("%" + userName + "%");
		// }
		// if (StringUtils.isNotBlank(startTime)) {
		// hql.append("and w.applyTime > ?");
		// values.add(DateFormatUtils.getStartTime(startTime));
		// }
		// if (StringUtils.isNotBlank(endTime)) {
		// hql.append("and w.applyTime < ?");
		// values.add(DateFormatUtils.getEndTime(endTime));
		// }
		// hql.append(" order by w.applyTime desc");
		return goldqPayClientDAO.getGoldqPayClientByPage(hql.toString(), values, currentPage, 10);
	}

	public void updateGoldqPayClient(GoldqPayClient goldqPayClient) {
		goldqPayClientDAO.updateGoldqPayClient(goldqPayClient);
		
	}
}
