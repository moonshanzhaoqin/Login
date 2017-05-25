/**
 * 
 */
package com.yuyutechnology.exchange.enums;

/**
 * @author suzan.wu
 *
 */
public enum Operation {
	UPDATE_USER_INFO("update_user_info");
	
	private String operationName;

	private Operation(String operationName) {
		this.setOperationName(operationName);
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	
}
