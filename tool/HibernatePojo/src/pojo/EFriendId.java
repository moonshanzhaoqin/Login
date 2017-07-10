package pojo;
// Generated Jul 10, 2017 4:40:52 PM by Hibernate Tools 5.2.3.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * EFriendId generated by hbm2java
 */
@Embeddable
public class EFriendId implements java.io.Serializable {

	private int userId;
	private int friendId;

	public EFriendId() {
	}

	public EFriendId(int userId, int friendId) {
		this.userId = userId;
		this.friendId = friendId;
	}

	@Column(name = "user_id", nullable = false)
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Column(name = "friend_Id", nullable = false)
	public int getFriendId() {
		return this.friendId;
	}

	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EFriendId))
			return false;
		EFriendId castOther = (EFriendId) other;

		return (this.getUserId() == castOther.getUserId()) && (this.getFriendId() == castOther.getFriendId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getUserId();
		result = 37 * result + this.getFriendId();
		return result;
	}

}
