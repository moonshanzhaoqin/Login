package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class SearchFriendRequest extends BaseRequest{
	/**
	 * 
	 */
	private static final long serialVersionUID = -703862967645471603L;
	@ApiModelProperty(value = "关键词")
	private String keyWords;

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public boolean empty() {
		if (StringUtils.isBlank(this.keyWords)) {
			return true;
		}
		return false;
	}
}
