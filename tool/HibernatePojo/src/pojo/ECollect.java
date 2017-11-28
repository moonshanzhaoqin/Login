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
import javax.persistence.UniqueConstraint;

/**
 * ECollect generated by hbm2java
 */
@Entity
@Table(name = "e_collect", catalog = "anytime_exchange", uniqueConstraints = @UniqueConstraint(columnNames = {
		"area_code", "user_phone", "collect_time" }))
public class ECollect implements java.io.Serializable {

	private Integer collectId;
	private String areaCode;
	private String userPhone;
	private int inviterId;
	private int campaignId;
	private BigDecimal inviterBonus;
	private BigDecimal inviteeBonus;
	private Date collectTime;
	private int registerStatus;
	private int sharePath;

	public ECollect() {
	}

	public ECollect(String areaCode, String userPhone, int inviterId, int campaignId, BigDecimal inviterBonus,
			BigDecimal inviteeBonus, Date collectTime, int registerStatus, int sharePath) {
		this.areaCode = areaCode;
		this.userPhone = userPhone;
		this.inviterId = inviterId;
		this.campaignId = campaignId;
		this.inviterBonus = inviterBonus;
		this.inviteeBonus = inviteeBonus;
		this.collectTime = collectTime;
		this.registerStatus = registerStatus;
		this.sharePath = sharePath;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "collect_id", unique = true, nullable = false)
	public Integer getCollectId() {
		return this.collectId;
	}

	public void setCollectId(Integer collectId) {
		this.collectId = collectId;
	}

	@Column(name = "area_code", nullable = false, length = 5)
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Column(name = "user_phone", nullable = false, length = 11)
	public String getUserPhone() {
		return this.userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	@Column(name = "inviter_id", nullable = false)
	public int getInviterId() {
		return this.inviterId;
	}

	public void setInviterId(int inviterId) {
		this.inviterId = inviterId;
	}

	@Column(name = "campaign_id", nullable = false)
	public int getCampaignId() {
		return this.campaignId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
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
	@Column(name = "collect_time", nullable = false, length = 19)
	public Date getCollectTime() {
		return this.collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

	@Column(name = "register_status", nullable = false)
	public int getRegisterStatus() {
		return this.registerStatus;
	}

	public void setRegisterStatus(int registerStatus) {
		this.registerStatus = registerStatus;
	}

	@Column(name = "share_path", nullable = false)
	public int getSharePath() {
		return this.sharePath;
	}

	public void setSharePath(int sharePath) {
		this.sharePath = sharePath;
	}

}
