/**
 * 
 */
package com.yuyutechnology.exchange.enums;

/**
 * crm 动作
 * 
 * @author suzan.wu
 */
public enum Operation {

	/**
	 * 登录
	 */
	ADMIN_LOGIN("admin_login"),
	/**
	 * 登出
	 */
	ADMIN_LOGOUT("admin_logout"),
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
	 * 添加币种
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
	UPDATE_CONFIG("update_config"),
	/**
	 * 删除预警人
	 */
	DELETE_SUPERVISOR("delete_supervisor"),
	/**
	 * 添加预警人
	 */
	ADD_SUPERVISOR("add_supervisor"),
	/**
	 * 删除预警信息
	 */
	DELETE_ALARM("delete_alarm"),
	/**
	 * 编辑预警信息
	 */
	EDIT_ALARM("edit_alarm"),
	/**
	 * 添加预警信息
	 */
	ADD_ALARM("add_alarm"),
	/**
	 * 开启预警
	 */
	ON_ALARM("on_alarm"),
	/**
	 * 关闭预警
	 */
	OFF_ALARM("off_alarm"),
	/**
	 *新增活动 
	 */
	ADD_CAMPAIGN("add_campaign"),
	/**
	 * 修改奖励金
	 */
	CHANGE_BOUNS("change_bouns"),
	/**
	 * 追加预算 
	 */
	ADDITIONAL_BUDGET("additional_budget")
	;
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
