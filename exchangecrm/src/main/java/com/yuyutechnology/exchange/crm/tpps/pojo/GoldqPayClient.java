package com.yuyutechnology.exchange.crm.tpps.pojo;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * GoldqPayClient entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "goldq_pay_client")
public class GoldqPayClient implements java.io.Serializable
{
	// Fields
	private long id;
	private long exId;
	private long accountId;
	private String accountNo;
	private String clientId;
	private String secretKey;
	private String name;
	private String redirectUrl;
	private String customDomain;
	// Constructors
	/** default constructor */
	public GoldqPayClient()
	{
	}
	/** minimal constructor */
	public GoldqPayClient(long accountId, String accountNo, String clientId,
			String secretKey, String customDomain)
	{
		this.accountId = accountId;
		this.accountNo = accountNo;
		this.clientId = clientId;
		this.secretKey = secretKey;
		this.customDomain = customDomain;
	}
	/** full constructor */
	public GoldqPayClient(long accountId, String accountNo, String clientId,
			String secretKey, String name, String redirectUrl, String customDomain)
	{
		this.accountId = accountId;
		this.accountNo = accountNo;
		this.clientId = clientId;
		this.secretKey = secretKey;
		this.name = name;
		this.redirectUrl = redirectUrl;
		this.customDomain = customDomain;
	}
	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public long getId()
	{
		return this.id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	@Column(name = "ex_id", nullable = false)
	public long getExId() {
		return exId;
	}
	public void setExId(long exId) {
		this.exId = exId;
	}
	@Column(name = "account_id", nullable = false)
	public long getAccountId()
	{
		return this.accountId;
	}
	public void setAccountId(long accountId)
	{
		this.accountId = accountId;
	}
	@Column(name = "account_NO", nullable = false, length = 32)
	public String getAccountNo()
	{
		return this.accountNo;
	}
	public void setAccountNo(String accountNo)
	{
		this.accountNo = accountNo;
	}
	@Column(name = "client_id", nullable = false, length = 32)
	public String getClientId()
	{
		return this.clientId;
	}
	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}
	@Column(name = "secret_key", nullable = false, length = 64)
	public String getSecretKey()
	{
		return this.secretKey;
	}
	public void setSecretKey(String secretKey)
	{
		this.secretKey = secretKey;
	}
	@Column(name = "name", length = 40)
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	@Column(name = "redirect_url", length = 127)
	public String getRedirectUrl()
	{
		return this.redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl)
	{
		this.redirectUrl = redirectUrl;
	}
	@Column(name = "custom_domain", length = 127)
	public String getCustomDomain() {
		return customDomain;
	}
	public void setCustomDomain(String customDomain) {
		this.customDomain = customDomain;
	}
}