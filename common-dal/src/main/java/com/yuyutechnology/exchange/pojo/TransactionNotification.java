package com.yuyutechnology.exchange.pojo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transaction_notification")
public class TransactionNotification {
	
	private int noticeId;
	private int sponsorId;
	private int payerId;
	private String currency;
	private BigDecimal amount;
	private String remarks;
	private Date createAt;
	private int noticeStatus;
	private int trading_status;
	
	@Id
	@Column(name = "notice_id", unique = true, nullable = false)
	public int getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(int noticeId) {
		this.noticeId = noticeId;
	}
	@Column(name = "sponsor_id")
	public int getSponsorId() {
		return sponsorId;
	}
	public void setSponsorId(int sponsorId) {
		this.sponsorId = sponsorId;
	}
	@Column(name = "payer_id")
	public int getPayerId() {
		return payerId;
	}
	public void setPayerId(int payerId) {
		this.payerId = payerId;
	}
	@Column(name = "currency")
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	@Column(name = "amount")
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	@Column(name = "remarks")
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Column(name = "create_at")
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	@Column(name = "notice_status")
	public int getNoticeStatus() {
		return noticeStatus;
	}
	public void setNoticeStatus(int noticeStatus) {
		this.noticeStatus = noticeStatus;
	}
	@Column(name = "trading_status")
	public int getTrading_status() {
		return trading_status;
	}
	public void setTrading_status(int trading_status) {
		this.trading_status = trading_status;
	}

}
