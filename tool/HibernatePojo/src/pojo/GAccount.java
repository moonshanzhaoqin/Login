package pojo;
// Generated Dec 14, 2017 3:09:15 PM by Hibernate Tools 5.2.6.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * GAccount generated by hbm2java
 */
@Entity
@Table(name = "g_account", catalog = "anytime_exchange")
public class GAccount implements java.io.Serializable {

	private long userId;
	private Long balance;
	private String accountId;

	public GAccount() {
	}

	public GAccount(long userId) {
		this.userId = userId;
	}

	public GAccount(long userId, Long balance, String accountId) {
		this.userId = userId;
		this.balance = balance;
		this.accountId = accountId;
	}

	@Id

	@Column(name = "user_id", unique = true, nullable = false)
	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Column(name = "balance")
	public Long getBalance() {
		return this.balance;
	}

	public void setBalance(Long balance) {
		this.balance = balance;
	}

	@Column(name = "account_id", length = 12)
	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

}
