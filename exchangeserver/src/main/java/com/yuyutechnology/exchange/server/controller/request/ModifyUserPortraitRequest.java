package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class ModifyUserPortraitRequest {
	private String uploadFile;

	public String getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(String uploadFile) {
		this.uploadFile = uploadFile;
	}

	public boolean empty() {
		if (StringUtils.isBlank(this.uploadFile)) {
			return true;
		}
		return false;
	}
	
	
}
