/**
 * 
 */
package com.yuyutechnology.exchange.server.controller.request;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * @author silent.sun
 *
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(alphabetic = true)
public class BaseRequest implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sign;

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
