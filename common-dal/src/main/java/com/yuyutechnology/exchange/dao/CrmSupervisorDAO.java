package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.CrmSupervisor;

public interface CrmSupervisorDAO {
	
	public List<CrmSupervisor> getCrmSupervisorList();
	
	public CrmSupervisor getCrmSupervisorById(Integer supervisorId);
	

}