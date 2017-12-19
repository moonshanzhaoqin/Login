package pojo;
// Generated Dec 19, 2017 12:22:01 PM by Hibernate Tools 5.2.6.Final

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EWalletNow generated by hbm2java
 */
@Entity
@Table(name = "e_wallet_now", catalog = "anytime_exchange")
public class EWalletNow implements java.io.Serializable {

	private EWalletNowId id;
	private BigDecimal balance;
	private Date updateTime;
	private long updateSeqId;

	public EWalletNow() {
	}

	public EWalletNow(EWalletNowId id, BigDecimal balance, long updateSeqId) {
		this.id = id;
		this.balance = balance;
		this.updateSeqId = updateSeqId;
	}

	public EWalletNow(EWalletNowId id, BigDecimal balance, Date updateTime, long updateSeqId) {
		this.id = id;
		this.balance = balance;
		this.updateTime = updateTime;
		this.updateSeqId = updateSeqId;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false)),
			@AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false, length = 3)) })
	public EWalletNowId getId() {
		return this.id;
	}

	public void setId(EWalletNowId id) {
		this.id = id;
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
