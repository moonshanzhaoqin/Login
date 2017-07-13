package com.yuyutechnology.exchange;

import org.springframework.core.NestedRuntimeException;

/**
 * 
 */
public class ServerException extends NestedRuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int retCode = 0;

	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public ServerException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public ServerException(String msg, int retCode) {
		super(msg);
		this.retCode = retCode;

	}

	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public ServerException(String msg) {
		super(msg);
	}

	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public ServerException(Throwable cause) {
		super("", cause);
	}

	public int getRetCode() {
		return retCode;
	}

	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}
}
