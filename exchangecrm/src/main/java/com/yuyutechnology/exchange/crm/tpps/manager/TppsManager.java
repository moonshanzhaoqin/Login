package com.yuyutechnology.exchange.crm.tpps.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.crm.tpps.TppsConsts;
import com.yuyutechnology.exchange.crm.tpps.dao.TppsDAO;
import com.yuyutechnology.exchange.crm.tpps.pojo.GoldqPayClient;
import com.yuyutechnology.exchange.crm.tpps.pojo.GoldqPayFee;
import com.yuyutechnology.exchange.util.UidUtils;
import com.yuyutechnology.exchange.util.page.PageBean;

@Service
public class TppsManager {
	private static Logger logger = LogManager.getLogger(TppsManager.class);

	@Autowired
	TppsDAO tppsDAO;

	public void addGoldqPayClient(Integer exId) {
		/* 生成商户ClientId */
		GoldqPayClient goldqPayClient = new GoldqPayClient(exId, UidUtils.genUid(), UidUtils.genUid());
		tppsDAO.saveGoldqPayClient(goldqPayClient);
		/* 生成默认手续费模板 */
		tppsDAO.saveGoldqPayFee(new GoldqPayFee(goldqPayClient.getClientId(), TppsConsts.PAY_ROLE_PAYER,
				BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, TppsConsts.PAY_ROLE_PAYEE));
		tppsDAO.saveGoldqPayFee(new GoldqPayFee(goldqPayClient.getClientId(), TppsConsts.PAY_ROLE_PAYEE,
				BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, TppsConsts.PAY_ROLE_PAYEE));
	}

	public void updateGoldqPayFee(GoldqPayFee goldqPayFee) {
		tppsDAO.updateGoldqPayFee(goldqPayFee);

	}

	public List<GoldqPayFee> getGoldqPayFeeByClientId(String clientId) {
		List<GoldqPayFee> goldqPayFees = tppsDAO.getGoldqPayFeeByClientId(clientId);
		if (goldqPayFees.isEmpty()) {
			/* 生成默认手续费模板 */
			tppsDAO.saveGoldqPayFee(new GoldqPayFee(clientId, TppsConsts.PAY_ROLE_PAYER, BigDecimal.ZERO,
					BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, TppsConsts.PAY_ROLE_PAYEE));
			tppsDAO.saveGoldqPayFee(new GoldqPayFee(clientId, TppsConsts.PAY_ROLE_PAYEE, BigDecimal.ZERO,
					BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, TppsConsts.PAY_ROLE_PAYEE));
			goldqPayFees = tppsDAO.getGoldqPayFeeByClientId(clientId);
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
		return tppsDAO.getGoldqPayClientByPage(hql.toString(), values, currentPage, 5);
	}

	public void updateGoldqPayClient(Integer exId, String clientId, String secretKey, String name, String redirectUrl,
			String customDomain) {
		GoldqPayClient goldqPayClient = tppsDAO.getGoldqPayClientByClientId(clientId);
		goldqPayClient.setExId(exId);
		goldqPayClient.setSecretKey(secretKey);
		goldqPayClient.setName(name);
		goldqPayClient.setRedirectUrl(redirectUrl);
		goldqPayClient.setCustomDomain(customDomain);
		tppsDAO.updateGoldqPayClient(goldqPayClient);
	}
}
