package com.yuyutechnology.exchange.pojo;
// Generated Dec 2, 2016 4:27:04 PM by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Transfer generated by hbm2java
 */
@Entity
@Table(name = "transfer", catalog = "anytime_exchange")
public class Transfer implements java.io.Serializable {

	private String transferId;
	private int userFrom;
	private int userTo;
	private String currency;
	private BigDecimal transferAmount;
	private String transferComment;
	private Date createTime;
	private Date finishTime;
	private int transferStatus;
	private int transferType;

	public Transfer() {
	}

	public Transfer(String transferId, int userFrom, int userTo, String currency, BigDecimal transferAmount,
			int transferStatus, int transferType) {
		this.transferId = transferId;
		this.userFrom = userFrom;
		this.userTo = userTo;
		this.currency = currency;
		this.transferAmount = transferAmount;
		this.transferStatus = transferStatus;
		this.transferType = transferType;
	}

	public Transfer(String transferId, int userFrom, int userTo, String currency, BigDecimal transferAmount,
			String transferComment, Date createTime, Date finishTime, int transferStatus, int transferType) {
		this.transferId = transferId;
		this.userFrom = userFrom;
		this.userTo = userTo;
		this.currency = currency;
		this.transferAmount = transferAmount;
		this.transferComment = transferComment;
		this.createTime = createTime;
		this.finishTime = finishTime;
		this.transferStatus = transferStatus;
		this.transferType = transferType;
	}

	@Id

	@Column(name = "transfer_id", unique = true, nullable = false)
	public String getTransferId() {
		return this.transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	@Column(name = "user_from", nullable = false)
	public int getUserFrom() {
		return this.userFrom;
	}

	public void setUserFrom(int userFrom) {
		this.userFrom = userFrom;
	}

	@Column(name = "user_to", nullable = false)
	public int getUserTo() {
		return this.userTo;
	}

	public void setUserTo(int userTo) {
		this.userTo = userTo;
	}

	@Column(name = "currency", nullable = false, length = 3)
	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Column(name = "transfer_amount", nullable = false, precision = 10)
	public BigDecimal getTransferAmount() {
		return this.transferAmount;
	}

	public void setTransferAmount(BigDecimal transferAmount) {
		this.transferAmount = transferAmount;
	}

	@Column(name = "transfer_comment")
	public String getTransferComment() {
		return this.transferComment;
	}

	public void setTransferComment(String transferComment) {
		this.transferComment = transferComment;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "finish_time", length = 19)
	public Date getFinishTime() {
		return this.finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	@Column(name = "transfer_status", nullable = false)
	public int getTransferStatus() {
		return this.transferStatus;
	}

	public void setTransferStatus(int transferStatus) {
		this.transferStatus = transferStatus;
	}

	@Column(name = "transfer_type", nullable = false)
	public int getTransferType() {
		return this.transferType;
	}

	public void setTransferType(int transferType) {
		this.transferType = transferType;
	}

}
