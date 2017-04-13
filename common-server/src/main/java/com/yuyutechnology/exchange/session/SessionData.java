package com.yuyutechnology.exchange.session;

import java.io.Serializable;

public class SessionData implements Serializable {
	private static final long serialVersionUID = 1367760874322776220L;
	private Integer userId;
	private String sessionId;
	private boolean verify;

	public SessionData() {
	}

	public SessionData(Integer userId, String sessionId) {
		super();
		this.userId = userId;
		this.sessionId = sessionId;
	}

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

	public boolean isVerify() {
		return verify;
	}

	public void setVerify(boolean verify) {
		this.verify = verify;
	}

}