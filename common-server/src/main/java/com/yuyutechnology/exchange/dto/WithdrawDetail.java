package com.yuyutechnology.exchange.dto;

import java.math.BigDecimal;
import java.util.Date;

public class WithdrawDetail {
	@Override
	public String toString() {
		return "WithdrawDetail [userId=" + userId + ", userName=" + userName + ", goldpayAcount=" + goldpayAcount
				+ ", goldpayName=" + goldpayName + ", transferId=" + transferId + ", transferAmount=" + transferAmount
				+ ", createTime=" + createTime + ", reviewStatus=" + reviewStatus + ", goldpayRemit=" + goldpayRemit
				+ "]";
	}

	private Integer userId;
	private String userName;
	private String goldpayAcount;
	private String goldpayName;
	private String transferId;
	private BigDecimal transferAmount;
	private Date createTime;
	private int reviewStatus;
	private int goldpayRemit;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGoldpayAcount() {
		return goldpayAcount;
	}

	public void setGoldpayAcount(String goldpayAcount) {
		this.goldpayAcount = goldpayAcount;
	}

	public String getGoldpayName() {
		return goldpayName;
	}

	public void setGoldpayName(String goldpayName) {
		this.goldpayName = goldpayName;
	}

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public BigDecimal getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(BigDecimal transferAmount) {
		this.transferAmount = transferAmount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(int reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public int getGoldpayRemit() {
		return goldpayRemit;
	}

	public void setGoldpayRemit(int goldpayRemit) {
		this.goldpayRemit = goldpayRemit;
	}
}
