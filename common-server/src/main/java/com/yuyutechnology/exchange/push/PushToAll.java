package com.yuyutechnology.exchange.push;

public class PushToAll {
	private String appName;
	private String title;
	private String body;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
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
		return "PushToAll [appName=" + appName + ", title=" + title + ", body=" + body + "]";
	}

}
