package pojo;
// Generated Dec 2, 2016 4:27:04 PM by Hibernate Tools 4.0.0

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
 * Wallet generated by hbm2java
 */
@Entity
@Table(name = "wallet", catalog = "anytime_exchange")
public class Wallet implements java.io.Serializable {

	private Integer walletId;
	private int userId;
	private String currency;
	private BigDecimal balance;
	private Date updateTime;

	public Wallet() {
	}

	public Wallet(int userId, String currency, BigDecimal balance) {
		this.userId = userId;
		this.currency = currency;
		this.balance = balance;
	}

	public Wallet(int userId, String currency, BigDecimal balance, Date updateTime) {
		this.userId = userId;
		this.currency = currency;
		this.balance = balance;
		this.updateTime = updateTime;
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

	@Column(name = "balance", nullable = false, precision = 10)
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

}
