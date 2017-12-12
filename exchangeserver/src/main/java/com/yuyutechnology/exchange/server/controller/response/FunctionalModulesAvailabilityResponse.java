/**
 * 
 */
package com.yuyutechnology.exchange.server.controller.response;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @author suzan.wu
 *
 */
public class FunctionalModulesAvailabilityResponse extends BaseResponse {
	@ApiModelProperty(value = "Paypal开启状态")
	private boolean paypalRecharge;
	@ApiModelProperty(value = "银行汇款开启状态 ")
	private boolean bankRechage;
	@ApiModelProperty(value = "提取金本开启状态 ")
	private boolean withdrawGold;
	@ApiModelProperty(value = "1根金条对应的黄金克数")
	private int goldbullion2goldg;
	@ApiModelProperty(value = "1g黄金对应的Goldpay数量")
	private int goldg2goldpay;

	public boolean isPaypalRecharge() {
		return paypalRecharge;
	}

	public void setPaypalRecharge(boolean paypalRecharge) {
		this.paypalRecharge = paypalRecharge;
	}

	public boolean isBankRechage() {
		return bankRechage;
	}

	public void setBankRechage(boolean bankRechage) {
		this.bankRechage = bankRechage;
	}

	public boolean isWithdrawGold() {
		return withdrawGold;
	}

	public void setWithdrawGold(boolean withdrawGold) {
		this.withdrawGold = withdrawGold;
	}

	public int getGoldbullion2goldg() {
		return goldbullion2goldg;
	}

	public void setGoldbullion2goldg(int goldbullion2goldg) {
		this.goldbullion2goldg = goldbullion2goldg;
	}

	public int getGoldg2goldpay() {
		return goldg2goldpay;
	}

	public void setGoldg2goldpay(int goldg2goldpay) {
		this.goldg2goldpay = goldg2goldpay;
	}

}
