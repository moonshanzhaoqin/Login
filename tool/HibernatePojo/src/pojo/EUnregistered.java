package pojo;
// Generated Nov 14, 2017 2:36:25 PM by Hibernate Tools 5.2.6.Final

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
 * EUnregistered generated by hbm2java
 */
@Entity
@Table(name = "e_unregistered", catalog = "anytime_exchange", uniqueConstraints = @UniqueConstraint(columnNames = "transfer_id"))
public class EUnregistered implements java.io.Serializable {

	private Integer unregisteredId;
	private String areaCode;
	private String userPhone;
	private String currency;
	private BigDecimal amount;
	private Date createTime;
	private int unregisteredStatus;
	private String transferId;
	private String refundTransId;

	public EUnregistered() {
	}

	public EUnregistered(String areaCode, String userPhone, String currency, BigDecimal amount, Date createTime,
			int unregisteredStatus, String transferId) {
		this.areaCode = areaCode;
		this.userPhone = userPhone;
		this.currency = currency;
		this.amount = amount;
		this.createTime = createTime;
		this.unregisteredStatus = unregisteredStatus;
		this.transferId = transferId;
	}

	public EUnregistered(String areaCode, String userPhone, String currency, BigDecimal amount, Date createTime,
			int unregisteredStatus, String transferId, String refundTransId) {
		this.areaCode = areaCode;
		this.userPhone = userPhone;
		this.currency = currency;
		this.amount = amount;
		this.createTime = createTime;
		this.unregisteredStatus = unregisteredStatus;
		this.transferId = transferId;
		this.refundTransId = refundTransId;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "unregistered_id", unique = true, nullable = false)
	public Integer getUnregisteredId() {
		return this.unregisteredId;
	}

	public void setUnregisteredId(Integer unregisteredId) {
		this.unregisteredId = unregisteredId;
	}

	@Column(name = "area_code", nullable = false, length = 5)
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Column(name = "user_phone", nullable = false)
	public String getUserPhone() {
		return this.userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	@Column(name = "currency", nullable = false, length = 3)
	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Column(name = "amount", nullable = false, precision = 20, scale = 4)
	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "unregistered_status", nullable = false)
	public int getUnregisteredStatus() {
		return this.unregisteredStatus;
	}

	public void setUnregisteredStatus(int unregisteredStatus) {
		this.unregisteredStatus = unregisteredStatus;
	}

	@Column(name = "transfer_id", unique = true, nullable = false)
	public String getTransferId() {
		return this.transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	@Column(name = "refund_trans_id")
	public String getRefundTransId() {
		return this.refundTransId;
	}

	public void setRefundTransId(String refundTransId) {
		this.refundTransId = refundTransId;
	}

}
