package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CrmAlarmDAO;
import com.yuyutechnology.exchange.dao.CrmSupervisorDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.mail.MailManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.pojo.CrmAlarm;
import com.yuyutechnology.exchange.pojo.CrmSupervisor;
import com.yuyutechnology.exchange.sms.SmsManager;
import com.yuyutechnology.exchange.utils.DateFormatUtils;

@Service
public class CrmAlarmManagerImpl implements CrmAlarmManager {

	@Autowired
	CrmAlarmDAO crmAlarmDAO;
	@Autowired
	CrmSupervisorDAO crmSupervisorDAO;
	@Autowired
	TransferDAO transferDAO;
	@Autowired
	ConfigManager configManager;

	@Autowired
	SmsManager smsManager;
	@Autowired
	MailManager mailManager;
	@Autowired
	OandaRatesManager oandaRatesManager;

	private static Logger logger = LogManager.getLogger(CrmAlarmManagerImpl.class);

	@Override
	public void addAlarmConfig(int alarmType, BigDecimal lowerLimit, BigDecimal upperLimit, int alarmMode,
			int editorid, String supervisorId) {
		CrmAlarm crmAlarm = new CrmAlarm();

		crmAlarm.setAlarmType(alarmType);
		crmAlarm.setAlarmMode(alarmMode);
		crmAlarm.setCreateAt(new Date());
		crmAlarm.setLowerLimit(lowerLimit);
		crmAlarm.setUpperLimit(upperLimit);
		crmAlarm.setSupervisorIdArr(supervisorId);
		crmAlarm.setEditorid(editorid);
		crmAlarm.setAlarmAvailable(1);

		crmAlarmDAO.addCrmAlarmConfig(crmAlarm);

	}

	@Override
	public int delAlarmConfig(int alarmId) {
		logger.info("delete alarmId : {}", alarmId);
		return crmAlarmDAO.delCrmAlarmConfig(alarmId);
	}

	@Override
	public CrmAlarm getAlarmConfigById(int alarmId) {
		return crmAlarmDAO.getCrmAlarmConfig(alarmId);
	}
	
	
	
	@Override
	public List<CrmSupervisor> getCrmSupervisorList() {
		return crmSupervisorDAO.getCrmSupervisorList();
	}

	@Override
	public List<CrmAlarm> getCrmAlarmConfigList() {
		return crmAlarmDAO.getCrmAlarmConfigList();
	}

	@Override
	public void delSupervisorById(Integer supervisorId) {
		crmSupervisorDAO.delSupervisorById(supervisorId);
	}

	@Override
	public String saveSupervisor(String supervisorName, String supervisorMobile, String supervisorEmail) {

		List<CrmSupervisor> list = crmSupervisorDAO.getCrmSupervisorByCondition(supervisorName, supervisorMobile,
				supervisorEmail);

		if (list == null || list.isEmpty()) {
			CrmSupervisor crmSupervisor = new CrmSupervisor();
			crmSupervisor.setSupervisorName(supervisorName);
			crmSupervisor.setSupervisorMobile(supervisorMobile);
			crmSupervisor.setSupervisorEmail(supervisorEmail);
			crmSupervisor.setUpdateAt(new Date());

			crmSupervisorDAO.saveCrmSupervisor(crmSupervisor);

			return "success";
		}
		return "fail";

	}

	@Override
	public void updateAlarmConfig(Integer alarmId, int alarmType, BigDecimal lowerLimit, BigDecimal upperLimit,
			int alarmMode, int editorid, String adminIdArr) {
		CrmAlarm crmAlarm = crmAlarmDAO.getCrmAlarmConfig(alarmId);
		crmAlarm.setAlarmId(alarmId);
		crmAlarm.setAlarmType(alarmType);
		crmAlarm.setLowerLimit(lowerLimit);
		crmAlarm.setUpperLimit(upperLimit);
		crmAlarm.setAlarmMode(alarmMode);
		crmAlarm.setSupervisorIdArr(adminIdArr);
		crmAlarm.setEditorid(editorid);

		crmAlarmDAO.addCrmAlarmConfig(crmAlarm);
	}

	@Override
	public int updateAlarmAvailable(Integer alarmId, int alarmAvailable) {
		CrmAlarm crmAlarm = crmAlarmDAO.getCrmAlarmConfig(alarmId);
		crmAlarm.setAlarmAvailable(alarmAvailable);
		crmAlarmDAO.addCrmAlarmConfig(crmAlarm);
		return crmAlarm.getAlarmType();
	}

	@Override
	public HashMap<String, BigDecimal> getAccountInfo(BigDecimal userHoldingTotalAssets) {

		HashMap<String, BigDecimal> map = new HashMap<>();

		// Ex公司持有的总资产 = 用户充值Goldpay总额 - 用户提现Goldpay总额;
		// 预备金剩余量 = 1 - (Ex用户持有的总资产 - Ex公司持有的总资产) / 预备金;
		BigDecimal sumRecharge = transferDAO.sumGoldpayTransAmount(ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE);
		BigDecimal sumWithdraw = transferDAO.sumGoldpayTransAmount(ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW);
		BigDecimal exHoldingTotalAssets = oandaRatesManager.getDefaultCurrencyAmount(ServerConsts.CURRENCY_OF_GOLDPAY,
				sumRecharge.subtract(sumWithdraw));

		logger.info("sumRecharge:{},sumWithdraw:{},exHoldingTotalAssets(USD):{}",
				new Object[] { sumRecharge, sumWithdraw, exHoldingTotalAssets });

		String reserveFundsStr = configManager.getConfigStringValue(ConfigKeyEnum.RESERVEFUNDS, "100000000");
		BigDecimal reserveFunds = oandaRatesManager.getDefaultCurrencyAmount(ServerConsts.CURRENCY_OF_GOLDPAY,
				new BigDecimal(reserveFundsStr));

		logger.info("reserveFunds (USD) :{}", reserveFunds);

		BigDecimal reserveAvailability = BigDecimal.ZERO;

		if (reserveFunds.compareTo(BigDecimal.ZERO) != 0) {
			reserveAvailability = (BigDecimal.ONE.subtract(
					(userHoldingTotalAssets.subtract(exHoldingTotalAssets.setScale(4, RoundingMode.DOWN))).divide(reserveFunds, 5, RoundingMode.DOWN)))
							.multiply(new BigDecimal("100"));
		}
		logger.info("userHoldingTotalAssets:{} ", userHoldingTotalAssets);
		logger.info("exHoldingTotalAssets:{} ", exHoldingTotalAssets);
		logger.info("reserveFunds:{}", reserveFunds);
		logger.info("ReserveAvailability : {}%", reserveAvailability);

		map.put("exHoldingTotalAssets", exHoldingTotalAssets.setScale(4, RoundingMode.DOWN));
		map.put("userHoldingTotalAssets", userHoldingTotalAssets);
//		map.put("exHoldingTotalAssets", new BigDecimal("273.1973"));
//		map.put("userHoldingTotalAssets", new BigDecimal("273.0218"));
		map.put("reserveFunds", reserveFunds.setScale(4, RoundingMode.DOWN));
		map.put("reserveAvailability", reserveAvailability.setScale(2, RoundingMode.DOWN));

		return map;
	}

	@SuppressWarnings("serial")
	@Override
	public void autoAlarm(BigDecimal userHoldingTotalAssets) {

		final HashMap<String, BigDecimal> map = getAccountInfo(userHoldingTotalAssets);

		if (map == null) {
			return;
		}

		List<CrmAlarm> list = crmAlarmDAO.getConfigListByTypeAndStatus(0, 1);

		if (list == null) {
			logger.warn("No related alarm information is configured ! {}", new Date());
			return;
		}

		for (final CrmAlarm crmAlarm : list) {
			if ((crmAlarm.getLowerLimit().compareTo(map.get("reserveAvailability")) == 0
					|| crmAlarm.getLowerLimit().compareTo(map.get("reserveAvailability")) == -1)
					&& (crmAlarm.getUpperLimit().compareTo(map.get("reserveAvailability")) == 1)) {

				logger.info("Initiate an alarm, alarmId : {},alarmMode: {}",
						new Object[] { crmAlarm.getAlarmId(), crmAlarm.getAlarmMode() });
				
				alarmNotice(crmAlarm.getSupervisorIdArr(),"reserveEarlyWarning",crmAlarm.getAlarmMode(),new HashMap<String,Object>(){
					{
						put("difference", map.get("reserveAvailability"));
						put("lowerLimit", crmAlarm.getLowerLimit());
					}
				});
				// 生成警报记录
			}
		}
	}

	@Override
	public HashMap<String, BigDecimal> getLargeTransLimit() {
		HashMap<String, BigDecimal> map = new HashMap<>();
		
		BigDecimal transferLimitPerPay =  BigDecimal.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITPERPAY, 10000d));
		BigDecimal exchangeLimitPerPay =  BigDecimal.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.EXCHANGELIMITPERPAY, 100000d));
		
		map.put("transferLimitPerPay", transferLimitPerPay);
		map.put("exchangeLimitPerPay", exchangeLimitPerPay);
		
		return map;
	}
	
	@Override
	public void alarmNotice(String supervisorIdArr,String features,int earlyWarningMode,HashMap<String, Object> params){
		String[] arr = (supervisorIdArr.replace("[", "").replace("]", "")).split(",");
		switch (features) {
		case "reserveEarlyWarning":
			//短信
			if(earlyWarningMode == 1){
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
					if(crmSupervisor != null){
						smsManager.sendSMS4CriticalAlarm(crmSupervisor.getSupervisorMobile(), 
								new BigDecimal(params.get("difference").toString()), 
								new BigDecimal(params.get("lowerLimit").toString()),
								DateFormatUtils.formatDateGMT8(new Date()));
					}
				}
			}
			//邮件
			if(earlyWarningMode == 2){
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId.trim()));
					if(crmSupervisor != null){
						mailManager.mail4criticalAlarm(crmSupervisor.getSupervisorEmail(), 
								new BigDecimal(params.get("difference").toString()), 
								new BigDecimal(params.get("lowerLimit").toString()),
								DateFormatUtils.formatDateGMT8(new Date()));
					}
				}
			}
			//短信邮件
			if(earlyWarningMode == 3){
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
					if(crmSupervisor != null){
						mailManager.mail4criticalAlarm(crmSupervisor.getSupervisorEmail(), 
								new BigDecimal(params.get("difference").toString()), 
								new BigDecimal(params.get("lowerLimit").toString()),
								DateFormatUtils.formatDateGMT8(new Date()));
						smsManager.sendSMS4CriticalAlarm(crmSupervisor.getSupervisorMobile(), 
								new BigDecimal(params.get("difference").toString()), 
								new BigDecimal(params.get("lowerLimit").toString()),
								DateFormatUtils.formatDateGMT8(new Date()));
					}
				}
			}
			
			break;
		case "largeTransactionWarning":
			//短信
			if(earlyWarningMode == 1){
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
					if(crmSupervisor != null){
						smsManager.sendSMS4LargeTrans(crmSupervisor.getSupervisorMobile(), 
								params.get("payerMobile").toString(),
								params.get("payeeMobile").toString(), 
								new BigDecimal(params.get("amount").toString()),
								params.get("currency").toString(), 
								DateFormatUtils.formatDateGMT8(new Date()));
					}
				}
			}
			//邮件
			if(earlyWarningMode == 2){
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId.trim()));
					if(crmSupervisor != null){
						mailManager.mail4LargeTrans(crmSupervisor.getSupervisorEmail(), 
								params.get("payerMobile").toString(),
								params.get("payeeMobile").toString(), 
								new BigDecimal(params.get("amount").toString()),
								params.get("currency").toString(), 
								DateFormatUtils.formatDateGMT8(new Date()));
					}
				}
			}
			//短信邮件
			if(earlyWarningMode == 3){
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
					if(crmSupervisor != null){
						mailManager.mail4LargeTrans(crmSupervisor.getSupervisorEmail(), 
								params.get("payerMobile").toString(),
								params.get("payeeMobile").toString(), 
								new BigDecimal(params.get("amount").toString()),
								params.get("currency").toString(), 
								DateFormatUtils.formatDateGMT8(new Date()));
						smsManager.sendSMS4LargeTrans(crmSupervisor.getSupervisorMobile(), 
								params.get("payerMobile").toString(),
								params.get("payeeMobile").toString(), 
								new BigDecimal(params.get("amount").toString()),
								params.get("currency").toString(), 
								DateFormatUtils.formatDateGMT8(new Date()));
					}
				}
			}
			
			break;
			
		case "largeExchangeWarning":
			//短信
			if(earlyWarningMode == 1){
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
					if(crmSupervisor != null){
						smsManager.sendSMS4LargeExchange(crmSupervisor.getSupervisorMobile(), 
								params.get("payerMobile").toString(), 
								DateFormatUtils.formatDateGMT8(new Date()), 
								new BigDecimal(params.get("amountOut").toString()), 
								params.get("currencyOut").toString(), 
								new BigDecimal(params.get("amountIn").toString()), 
								params.get("currencyIn").toString());
					}
					
				}
			}
			//邮件
			if(earlyWarningMode == 2){
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId.trim()));
					if(crmSupervisor != null){
						mailManager.mail4LargeExchange(crmSupervisor.getSupervisorEmail(), 
								params.get("payerMobile").toString(), 
								DateFormatUtils.formatDateGMT8(new Date()), 
								new BigDecimal(params.get("amountOut").toString()), 
								params.get("currencyOut").toString(), 
								new BigDecimal(params.get("amountIn").toString()), 
								params.get("currencyIn").toString());
					}
				}
			}
			//短信邮件
			if(earlyWarningMode == 3){
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
					if(crmSupervisor != null){
						mailManager.mail4LargeExchange(crmSupervisor.getSupervisorEmail(), 
								params.get("payerMobile").toString(), 
								DateFormatUtils.formatDateGMT8(new Date()), 
								new BigDecimal(params.get("amountOut").toString()), 
								params.get("currencyOut").toString(), 
								new BigDecimal(params.get("amountIn").toString()), 
								params.get("currencyIn").toString());
						smsManager.sendSMS4LargeExchange(crmSupervisor.getSupervisorMobile(), 
								params.get("payerMobile").toString(), 
								DateFormatUtils.formatDateGMT8(new Date()), 
								new BigDecimal(params.get("amountOut").toString()), 
								params.get("currencyOut").toString(), 
								new BigDecimal(params.get("amountIn").toString()), 
								params.get("currencyIn").toString());
					}
				}
			}
			
			break;

		default:
			break;
		}
	}
}
