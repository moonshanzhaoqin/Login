package pojo;
// Generated Dec 13, 2017 10:49:46 AM by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ETransactionNotification generated by hbm2java
 */
@Entity
@Table(name = "e_transaction_notification", catalog = "anytime_exchange")
public class ETransactionNotification implements java.io.Serializable {

	private Integer noticeId;
	private Integer sponsorId;
	private Integer payerId;
	private String currency;
	private BigDecimal amount;
	private String remarks;
	private Date createAt;
	private Integer noticeStatus;
	private Integer tradingStatus;

	public ETransactionNotification() {
	}

	public ETransactionNotification(Integer sponsorId, Integer payerId, String currency, BigDecimal amount,
			String remarks, Date createAt, Integer noticeStatus, Integer tradingStatus) {
		this.sponsorId = sponsorId;
		this.payerId = payerId;
		this.currency = currency;
		this.amount = amount;
		this.remarks = remarks;
		this.createAt = createAt;
		this.noticeStatus = noticeStatus;
		this.tradingStatus = tradingStatus;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "notice_id", unique = true, nullable = false)
	public Integer getNoticeId() {
		return this.noticeId;
	}

	public void setNoticeId(Integer noticeId) {
		this.noticeId = noticeId;
	}

	@Column(name = "sponsor_id")
	public Integer getSponsorId() {
		return this.sponsorId;
	}

	public void setSponsorId(Integer sponsorId) {
		this.sponsorId = sponsorId;
	}

	@Column(name = "payer_id")
	public Integer getPayerId() {
		return this.payerId;
	}

	public void setPayerId(Integer payerId) {
		this.payerId = payerId;
	}

	@Column(name = "currency")
	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Column(name = "amount", precision = 20, scale = 4)
	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Column(name = "remarks")
	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_at", length = 19)
	public Date getCreateAt() {
		return this.createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	@Column(name = "notice_status")
	public Integer getNoticeStatus() {
		return this.noticeStatus;
	}

	public void setNoticeStatus(Integer noticeStatus) {
		this.noticeStatus = noticeStatus;
	}

	@Column(name = "trading_status")
	public Integer getTradingStatus() {
		return this.tradingStatus;
	}

	public void setTradingStatus(Integer tradingStatus) {
		this.tradingStatus = tradingStatus;
	}

}
