package pojo;
// Generated Jun 5, 2017 10:04:57 AM by Hibernate Tools 5.2.1.Final

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
 * ETransfer generated by hbm2java
 */
@Entity
@Table(name = "e_transfer", catalog = "anytime_exchange")
public class ETransfer implements java.io.Serializable {

	private String transferId;
	private int version;
	private int userFrom;
	private int userTo;
	private String areaCode;
	private String phone;
	private String currency;
	private BigDecimal transferAmount;
	private String transferComment;
	private String userComment;
	private Date createTime;
	private Date finishTime;
	private int transferStatus;
	private int transferType;
	private Integer noticeId;
	private String goldpayResult;
	private String goldpayName;
	private String goldpayAcount;
	private String paypalCurrency;
	private Long paypalExchange;

	public ETransfer() {
	}

	public ETransfer(String transferId, int userFrom, int userTo, String currency, BigDecimal transferAmount,
			int transferStatus, int transferType) {
		this.transferId = transferId;
		this.userFrom = userFrom;
		this.userTo = userTo;
		this.currency = currency;
		this.transferAmount = transferAmount;
		this.transferStatus = transferStatus;
		this.transferType = transferType;
	}

	public ETransfer(String transferId, int userFrom, int userTo, String areaCode, String phone, String currency,
			BigDecimal transferAmount, String transferComment, String userComment, Date createTime, Date finishTime,
			int transferStatus, int transferType, Integer noticeId, String goldpayResult, String goldpayName,
			String goldpayAcount, String paypalCurrency, Long paypalExchange) {
		this.transferId = transferId;
		this.userFrom = userFrom;
		this.userTo = userTo;
		this.areaCode = areaCode;
		this.phone = phone;
		this.currency = currency;
		this.transferAmount = transferAmount;
		this.transferComment = transferComment;
		this.userComment = userComment;
		this.createTime = createTime;
		this.finishTime = finishTime;
		this.transferStatus = transferStatus;
		this.transferType = transferType;
		this.noticeId = noticeId;
		this.goldpayResult = goldpayResult;
		this.goldpayName = goldpayName;
		this.goldpayAcount = goldpayAcount;
		this.paypalCurrency = paypalCurrency;
		this.paypalExchange = paypalExchange;
	}

	@Id

	@Column(name = "transfer_id", unique = true, nullable = false)
	public String getTransferId() {
		return this.transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	@Version
	@Column(name = "version", nullable = false)
	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
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

	@Column(name = "area_code", length = 5)
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

	@Column(name = "user_comment", length = 500)
	public String getUserComment() {
		return this.userComment;
	}

	public void setUserComment(String userComment) {
		this.userComment = userComment;
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

	@Column(name = "notice_id")
	public Integer getNoticeId() {
		return this.noticeId;
	}

	public void setNoticeId(Integer noticeId) {
		this.noticeId = noticeId;
	}

	@Column(name = "goldpay_result")
	public String getGoldpayResult() {
		return this.goldpayResult;
	}

	public void setGoldpayResult(String goldpayResult) {
		this.goldpayResult = goldpayResult;
	}

	@Column(name = "goldpay_name")
	public String getGoldpayName() {
		return this.goldpayName;
	}

	public void setGoldpayName(String goldpayName) {
		this.goldpayName = goldpayName;
	}

	@Column(name = "goldpay_acount")
	public String getGoldpayAcount() {
		return this.goldpayAcount;
	}

	public void setGoldpayAcount(String goldpayAcount) {
		this.goldpayAcount = goldpayAcount;
	}

	@Column(name = "paypal_currency", length = 3)
	public String getPaypalCurrency() {
		return this.paypalCurrency;
	}

	public void setPaypalCurrency(String paypalCurrency) {
		this.paypalCurrency = paypalCurrency;
	}

	@Column(name = "paypal_exchange", precision = 10, scale = 0)
	public Long getPaypalExchange() {
		return this.paypalExchange;
	}

	public void setPaypalExchange(Long paypalExchange) {
		this.paypalExchange = paypalExchange;
	}

}
