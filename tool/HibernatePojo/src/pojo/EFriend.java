package pojo;
// Generated Dec 19, 2017 12:22:01 PM by Hibernate Tools 5.2.6.Final

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
	private Date createTime;

	public EFriend() {
	}

	public EFriend(EFriendId id, Date createTime) {
		this.id = id;
		this.createTime = createTime;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false)),
			@AttributeOverride(name = "friendId", column = @Column(name = "friend_Id", nullable = false)) })
	public EFriendId getId() {
		return this.id;
	}

	public void setId(EFriendId id) {
		this.id = id;
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
