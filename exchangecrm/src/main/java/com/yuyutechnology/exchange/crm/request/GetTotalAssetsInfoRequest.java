package com.yuyutechnology.exchange.crm.request;

import java.math.BigDecimal;

import com.yuyutechnology.exchange.utils.page.PageBean;

public class GetTotalAssetsInfoRequest {

	private String userPhone;
	private String userName;
	private int isFrozen;
	private BigDecimal upperLimit;
	private BigDecimal lowerLimit;
	
	private PageBean pageBean;

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getIsFrozen() {
		return isFrozen;
	}

	public void setIsFrozen(int isFrozen) {
		this.isFrozen = isFrozen;
	}

	public BigDecimal getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(BigDecimal upperLimit) {
		this.upperLimit = upperLimit;
	}

	public BigDecimal getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(BigDecimal lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}
}
