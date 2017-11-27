package pojo;
// Generated Nov 27, 2017 12:17:17 PM by Hibernate Tools 5.2.6.Final

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
 * EWalletSeq generated by hbm2java
 */
@Entity
@Table(name = "e_wallet_seq", catalog = "anytime_exchange")
public class EWalletSeq implements java.io.Serializable {

	private Long seqId;
	private int userId;
	private int transferType;
	private String currency;
	private BigDecimal amount;
	private String transactionId;
	private Date createTime;

	public EWalletSeq() {
	}

	public EWalletSeq(int userId, int transferType, String currency) {
		this.userId = userId;
		this.transferType = transferType;
		this.currency = currency;
	}

	public EWalletSeq(int userId, int transferType, String currency, BigDecimal amount, String transactionId,
			Date createTime) {
		this.userId = userId;
		this.transferType = transferType;
		this.currency = currency;
		this.amount = amount;
		this.transactionId = transactionId;
		this.createTime = createTime;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "seq_id", unique = true, nullable = false)
	public Long getSeqId() {
		return this.seqId;
	}

	public void setSeqId(Long seqId) {
		this.seqId = seqId;
	}

	@Column(name = "user_id", nullable = false)
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Column(name = "transfer_type", nullable = false)
	public int getTransferType() {
		return this.transferType;
	}

	public void setTransferType(int transferType) {
		this.transferType = transferType;
	}

	@Column(name = "currency", nullable = false, length = 3)
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

	@Column(name = "transaction_id")
	public String getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
