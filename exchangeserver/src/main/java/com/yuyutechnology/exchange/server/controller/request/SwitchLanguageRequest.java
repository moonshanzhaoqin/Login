package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class SwitchLanguageRequest extends BaseRequest{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8022334354606664791L;
	private String language;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * 判断参数是否为空
	 * 
	 * @return
	 */
	public boolean empty() {
		if (StringUtils.isBlank(this.language)) {
			return true;
		}
		return false;
	}
}
