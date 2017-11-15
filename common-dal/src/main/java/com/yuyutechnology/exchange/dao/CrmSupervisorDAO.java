package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.CrmSupervisor;

public interface CrmSupervisorDAO {
	
	public List<CrmSupervisor> getCrmSupervisorList();
	
	public CrmSupervisor getCrmSupervisorById(Integer supervisorId);
	
	public void delSupervisorById(Integer supervisorId);
	
	public void saveCrmSupervisor(CrmSupervisor crmSupervisor);
	
	public List<CrmSupervisor> getCrmSupervisorByCondition(String supervisorName,
			String supervisorMobile,String supervisorEmail);

}
