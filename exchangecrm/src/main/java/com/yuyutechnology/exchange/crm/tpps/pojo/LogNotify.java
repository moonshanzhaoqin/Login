package com.yuyutechnology.exchange.crm.tpps.pojo;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * LogNotify entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "log_notify")
public class LogNotify implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3415748214467886983L;
	// Fields
	private String payOrderId;
	private Integer status;
	private Date createAt;
	private Date sendAt;
	private String sendUrl;
	private Integer retCode;
	// Constructors
	/** default constructor */
	public LogNotify()
	{
	}
	/** minimal constructor */
	public LogNotify(String payOrderId, Date createAt, Date sendAt)
	{
		this.payOrderId = payOrderId;
		this.createAt = createAt;
		this.sendAt = sendAt;
	}
	/** full constructor */
	public LogNotify(String payOrderId, Integer status, Date createAt,
			Date sendAt, String sendUrl, Integer retCode)
	{
		this.payOrderId = payOrderId;
		this.status = status;
		this.createAt = createAt;
		this.sendAt = sendAt;
		this.sendUrl = sendUrl;
		this.retCode = retCode;
	}
	// Property accessors
	@Id
	@Column(name = "pay_order_id", unique = true, nullable = false, length = 32)
	public String getPayOrderId()
	{
		return this.payOrderId;
	}
	public void setPayOrderId(String payOrderId)
	{
		this.payOrderId = payOrderId;
	}
	@Column(name = "status")
	public Integer getStatus()
	{
		return this.status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
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
	@Column(name = "send_at", nullable = false, length = 19)
	public Date getSendAt()
	{
		return this.sendAt;
	}
	public void setSendAt(Date sendAt)
	{
		this.sendAt = sendAt;
	}
	@Column(name = "send_url", length = 256)
	public String getSendUrl()
	{
		return this.sendUrl;
	}
	public void setSendUrl(String sendUrl)
	{
		this.sendUrl = sendUrl;
	}
	@Column(name = "ret_code")
	public Integer getRetCode()
	{
		return this.retCode;
	}
	public void setRetCode(Integer retCode)
	{
		this.retCode = retCode;
	}
}