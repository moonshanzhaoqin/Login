package com.yuyutechnology.exchange.crm.request;

public class SaveSupervisorRequest {
	
	private String supervisorName;
	private String supervisorMobile;
	private String supervisorEmail;
	
	public String getSupervisorName() {
		return supervisorName;
	}
	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}
	public String getSupervisorMobile() {
		return supervisorMobile;
	}
	public void setSupervisorMobile(String supervisorMobile) {
		this.supervisorMobile = supervisorMobile;
	}
	public String getSupervisorEmail() {
		return supervisorEmail;
	}
	public void setSupervisorEmail(String supervisorEmail) {
		this.supervisorEmail = supervisorEmail;
	}
}
