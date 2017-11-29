package pojo;
// Generated Nov 29, 2017 5:23:39 PM by Hibernate Tools 5.2.6.Final

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EInviter generated by hbm2java
 */
@Entity
@Table(name = "e_inviter", catalog = "anytime_exchange")
public class EInviter implements java.io.Serializable {

	private int userId;
	private int inviteQuantity;
	private BigDecimal inviteBonus;

	public EInviter() {
	}

	public EInviter(int userId, int inviteQuantity, BigDecimal inviteBonus) {
		this.userId = userId;
		this.inviteQuantity = inviteQuantity;
		this.inviteBonus = inviteBonus;
	}

	@Id

	@Column(name = "user_id", unique = true, nullable = false)
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Column(name = "invite_quantity", nullable = false)
	public int getInviteQuantity() {
		return this.inviteQuantity;
	}

	public void setInviteQuantity(int inviteQuantity) {
		this.inviteQuantity = inviteQuantity;
	}

	@Column(name = "invite_bonus", nullable = false, precision = 20, scale = 0)
	public BigDecimal getInviteBonus() {
		return this.inviteBonus;
	}

	public void setInviteBonus(BigDecimal inviteBonus) {
		this.inviteBonus = inviteBonus;
	}

}
