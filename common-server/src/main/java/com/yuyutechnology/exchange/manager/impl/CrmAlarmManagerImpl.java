package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CrmAlarmDAO;
import com.yuyutechnology.exchange.dao.CrmSupervisorDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.mail.MailManager;
import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.ExchangeRateManager;
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
	SmsManager smsManager;
	@Autowired
	MailManager mailManager;
	@Autowired
	ExchangeRateManager exchangeRateManager;
	

	private static Logger logger = LoggerFactory.getLogger(CrmAlarmManagerImpl.class);

	@Override
	public void addAlarmConfig(String alarmGrade, BigDecimal lowerLimit,
			BigDecimal upperLimit, int alarmMode, int editorid,String supervisorId) {
		CrmAlarm crmAlarm = new CrmAlarm();
		
		crmAlarm.setAlarmGrade(alarmGrade);
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
	public void delAlarmConfig(int alarmId) {
		logger.info("delete alarmId : {}",alarmId);
		crmAlarmDAO.delCrmAlarmConfig(alarmId);
	}

	@Override
	public CrmAlarm getAlarmConfigById(int alarmId) {
		return crmAlarmDAO.getCrmAlarmConfig(alarmId);
	}

	private void alarmBySMS(String supervisorIdArr,BigDecimal difference,BigDecimal lowerLimit,String alarmGrade){
		String[] arr = (supervisorIdArr.replace("[", "").replace("]", "")).split(",");
		
		for (String supervisorId : arr) {
			CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId.trim()));
			smsManager.sendSMS4CriticalAlarm(crmSupervisor.getSupervisorMobile(), 
					difference, lowerLimit, alarmGrade, DateFormatUtils.formatDateGMT8(new Date()));
		}
	}
	
	private void alarmByEmail(String supervisorIdArr,BigDecimal difference,BigDecimal lowerLimit,String alarmGrade){
		String[] arr = (supervisorIdArr.replace("[", "").replace("]", "")).split(",");
		
		for (String supervisorId : arr) {
			CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
			mailManager.mail4criticalAlarm(crmSupervisor.getSupervisorEmail(),
					difference, lowerLimit, alarmGrade, DateFormatUtils.formatDateGMT8(new Date()));
		}
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
		
		List<CrmSupervisor> list = crmSupervisorDAO.
				getCrmSupervisorByCondition(supervisorName, supervisorMobile, supervisorEmail);
		
		if(list == null || list.isEmpty()){
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
	public void updateAlarmConfig(Integer alarmId, String alarmGrade, BigDecimal lowerLimit, BigDecimal upperLimit,
			int alarmMode, int editorid, String adminIdArr) {
		CrmAlarm crmAlarm = crmAlarmDAO.getCrmAlarmConfig(alarmId);
		crmAlarm.setAlarmId(alarmId);
		crmAlarm.setAlarmGrade(alarmGrade);
		crmAlarm.setLowerLimit(lowerLimit);
		crmAlarm.setUpperLimit(upperLimit);
		crmAlarm.setAlarmMode(alarmMode);
		crmAlarm.setSupervisorIdArr(adminIdArr);
		crmAlarm.setEditorid(editorid);
		
		crmAlarmDAO.addCrmAlarmConfig(crmAlarm);
	}
	
	@Override
	public void updateAlarmAvailable(Integer alarmId,int  alarmAvailable){
		CrmAlarm crmAlarm = crmAlarmDAO.getCrmAlarmConfig(alarmId);
		crmAlarm.setAlarmAvailable(alarmAvailable);
		crmAlarmDAO.addCrmAlarmConfig(crmAlarm);
	}
	
	@Override
	public HashMap<String, BigDecimal> getAccountInfo(BigDecimal userHoldingTotalAssets){
		
		HashMap<String, BigDecimal> map = new HashMap<>();
		
//		Ex公司持有的总资产 = 用户充值Goldpay总额 - 用户提现Goldpay总额;
//		预备金剩余量 = 1 - (Ex用户持有的总资产 - Ex公司持有的总资产) / 预备金;
		BigDecimal sumRecharge = transferDAO.sumGoldpayTransAmount(ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE);
		BigDecimal sumWithdraw = transferDAO.sumGoldpayTransAmount(ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW);
		BigDecimal exHoldingTotalAssets = exchangeRateManager.getExchangeResult(ServerConsts.CURRENCY_OF_GOLDPAY, 
				sumRecharge.subtract(sumWithdraw));
		
		logger.info("sumRecharge:{},sumWithdraw:{},exHoldingTotalAssets(USD):{}",new Object[]{sumRecharge,sumWithdraw,exHoldingTotalAssets});
		
		BigDecimal reserveFunds = exchangeRateManager.getExchangeResult(ServerConsts.CURRENCY_OF_GOLDPAY, new BigDecimal("100000000"));
		
		logger.info("reserveFunds (USD) :{}",reserveFunds);
		
		BigDecimal reserveAvailability =(new BigDecimal("1").subtract(
				(userHoldingTotalAssets.subtract(exHoldingTotalAssets)).divide(reserveFunds,5,RoundingMode.FLOOR))).
				multiply(new BigDecimal("100"));
		
		logger.info("ReserveAvailability : {}%",reserveAvailability);
		
		map.put("exHoldingTotalAssets", exHoldingTotalAssets.setScale(5, RoundingMode.FLOOR));
		map.put("userHoldingTotalAssets", userHoldingTotalAssets);
		map.put("reserveFunds", reserveFunds.setScale(5, RoundingMode.FLOOR));
		map.put("reserveAvailability", reserveAvailability.setScale(2, RoundingMode.FLOOR));

		return map;
	}
 	
	
	
	@Override
	public void autoAlarm(BigDecimal userHoldingTotalAssets){

		HashMap<String, BigDecimal> map = getAccountInfo(userHoldingTotalAssets);
		
		if(map == null){
			return ;
		}
		
		List<CrmAlarm> list = crmAlarmDAO.getCrmAlarmConfigListByType(1);
		
		if(list == null){
			logger.warn("No related alarm information is configured ! {}",new Date());
			return ;
		}
		
		for (CrmAlarm crmAlarm : list) {
			if(
				(crmAlarm.getLowerLimit().compareTo(map.get("reserveAvailability")) == 0 
				||crmAlarm.getLowerLimit().compareTo(map.get("reserveAvailability")) == -1)&&
				(crmAlarm.getUpperLimit().compareTo(map.get("reserveAvailability")) == 1)
			){
				
				logger.info("Initiate an alarm, alarmId : {},alarmMode: {}",new Object[]{crmAlarm.getAlarmId(),crmAlarm.getAlarmMode()});

				//发短信
				if(crmAlarm.getAlarmMode() == 1){
					alarmBySMS(crmAlarm.getSupervisorIdArr(), map.get("reserveAvailability"), 
							crmAlarm.getLowerLimit(), crmAlarm.getAlarmGrade());
				}
				//发邮件
				if(crmAlarm.getAlarmMode() == 2){
					alarmByEmail(crmAlarm.getSupervisorIdArr(),map.get("reserveAvailability"), 
							crmAlarm.getLowerLimit(), crmAlarm.getAlarmGrade());
				}
				
				//发邮件+发短信
				if(crmAlarm.getAlarmMode() == 3){
					
					alarmByEmail(crmAlarm.getSupervisorIdArr(),map.get("reserveAvailability"), 
							crmAlarm.getLowerLimit(), crmAlarm.getAlarmGrade());
					
					alarmBySMS(crmAlarm.getSupervisorIdArr(),map.get("reserveAvailability"), 
							crmAlarm.getLowerLimit(), crmAlarm.getAlarmGrade());
				}
				
				//生成警报记录
			}
		}
	}
	
}
