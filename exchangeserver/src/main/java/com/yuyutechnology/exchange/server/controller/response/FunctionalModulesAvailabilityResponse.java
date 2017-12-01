/**
 * 
 */
package com.yuyutechnology.exchange.server.controller.response;

/**
 * @author suzan.wu
 *
 */
public class FunctionalModulesAvailabilityResponse extends BaseResponse {
	private boolean paypalRecharge;
	private boolean bankRechage;
	private boolean withdrawGold;
	private int goldbullion2goldg;
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
