package com.yuyutechnology.exchange.push;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class PushToTag {
	private String appName;
	private String tagName;
	private String title;
	private String body;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	@ApiModelProperty(allowableValues = "ZH,TW,EN")
	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "PushToTag [appName=" + appName + ", tagName=" + tagName + ", title=" + title + ", body=" + body + "]";
	}

}
