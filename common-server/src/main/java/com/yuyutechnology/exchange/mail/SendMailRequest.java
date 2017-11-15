package com.yuyutechnology.exchange.mail;

import java.util.List;

public class SendMailRequest {
	private String subject;
	private String content;
	private String fromMailAddress;
	private String fromName;
	private List<String> toMails;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFromMailAddress() {
		return fromMailAddress;
	}

	public void setFromMailAddress(String fromMailAddress) {
		this.fromMailAddress = fromMailAddress;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public List<String> getToMails() {
		return toMails;
	}

	public void setToMails(List<String> toMails) {
		this.toMails = toMails;
	}

}
