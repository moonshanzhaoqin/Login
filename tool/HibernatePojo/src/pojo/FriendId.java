package pojo;
// Generated Dec 14, 2016 6:26:12 PM by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * FriendId generated by hbm2java
 */
@Embeddable
public class FriendId implements java.io.Serializable {

	private int userId;
	private int friendId;

	public FriendId() {
	}

	public FriendId(int userId, int friendId) {
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
		if (!(other instanceof FriendId))
			return false;
		FriendId castOther = (FriendId) other;

		return (this.getUserId() == castOther.getUserId()) && (this.getFriendId() == castOther.getFriendId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getUserId();
		result = 37 * result + this.getFriendId();
		return result;
	}

}