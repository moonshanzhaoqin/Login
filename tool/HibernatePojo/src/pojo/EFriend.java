package pojo;
// Generated May 25, 2017 5:41:24 PM by Hibernate Tools 5.2.1.Final

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EFriend generated by hbm2java
 */
@Entity
@Table(name = "e_friend", catalog = "anytime_exchange")
public class EFriend implements java.io.Serializable {

	private EFriendId id;
	private EUser EUser;
	private Date createTime;

	public EFriend() {
	}

	public EFriend(EFriendId id, EUser EUser, Date createTime) {
		this.id = id;
		this.EUser = EUser;
		this.createTime = createTime;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false)),
			@AttributeOverride(name = "friendId", column = @Column(name = "friend_id", nullable = false)) })
	public EFriendId getId() {
		return this.id;
	}

	public void setId(EFriendId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "friend_id", nullable = false, insertable = false, updatable = false)
	public EUser getEUser() {
		return this.EUser;
	}

	public void setEUser(EUser EUser) {
		this.EUser = EUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
