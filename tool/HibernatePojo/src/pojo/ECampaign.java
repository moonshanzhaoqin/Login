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
 * ECampaign generated by hbm2java
 */
@Entity
@Table(name = "e_campaign", catalog = "anytime_exchange")
public class ECampaign implements java.io.Serializable {

	private Integer campaignId;
	private Date startTime;
	private Date endTime;
	private int campaignStatus;
	private BigDecimal campaignBudget;
	private BigDecimal budgetSurplus;
	private BigDecimal inviterBonus;
	private BigDecimal inviteeBonus;
	private Date createTime;
	private Date updateTime;

	public ECampaign() {
	}

	public ECampaign(Date startTime, Date endTime, int campaignStatus, BigDecimal campaignBudget,
			BigDecimal budgetSurplus, BigDecimal inviterBonus, BigDecimal inviteeBonus, Date createTime,
			Date updateTime) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.campaignStatus = campaignStatus;
		this.campaignBudget = campaignBudget;
		this.budgetSurplus = budgetSurplus;
		this.inviterBonus = inviterBonus;
		this.inviteeBonus = inviteeBonus;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "campaign_id", unique = true, nullable = false)
	public Integer getCampaignId() {
		return this.campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
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

	@Column(name = "campaign_status", nullable = false)
	public int getCampaignStatus() {
		return this.campaignStatus;
	}

	public void setCampaignStatus(int campaignStatus) {
		this.campaignStatus = campaignStatus;
	}

	@Column(name = "campaign_budget", nullable = false, precision = 20, scale = 0)
	public BigDecimal getCampaignBudget() {
		return this.campaignBudget;
	}

	public void setCampaignBudget(BigDecimal campaignBudget) {
		this.campaignBudget = campaignBudget;
	}

	@Column(name = "budget_surplus", nullable = false, precision = 20, scale = 0)
	public BigDecimal getBudgetSurplus() {
		return this.budgetSurplus;
	}

	public void setBudgetSurplus(BigDecimal budgetSurplus) {
		this.budgetSurplus = budgetSurplus;
	}

	@Column(name = "inviter_bonus", nullable = false, precision = 20, scale = 0)
	public BigDecimal getInviterBonus() {
		return this.inviterBonus;
	}

	public void setInviterBonus(BigDecimal inviterBonus) {
		this.inviterBonus = inviterBonus;
	}

	@Column(name = "invitee_bonus", nullable = false, precision = 20, scale = 0)
	public BigDecimal getInviteeBonus() {
		return this.inviteeBonus;
	}

	public void setInviteeBonus(BigDecimal inviteeBonus) {
		this.inviteeBonus = inviteeBonus;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time", nullable = false, length = 19)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
