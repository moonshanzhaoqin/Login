package com.yuyutechnology.exchange.pojo;
// Generated Jul 10, 2017 4:40:52 PM by Hibernate Tools 5.2.3.Final

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EInviter generated by hbm2java
 */
@Entity
@Table(name = "e_inviter")
public class Inviter implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7603785212002399278L;
	private Integer userId;
	private int inviteQuantity;
	private BigDecimal inviteBonus;

	public Inviter() {
	}

	public Inviter(Integer userId, int inviteQuantity, BigDecimal inviteBonus) {
		this.userId = userId;
		this.inviteQuantity = inviteQuantity;
		this.inviteBonus = inviteBonus;
	}

	@Id

	@Column(name = "user_id", unique = true, nullable = false)
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
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
