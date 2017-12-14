package pojo;
// Generated Dec 14, 2017 3:09:15 PM by Hibernate Tools 5.2.6.Final

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
 * EBadAccount generated by hbm2java
 */
@Entity
@Table(name = "e_bad_account", catalog = "anytime_exchange")
public class EBadAccount implements java.io.Serializable {

	private Integer badAccountId;
	private int userId;
	private String currency;
	private BigDecimal sumAmount;
	private BigDecimal balanceHistory;
	private BigDecimal balanceNow;
	private Date startTime;
	private Date endTime;
	private long startSeqId;
	private long endSeqId;
	private int badAccountStatus;
	private String transferId;

	public EBadAccount() {
	}

	public EBadAccount(int userId, String currency, BigDecimal sumAmount, BigDecimal balanceHistory,
			BigDecimal balanceNow, Date startTime, Date endTime, long startSeqId, long endSeqId, int badAccountStatus,
			String transferId) {
		this.userId = userId;
		this.currency = currency;
		this.sumAmount = sumAmount;
		this.balanceHistory = balanceHistory;
		this.balanceNow = balanceNow;
		this.startTime = startTime;
		this.endTime = endTime;
		this.startSeqId = startSeqId;
		this.endSeqId = endSeqId;
		this.badAccountStatus = badAccountStatus;
		this.transferId = transferId;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "bad_account_id", unique = true, nullable = false)
	public Integer getBadAccountId() {
		return this.badAccountId;
	}

	public void setBadAccountId(Integer badAccountId) {
		this.badAccountId = badAccountId;
	}

	@Column(name = "user_id", nullable = false)
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Column(name = "currency", nullable = false, length = 3)
	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Column(name = "sum_amount", nullable = false, precision = 20, scale = 4)
	public BigDecimal getSumAmount() {
		return this.sumAmount;
	}

	public void setSumAmount(BigDecimal sumAmount) {
		this.sumAmount = sumAmount;
	}

	@Column(name = "balance_history", nullable = false, precision = 20, scale = 4)
	public BigDecimal getBalanceHistory() {
		return this.balanceHistory;
	}

	public void setBalanceHistory(BigDecimal balanceHistory) {
		this.balanceHistory = balanceHistory;
	}

	@Column(name = "balance_now", nullable = false, precision = 20, scale = 4)
	public BigDecimal getBalanceNow() {
		return this.balanceNow;
	}

	public void setBalanceNow(BigDecimal balanceNow) {
		this.balanceNow = balanceNow;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time", nullable = false, length = 19)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time", nullable = false, length = 19)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "start_seq_id", nullable = false)
	public long getStartSeqId() {
		return this.startSeqId;
	}

	public void setStartSeqId(long startSeqId) {
		this.startSeqId = startSeqId;
	}

	@Column(name = "end_seq_id", nullable = false)
	public long getEndSeqId() {
		return this.endSeqId;
	}

	public void setEndSeqId(long endSeqId) {
		this.endSeqId = endSeqId;
	}

	@Column(name = "bad_account_status", nullable = false)
	public int getBadAccountStatus() {
		return this.badAccountStatus;
	}

	public void setBadAccountStatus(int badAccountStatus) {
		this.badAccountStatus = badAccountStatus;
	}

	@Column(name = "transfer_id", nullable = false)
	public String getTransferId() {
		return this.transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

}
