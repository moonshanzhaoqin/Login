package pojo;
// Generated Jul 10, 2017 4:40:52 PM by Hibernate Tools 5.2.3.Final

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
 * EWallet generated by hbm2java
 */
@Entity
@Table(name = "e_wallet", catalog = "anytime_exchange")
public class EWallet implements java.io.Serializable {

	private Integer walletId;
	private int userId;
	private String currency;
	private BigDecimal balance;
	private Date updateTime;
	private long updateSeqId;

	public EWallet() {
	}

	public EWallet(int userId, String currency, BigDecimal balance, long updateSeqId) {
		this.userId = userId;
		this.currency = currency;
		this.balance = balance;
		this.updateSeqId = updateSeqId;
	}

	public EWallet(int userId, String currency, BigDecimal balance, Date updateTime, long updateSeqId) {
		this.userId = userId;
		this.currency = currency;
		this.balance = balance;
		this.updateTime = updateTime;
		this.updateSeqId = updateSeqId;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "wallet_id", unique = true, nullable = false)
	public Integer getWalletId() {
		return this.walletId;
	}

	public void setWalletId(Integer walletId) {
		this.walletId = walletId;
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

	@Column(name = "balance", nullable = false, precision = 20, scale = 4)
	public BigDecimal getBalance() {
		return this.balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time", length = 19)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "update_seq_id", nullable = false)
	public long getUpdateSeqId() {
		return this.updateSeqId;
	}

	public void setUpdateSeqId(long updateSeqId) {
		this.updateSeqId = updateSeqId;
	}

}
