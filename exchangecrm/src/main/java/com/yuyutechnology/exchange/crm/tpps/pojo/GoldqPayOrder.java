package com.yuyutechnology.exchange.crm.tpps.pojo;
import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;
/**
 * GoldqPayOrder entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "goldq_pay_order")
public class GoldqPayOrder implements java.io.Serializable
{
	// Fields
	private GoldqPayOrderId id;
	private String secretKey;
	private String sessionId;
	private String accountFrom;
	private String accountTo;
	private Integer payAmount;
	private String payType;
	private String nonceStr;
	private String sign;
	private String itemDesc;
	private String attach;
	private Integer timeOut;
	private String returnUrl;
	private Date createAt;
	private Date updateAt;
	private Date expiresAt;
	private String orderType;
	private String status;
	private String payOrderId;
	private String payTransId;
	private String refundOrderId;
	private String refundInfo;
	private String failUrl;
	private Integer refundAmount;
	private Integer version;
	//add by Niklaus at 2016-01-11
	
	private String accountFromName;
	private String accountToName;

	// Constructors
	/** default constructor */
	public GoldqPayOrder()
	{
	}
	/** minimal constructor */
	public GoldqPayOrder(GoldqPayOrderId id, String secretKey,
			String sessionId, Integer payAmount, String payType,
			String returnUrl, Date createAt, Date updateAt, Date expiresAt,
			String orderType, String status)
	{
		this.id = id;
		this.secretKey = secretKey;
		this.sessionId = sessionId;
		this.payAmount = payAmount;
		this.payType = payType;
		this.returnUrl = returnUrl;
		this.createAt = createAt;
		this.updateAt = updateAt;
		this.expiresAt = expiresAt;
		this.orderType = orderType;
		this.status = status;
	}
	/** full constructor */
	public GoldqPayOrder(GoldqPayOrderId id, String secretKey,
			String sessionId, String accountFrom, String accountTo,
			Integer payAmount, String payType, String nonceStr, String sign,
			String itemDesc, String attach, Integer timeOut, String returnUrl,
			Date createAt, Date updateAt, Date expiresAt, String orderType,
			String status, String payOrderId, String refundOrderId,
			String refundInfo, String failUrl, Integer refundAmount,
			Integer version,String accountFromName,String accountToName)
	{
		this.id = id;
		this.secretKey = secretKey;
		this.sessionId = sessionId;
		this.accountFrom = accountFrom;
		this.accountTo = accountTo;
		this.payAmount = payAmount;
		this.payType = payType;
		this.nonceStr = nonceStr;
		this.sign = sign;
		this.itemDesc = itemDesc;
		this.attach = attach;
		this.timeOut = timeOut;
		this.returnUrl = returnUrl;
		this.createAt = createAt;
		this.updateAt = updateAt;
		this.expiresAt = expiresAt;
		this.orderType = orderType;
		this.status = status;
		this.payOrderId = payOrderId;
		this.refundOrderId = refundOrderId;
		this.refundInfo = refundInfo;
		this.failUrl = failUrl;
		this.refundAmount = refundAmount;
		this.version = version;
		this.accountFromName = accountFromName;
		this.accountToName = accountToName;
	}
	// Property accessors
	@EmbeddedId
	@AttributeOverrides(
	{
			@AttributeOverride(name = "orderId", column = @Column(name = "order_id", nullable = false, length = 32)),
			@AttributeOverride(name = "clientId", column = @Column(name = "client_id", nullable = false, length = 32)) })
	public GoldqPayOrderId getId()
	{
		return this.id;
	}
	public void setId(GoldqPayOrderId id)
	{
		this.id = id;
	}
	@Column(name = "secret_key", nullable = false, length = 32)
	public String getSecretKey()
	{
		return this.secretKey;
	}
	public void setSecretKey(String secretKey)
	{
		this.secretKey = secretKey;
	}
	@Column(name = "session_id", nullable = false, length = 32)
	public String getSessionId()
	{
		return this.sessionId;
	}
	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}
	@Column(name = "account_from", length = 32)
	public String getAccountFrom()
	{
		return this.accountFrom;
	}
	public void setAccountFrom(String accountFrom)
	{
		this.accountFrom = accountFrom;
	}
	@Column(name = "account_to", length = 32)
	public String getAccountTo()
	{
		return this.accountTo;
	}
	public void setAccountTo(String accountTo)
	{
		this.accountTo = accountTo;
	}
	@Column(name = "pay_amount", nullable = false)
	public Integer getPayAmount()
	{
		return this.payAmount;
	}
	public void setPayAmount(Integer payAmount)
	{
		this.payAmount = payAmount;
	}
	@Column(name = "pay_type", nullable = false, length = 10)
	public String getPayType()
	{
		return this.payType;
	}
	public void setPayType(String payType)
	{
		this.payType = payType;
	}
	@Column(name = "nonce_str", length = 32)
	public String getNonceStr()
	{
		return this.nonceStr;
	}
	public void setNonceStr(String nonceStr)
	{
		this.nonceStr = nonceStr;
	}
	@Column(name = "sign", length = 32)
	public String getSign()
	{
		return this.sign;
	}
	public void setSign(String sign)
	{
		this.sign = sign;
	}
	@Column(name = "item_desc", length = 127)
	public String getItemDesc()
	{
		return this.itemDesc;
	}
	public void setItemDesc(String itemDesc)
	{
		this.itemDesc = itemDesc;
	}
	@Column(name = "attach", length = 127)
	public String getAttach()
	{
		return this.attach;
	}
	public void setAttach(String attach)
	{
		this.attach = attach;
	}
	@Column(name = "time_out")
	public Integer getTimeOut()
	{
		return this.timeOut;
	}
	public void setTimeOut(Integer timeOut)
	{
		this.timeOut = timeOut;
	}
	@Column(name = "return_url", length = 127)
	public String getReturnUrl()
	{
		return this.returnUrl;
	}
	public void setReturnUrl(String returnUrl)
	{
		this.returnUrl = returnUrl;
	}
	@Column(name = "create_at", nullable = false, length = 19)
	public Date getCreateAt()
	{
		return this.createAt;
	}
	public void setCreateAt(Date createAt)
	{
		this.createAt = createAt;
	}
	@Column(name = "update_at", nullable = false, length = 19)
	public Date getUpdateAt()
	{
		return this.updateAt;
	}
	public void setUpdateAt(Date updateAt)
	{
		this.updateAt = updateAt;
	}
	@Column(name = "expires_at", nullable = false, length = 19)
	public Date getExpiresAt()
	{
		return this.expiresAt;
	}
	public void setExpiresAt(Date expiresAt)
	{
		this.expiresAt = expiresAt;
	}
	@Column(name = "order_type", nullable = false, length = 10)
	public String getOrderType()
	{
		return this.orderType;
	}
	public void setOrderType(String orderType)
	{
		this.orderType = orderType;
	}
	@Column(name = "status", nullable = false, length = 12)
	public String getStatus()
	{
		return this.status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	@Column(name = "pay_order_id", length = 32)
	public String getPayOrderId()
	{
		return this.payOrderId;
	}
	public void setPayOrderId(String payOrderId)
	{
		this.payOrderId = payOrderId;
	}
	@Column(name = "pay_trans_id", length = 32)
	public String getPayTransId() {
		return payTransId;
	}
	public void setPayTransId(String payTransId) {
		this.payTransId = payTransId;
	}
	@Column(name = "refund_order_id", length = 32)
	public String getRefundOrderId()
	{
		return this.refundOrderId;
	}
	public void setRefundOrderId(String refundOrderId)
	{
		this.refundOrderId = refundOrderId;
	}
	@Column(name = "refund_info", length = 127)
	public String getRefundInfo()
	{
		return this.refundInfo;
	}
	public void setRefundInfo(String refundInfo)
	{
		this.refundInfo = refundInfo;
	}
	@Column(name = "fail_url", length = 127)
	public String getFailUrl()
	{
		return this.failUrl;
	}
	public void setFailUrl(String failUrl)
	{
		this.failUrl = failUrl;
	}
	@Column(name = "refund_amount")
	public Integer getRefundAmount()
	{
		return this.refundAmount;
	}
	public void setRefundAmount(Integer refundAmount)
	{
		this.refundAmount = refundAmount;
	}
	@Version
	@Column(name = "version")
	public Integer getVersion()
	{
		return this.version;
	}
	public void setVersion(Integer version)
	{
		this.version = version;
	}
	@Column(name = "account_from_name")
	public String getAccountFromName() {
		return accountFromName;
	}
	public void setAccountFromName(String accountFromName) {
		this.accountFromName = accountFromName;
	}
	@Column(name = "account_to_name")
	public String getAccountToName() {
		return accountToName;
	}
	public void setAccountToName(String accountToName) {
		this.accountToName = accountToName;
	}
}