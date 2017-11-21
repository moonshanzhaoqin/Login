package com.yuyutechnology.exchange.crm.tpps.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * GoldqPayClient entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "goldq_pay_client", uniqueConstraints = @UniqueConstraint(columnNames = "client_id"))
public class GoldqPayClient implements java.io.Serializable {
	private Long id;
	private int exId;
	private String clientId;
	private String secretKey;
	private String name;
	private String redirectUrl;
	private String customDomain;

	public GoldqPayClient() {
	}

	public GoldqPayClient(int exId, String clientId, String secretKey) {
		this.exId = exId;
		this.clientId = clientId;
		this.secretKey = secretKey;
	}

	public GoldqPayClient(int exId, String clientId, String secretKey, String name, String redirectUrl,
			String customDomain) {
		this.exId = exId;
		this.clientId = clientId;
		this.secretKey = secretKey;
		this.name = name;
		this.redirectUrl = redirectUrl;
		this.customDomain = customDomain;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ex_id", nullable = false)
	public int getExId() {
		return this.exId;
	}

	public void setExId(int exId) {
		this.exId = exId;
	}

	@Column(name = "client_id", unique = true, nullable = false, length = 32)
	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@Column(name = "secret_key", nullable = false, length = 64)
	public String getSecretKey() {
		return this.secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	@Column(name = "name", length = 40)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "redirect_url", length = 127)
	public String getRedirectUrl() {
		return this.redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	@Column(name = "custom_domain", length = 127)
	public String getCustomDomain() {
		return this.customDomain;
	}

	public void setCustomDomain(String customDomain) {
		this.customDomain = customDomain;
	}

}