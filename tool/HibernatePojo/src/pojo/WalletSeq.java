package pojo;
// Generated Dec 14, 2016 6:26:12 PM by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * WalletSeq generated by hbm2java
 */
@Entity
@Table(name = "wallet_seq", catalog = "anytime_exchange")
public class WalletSeq implements java.io.Serializable {

	private Integer seqId;
	private int userId;
	private int transferType;
	private String currency;
	private BigDecimal amount;
	private String transactionId;

	public WalletSeq() {
	}

	public WalletSeq(int userId, int transferType, String currency) {
		this.userId = userId;
		this.transferType = transferType;
		this.currency = currency;
	}

	public WalletSeq(int userId, int transferType, String currency, BigDecimal amount, String transactionId) {
		this.userId = userId;
		this.transferType = transferType;
		this.currency = currency;
		this.amount = amount;
		this.transactionId = transactionId;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "seq_id", unique = true, nullable = false)
	public Integer getSeqId() {
		return this.seqId;
	}

	public void setSeqId(Integer seqId) {
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

	@Column(name = "amount", precision = 10)
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

}
