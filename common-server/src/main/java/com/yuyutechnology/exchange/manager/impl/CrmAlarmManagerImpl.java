package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.dao.CrmAlarmDAO;
import com.yuyutechnology.exchange.dao.CrmSupervisorDAO;
import com.yuyutechnology.exchange.mail.MailManager;
import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.pojo.CrmAlarm;
import com.yuyutechnology.exchange.pojo.CrmSupervisor;
import com.yuyutechnology.exchange.sms.SmsManager;

@Service
public class CrmAlarmManagerImpl implements CrmAlarmManager {
	
	@Autowired
	CrmAlarmDAO crmAlarmDAO;
	@Autowired
	CrmSupervisorDAO crmSupervisorDAO;
	
	@Autowired
	SmsManager smsManager;
	@Autowired
	MailManager mailManager;
	

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
	
	@Override
	public void autoAlarm(BigDecimal difference) {
		List<CrmAlarm> list = crmAlarmDAO.getCrmAlarmConfigList();
		
		if(list == null){
			logger.warn("No related alarm information is configured ! {}",new Date());
			return ;
		}
		
		for (CrmAlarm crmAlarm : list) {
			if((difference.compareTo(crmAlarm.getLowerLimit()) == 1 || 
					difference.compareTo(crmAlarm.getLowerLimit()) == 0) 
					&& (difference.compareTo(crmAlarm.getUpperLimit()) == -1 || 
					difference.compareTo(crmAlarm.getUpperLimit()) == 0)){
				logger.warn("Time : {},difference : {},configId : {},alarmGrade: {}",
						new Object[]{new Date(),difference,crmAlarm.getAlarmId(),
								crmAlarm.getAlarmGrade()});
				
				//发短信
				if(crmAlarm.getAlarmMode() == 1){
					alarmBySMS(crmAlarm.getSupervisorIdArr(), difference, 
							crmAlarm.getLowerLimit(), crmAlarm.getAlarmGrade());
				}
				//发邮件
				if(crmAlarm.getAlarmMode() == 2){
					alarmByEmail(crmAlarm.getSupervisorIdArr(), difference, 
							crmAlarm.getLowerLimit(), crmAlarm.getAlarmGrade());
				}
				
				//发邮件+发短信
				if(crmAlarm.getAlarmMode() == 3){
					
					alarmByEmail(crmAlarm.getSupervisorIdArr(), difference, 
							crmAlarm.getLowerLimit(), crmAlarm.getAlarmGrade());
					
					alarmBySMS(crmAlarm.getSupervisorIdArr(), difference, 
							crmAlarm.getLowerLimit(), crmAlarm.getAlarmGrade());
				}
				
				//生成警报记录
			}
		}
	}

	private void alarmBySMS(String supervisorIdArr,BigDecimal difference,BigDecimal lowerLimit,String alarmGrade){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String[] arr = (supervisorIdArr.replace("[", "").replace("]", "")).split(",");
		
		for (String supervisorId : arr) {
			CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId.trim()));
			smsManager.sendSMS4CriticalAlarm(crmSupervisor.getSupervisorMobile(), 
					difference, lowerLimit, alarmGrade, sdf.format(new Date()));
		}
	}
	
	private void alarmByEmail(String supervisorIdArr,BigDecimal difference,BigDecimal lowerLimit,String alarmGrade){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String[] arr = (supervisorIdArr.replace("[", "").replace("]", "")).split(",");
		
		for (String supervisorId : arr) {
			CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
			mailManager.mail4criticalAlarm(crmSupervisor.getSupervisorEmail(),
					difference, lowerLimit, alarmGrade, sdf.format(new Date()));
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
	public void saveSupervisor(String supervisorName, String supervisorMobile, String supervisorEmail) {
		CrmSupervisor crmSupervisor = new CrmSupervisor();
		crmSupervisor.setSupervisorName(supervisorName);
		crmSupervisor.setSupervisorMobile(supervisorMobile);
		crmSupervisor.setSupervisorEmail(supervisorEmail);
		crmSupervisor.setUpdateAt(new Date());
		
		crmSupervisorDAO.saveCrmSupervisor(crmSupervisor);
		
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
	
}
