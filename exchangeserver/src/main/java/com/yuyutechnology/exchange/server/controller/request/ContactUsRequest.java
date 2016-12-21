package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class ContactUsRequest {
	private String name;
	private String email;
	private String category;
	private String enquiry;

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
