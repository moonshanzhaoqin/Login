package com.yuyutechnology.exchange.pojo;
// Generated Dec 17, 2016 2:05:54 PM by Hibernate Tools 5.1.0.Alpha1

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * Transfer generated by hbm2java
 */
@Entity
@Table(name = "e_transfer")
public class Transfer implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4768242422633741225L;
	private String transferId;
	private int userFrom;
	private int userTo;
	private String areaCode;
	private String phone;
	private String currency;
	private BigDecimal transferAmount;
	private String transferComment;
	private Date createTime;
	private Date finishTime;
	private int transferStatus;
	private int transferType;
	private Integer noticeId;
	private Integer version;
	private String goldpayResult;
	private String goldpayName;
	private String goldpayAcount;
	private String paypalCurrency;
	private BigDecimal paypalExchange;
	private String goldpayOrderId;

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

	public Transfer(String transferId, int userFrom, int userTo, String areaCode, String phone, String currency,
			BigDecimal transferAmount, String transferComment, Date createTime, Date finishTime, int transferStatus,
			int transferType, Integer noticeId, Integer version, String goldpayResult, String goldpayName,
			String goldpayAcount) {
		super();
		this.transferId = transferId;
		this.userFrom = userFrom;
		this.userTo = userTo;
		this.areaCode = areaCode;
		this.phone = phone;
		this.currency = currency;
		this.transferAmount = transferAmount;
		this.transferComment = transferComment;
		this.createTime = createTime;
		this.finishTime = finishTime;
		this.transferStatus = transferStatus;
		this.transferType = transferType;
		this.noticeId = noticeId;
		this.version = version;
		this.goldpayResult = goldpayResult;
		this.goldpayName = goldpayName;
		this.goldpayAcount = goldpayAcount;
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

	@Column(name = "area_code")
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Column(name = "phone")
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "currency", nullable = false, length = 3)
	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Column(name = "transfer_amount", nullable = false, precision = 20, scale = 4)
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

	// @Column(name = "user_comment")
	// public String getUserComment() {
	// return userComment;
	// }
	//
	// public void setUserComment(String userComment) {
	// this.userComment = userComment;
	// }

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

	@Column(name = "notice_id")
	public Integer getNoticeId() {
		return this.noticeId;
	}

	public void setNoticeId(Integer noticeId) {
		this.noticeId = noticeId;
	}

	@Version
	@Column(name = "version")
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Column(name = "goldpay_result")
	public String getGoldpayResult() {
		return goldpayResult;
	}

	public void setGoldpayResult(String goldpayResult) {
		this.goldpayResult = goldpayResult;
	}

	@Column(name = "goldpay_name")
	public String getGoldpayName() {
		return goldpayName;
	}

	public void setGoldpayName(String goldpayName) {
		this.goldpayName = goldpayName;
	}

	@Column(name = "goldpay_acount")
	public String getGoldpayAcount() {
		return goldpayAcount;
	}

	public void setGoldpayAcount(String goldpayAcount) {
		this.goldpayAcount = goldpayAcount;
	}

	@Column(name = "paypal_exchange")
	public BigDecimal getPaypalExchange() {
		return paypalExchange;
	}

	public void setPaypalExchange(BigDecimal paypalExchange) {
		this.paypalExchange = paypalExchange;
	}

	@Column(name = "paypal_currency")
	public String getPaypalCurrency() {
		return paypalCurrency;
	}

	public void setPaypalCurrency(String paypalCurrency) {
		this.paypalCurrency = paypalCurrency;
	}

	@Column(name = "goldpay_order_id")
	public String getGoldpayOrderId() {
		return goldpayOrderId;
	}

	public void setGoldpayOrderId(String goldpayOrderId) {
		this.goldpayOrderId = goldpayOrderId;
	}

}
