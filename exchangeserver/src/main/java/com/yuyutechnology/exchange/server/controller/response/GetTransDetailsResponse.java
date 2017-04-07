package com.yuyutechnology.exchange.server.controller.response;

import java.util.Date;

public class GetTransDetailsResponse extends BaseResponse {
	
	private String trader;
	private String traderMobile;
	private String money;
	private int transferType;
	private Date createTime;
	private Date finishTime;
	private String transferId;
	
	private boolean isFriend;
	private boolean isRegiste;

	public String getTrader() {
		return trader;
	}

	public void setTrader(String trader) {
		this.trader = trader;
	}

	public String getTraderMobile() {
		return traderMobile;
	}

	public void setTraderMobile(String traderMobile) {
		this.traderMobile = traderMobile;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public int getTransferType() {
		return transferType;
	}

	public void setTransferType(int transferType) {
		this.transferType = transferType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public boolean isFriend() {
		return isFriend;
	}

	public void setFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}

	public boolean isRegiste() {
		return isRegiste;
	}

	public void setRegiste(boolean isRegiste) {
		this.isRegiste = isRegiste;
	}

}
