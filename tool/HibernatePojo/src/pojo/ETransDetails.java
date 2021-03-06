package pojo;
// Generated Nov 1, 2017 11:26:58 AM by Hibernate Tools 5.2.3.Final

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
 * ETransDetails generated by hbm2java
 */
@Entity
@Table(name = "e_trans_details", catalog = "anytime_exchange")
public class ETransDetails implements java.io.Serializable {

	private Long detailsId;
	private String transferId;
	private Integer userId;
	private String traderName;
	private String traderAreaCode;
	private String traderPhone;
	private String transCurrency;
	private BigDecimal transAmount;
	private String transRemarks;
	private Date detailsCreateTime;

	public ETransDetails() {
	}

	public ETransDetails(String transferId) {
		this.transferId = transferId;
	}

	public ETransDetails(String transferId, Integer userId, String traderName, String traderAreaCode,
			String traderPhone, String transCurrency, BigDecimal transAmount, String transRemarks,
			Date detailsCreateTime) {
		this.transferId = transferId;
		this.userId = userId;
		this.traderName = traderName;
		this.traderAreaCode = traderAreaCode;
		this.traderPhone = traderPhone;
		this.transCurrency = transCurrency;
		this.transAmount = transAmount;
		this.transRemarks = transRemarks;
		this.detailsCreateTime = detailsCreateTime;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "details_id", unique = true, nullable = false)
	public Long getDetailsId() {
		return this.detailsId;
	}

	public void setDetailsId(Long detailsId) {
		this.detailsId = detailsId;
	}

	@Column(name = "transfer_id", nullable = false, length = 100)
	public String getTransferId() {
		return this.transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	@Column(name = "user_id")
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "trader_name", length = 100)
	public String getTraderName() {
		return this.traderName;
	}

	public void setTraderName(String traderName) {
		this.traderName = traderName;
	}

	@Column(name = "trader_area_code", length = 30)
	public String getTraderAreaCode() {
		return this.traderAreaCode;
	}

	public void setTraderAreaCode(String traderAreaCode) {
		this.traderAreaCode = traderAreaCode;
	}

	@Column(name = "trader_phone", length = 30)
	public String getTraderPhone() {
		return this.traderPhone;
	}

	public void setTraderPhone(String traderPhone) {
		this.traderPhone = traderPhone;
	}

	@Column(name = "trans_currency", length = 3)
	public String getTransCurrency() {
		return this.transCurrency;
	}

	public void setTransCurrency(String transCurrency) {
		this.transCurrency = transCurrency;
	}

	@Column(name = "trans_amount", precision = 20, scale = 4)
	public BigDecimal getTransAmount() {
		return this.transAmount;
	}

	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}

	@Column(name = "trans_remarks", length = 500)
	public String getTransRemarks() {
		return this.transRemarks;
	}

	public void setTransRemarks(String transRemarks) {
		this.transRemarks = transRemarks;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "details_create_time", length = 19)
	public Date getDetailsCreateTime() {
		return this.detailsCreateTime;
	}

	public void setDetailsCreateTime(Date detailsCreateTime) {
		this.detailsCreateTime = detailsCreateTime;
	}

}
