package com.yuyutechnology.exchange.session;

import java.io.Serializable;

public class SessionData implements Serializable {
	private static final long serialVersionUID = 1367760874322776220L;
	private Integer userId;
	private String sessionId;
	private boolean login;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

}