package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class ContactUsRequest {
	private String name;
	private String email;
	private String category;
	private String enquiry;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getEnquiry() {
		return enquiry;
	}

	public void setEnquiry(String enquiry) {
		this.enquiry = enquiry;
	}

	/**
	 * 判断参数是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (StringUtils.isBlank(this.name)) {
			return true;
		}
		if (StringUtils.isBlank(this.email)) {
			return true;
		}
		if (StringUtils.isBlank(this.category)) {
			return true;
		}
		if (StringUtils.isBlank(this.enquiry)) {
			return true;
		}
		return false;
	}

}
