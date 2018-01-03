/**
 * 
 */
package com.yuyutechnology.exchange.server.controller.request;

/**
 * @author silent.sun
 *
 */
public class DecryptRequest extends BaseRequest{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2960041119943645149L;
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
