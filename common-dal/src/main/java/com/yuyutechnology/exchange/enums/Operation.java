/**
 * 
 */
package com.yuyutechnology.exchange.enums;

/**
 * @author suzan.wu
 *
 */
public enum Operation {
	/**
	 * 更新用户信息
	 */
	UPDATE_USER_INFO("update_user_info"),
	/**
	 * 冻结用户
	 */
	FREEZE_USER("freeze_user"), 
	/**
	 * 解冻用户
	 */
	DEFROST_USER("defrost_user"), 
	/**
	 * 开启核账
	 */
	OPEN_ACCOUTING_TASK("open_accouting_task"), 
	/**
	 * 关闭核账
	 */
	CLOSE_ACCOUTING_TASK("close_accouting_task"), 
	/**
	 * 提现退回
	 */
	WITHDRAW_REFUND("withdraw_refund"), 
	/**
	 * 提现重新审核
	 */
	WITHDRAW_RE_REVIEW("withdraw_re_review"),
	/**
	 * 提现重新划账
	 */
	WITHDRAW_RE_REMIT("withdraw_re_remit"),
	/**
	 *  添加币种
	 */
	ADD_CURRENCY("add_currency"),
	/**
	 * 编辑币种
	 */
	EDIT_CURRENCY("edit_currency"),
	/**
	 * 上架币种
	 */
	ON_CURRENCY("on_currency"),
	/**
	 * 下架币种
	 */
	OFF_CURRENCY("off_currency"),
	/**
	 * 更新配置
	 */
	UPDATE_CONFIG("update_config");
	
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
