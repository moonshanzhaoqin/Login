package pojo;
// Generated Jun 5, 2017 10:04:57 AM by Hibernate Tools 5.2.1.Final

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * EExchange generated by hbm2java
 */
@Entity
@Table(name = "e_exchange", catalog = "anytime_exchange")
public class EExchange implements java.io.Serializable {

	private String exchangeId;
	private int version;
	private int userId;
	private String currencyOut;
	private String currencyIn;
	private BigDecimal amountOut;
	private BigDecimal amountIn;
	private BigDecimal exchangeRate;
	private BigDecimal exchangeFeePerThousand;
	private BigDecimal exchangeFeeAmount;
	private Date createTime;
	private Date finishTime;
	private int exchangeStatus;

	public EExchange() {
	}

	public EExchange(String exchangeId, int userId, String currencyOut, String currencyIn, BigDecimal amountOut,
			BigDecimal amountIn, BigDecimal exchangeFeePerThousand, BigDecimal exchangeFeeAmount, int exchangeStatus) {
		this.exchangeId = exchangeId;
		this.userId = userId;
		this.currencyOut = currencyOut;
		this.currencyIn = currencyIn;
		this.amountOut = amountOut;
		this.amountIn = amountIn;
		this.exchangeFeePerThousand = exchangeFeePerThousand;
		this.exchangeFeeAmount = exchangeFeeAmount;
		this.exchangeStatus = exchangeStatus;
	}

	public EExchange(String exchangeId, int userId, String currencyOut, String currencyIn, BigDecimal amountOut,
			BigDecimal amountIn, BigDecimal exchangeRate, BigDecimal exchangeFeePerThousand,
			BigDecimal exchangeFeeAmount, Date createTime, Date finishTime, int exchangeStatus) {
		this.exchangeId = exchangeId;
		this.userId = userId;
		this.currencyOut = currencyOut;
		this.currencyIn = currencyIn;
		this.amountOut = amountOut;
		this.amountIn = amountIn;
		this.exchangeRate = exchangeRate;
		this.exchangeFeePerThousand = exchangeFeePerThousand;
		this.exchangeFeeAmount = exchangeFeeAmount;
		this.createTime = createTime;
		this.finishTime = finishTime;
		this.exchangeStatus = exchangeStatus;
	}

	@Id

	@Column(name = "exchange_id", unique = true, nullable = false)
	public String getExchangeId() {
		return this.exchangeId;
	}

	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}

	@Version
	@Column(name = "version", nullable = false)
	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "user_id", nullable = false)
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Column(name = "currency_out", nullable = false, length = 3)
	public String getCurrencyOut() {
		return this.currencyOut;
	}

	public void setCurrencyOut(String currencyOut) {
		this.currencyOut = currencyOut;
	}

	@Column(name = "currency_in", nullable = false, length = 3)
	public String getCurrencyIn() {
		return this.currencyIn;
	}

	public void setCurrencyIn(String currencyIn) {
		this.currencyIn = currencyIn;
	}

	@Column(name = "amount_out", nullable = false, precision = 20, scale = 4)
	public BigDecimal getAmountOut() {
		return this.amountOut;
	}

	public void setAmountOut(BigDecimal amountOut) {
		this.amountOut = amountOut;
	}

	@Column(name = "amount_in", nullable = false, precision = 20, scale = 4)
	public BigDecimal getAmountIn() {
		return this.amountIn;
	}

	public void setAmountIn(BigDecimal amountIn) {
		this.amountIn = amountIn;
	}

	@Column(name = "exchange_rate", precision = 20, scale = 10)
	public BigDecimal getExchangeRate() {
		return this.exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	@Column(name = "exchange_fee_per_thousand", nullable = false, precision = 20, scale = 4)
	public BigDecimal getExchangeFeePerThousand() {
		return this.exchangeFeePerThousand;
	}

	public void setExchangeFeePerThousand(BigDecimal exchangeFeePerThousand) {
		this.exchangeFeePerThousand = exchangeFeePerThousand;
	}

	@Column(name = "exchange_fee_amount", nullable = false, precision = 20, scale = 4)
	public BigDecimal getExchangeFeeAmount() {
		return this.exchangeFeeAmount;
	}

	public void setExchangeFeeAmount(BigDecimal exchangeFeeAmount) {
		this.exchangeFeeAmount = exchangeFeeAmount;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "finish_time", length = 19)
	public Date getFinishTime() {
		return this.finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	@Column(name = "exchange_status", nullable = false)
	public int getExchangeStatus() {
		return this.exchangeStatus;
	}

	public void setExchangeStatus(int exchangeStatus) {
		this.exchangeStatus = exchangeStatus;
	}

}
